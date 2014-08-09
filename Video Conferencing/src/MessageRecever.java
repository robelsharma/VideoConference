/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;

import javax.media.*;   
import javax.media.rtp.*;   
import javax.media.rtp.event.*;   
import javax.media.rtp.rtcp.*;   
import javax.media.protocol.*;   
import javax.media.protocol.DataSource;   
import javax.media.format.AudioFormat;   
import javax.media.format.VideoFormat;   
import javax.media.Format;   
import javax.media.format.FormatChangeEvent;   
import javax.media.control.BufferControl;   
   

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import static messengerclient.ClientConstant.*;
import messengerclient.VideoConference;

/**
 *
 * @author sharma
 */
public class MessageRecever implements Runnable
{
    ObjectInputStream input;
    boolean keepListening=true;
    ClientListListener clientListListener;
    ClientWindowListener clientWindowListener;
    ClientManager clientManager;
    Socket clientSocket;
    ExecutorService clientExecutor;
   


    MessageRecever(Socket getClientSocket,ClientListListener getClientListListener ,ClientWindowListener getClientWindowListener,ClientManager getClientManager)
    {
        clientExecutor=Executors.newCachedThreadPool();
        clientManager=getClientManager;
        clientSocket=getClientSocket;
        try
        {
            input = new ObjectInputStream(getClientSocket.getInputStream());
        }
        catch (IOException ex)
        {}
        clientListListener=getClientListListener;
        clientWindowListener=getClientWindowListener;
    }
    public void run()
    {
        String message,name="",ips = "";
        while(keepListening)
        {
            try
            {
                message = (String) input.readObject();
                System.out.println("user is receiving "+ message);
                StringTokenizer tokens=new StringTokenizer(message);

                String header=tokens.nextToken();
                if(tokens.hasMoreTokens())
                    name=tokens.nextToken();
     
                	
                if(header.equalsIgnoreCase("login"))
                {
                    clientListListener.addToList(name);
 
                }
                else if(header.equalsIgnoreCase(DISCONNECT_STRING))
                {
                    clientListListener.removeFromList(name);
                }
                else if(header.equalsIgnoreCase("server"))
                {
                    clientWindowListener.closeWindow(message);
                }
                
                // Video 
                
                else if(name.equalsIgnoreCase("video") || name.equalsIgnoreCase("video1"))
                {
                	 VideoConference videoConference = new VideoConference(message);
                	  clientExecutor.execute(videoConference);
                	 System.out.println("VIDEO CHAT Thread started :)");
                }
                
                else
                {
                    clientWindowListener.openWindow(message);
                }
            }
            catch (IOException ex)
            {
                clientListListener.removeFromList(name);
            }
            catch (ClassNotFoundException ex)
            {

            }
        }
    }

    void stopListening()
    {
        keepListening=false;
    }
}
