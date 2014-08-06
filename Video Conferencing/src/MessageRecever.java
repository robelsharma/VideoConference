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
                else if(name.equalsIgnoreCase("video"))
                {
                   // clientWindowListener.fileStatus("One File is Receiving");
                  //  String address=tokens.nextToken();
                   // String fileName=tokens.nextToken();
                //    clientExecutor.execute(new FileReceiver(address));
                ips = tokens.nextToken();
                    
			   String st="",pt="";
			   st += ips;
			   System.out.println("#########"+st);

				
			   String str[] = new String[2];
			   str[0]  =  st + "/20002";
			   str[1]  =  st + "/20004";
			   AVReceive2 avReceive= new AVReceive2(str);   
			   //VideoTransmit vt = new VideoTransmit(new MediaLocator("vfw://0"),st,"20006");
			   for(int o=1;o<st.length();o++)pt += st.charAt(o);
			   AVTransmit2 vt = new AVTransmit2(new MediaLocator("vfw://0"),pt,"20006",null);
			   AVTransmit2 at = new AVTransmit2(new MediaLocator("javasound://8000"),pt,"20008",null);
			   at.start();	
			    String result = vt.start();
			    
			 
			        if (!avReceive.initialize())    
			         {   
			             System.err.println("Failed to initialize the sessions.");   
			             System.exit(-1);   
			         }  
			         	
			         try    
			         {   
			             while (!avReceive.isDone())   
			                 Thread.sleep(60000);   
			         }    
			         catch (Exception ex)    
			         {}  
			         	
	
			
				if (result != null) {
				    System.out.println("Error : " + result);
				    System.exit(0);
				}
			
				System.out.println("Start transmission for 60 seconds...");
				 
				     
				// Transmit for 60 seconds and then close the processor
				// This is a safeguard when using a capture data source
				// so that the capture device will be properly released
				// before quitting.
				// The right thing to do would be to have a GUI with a
				// "Stop" button that would call stop on VideoTransmit
				try {
				    Thread.currentThread().sleep(60000);
				} catch (InterruptedException ie) {
				}
			
				// Stop the transmission
				vt.stop();
			   // avReceive.stop();
			                    
                }
                else if(name.equalsIgnoreCase("video1"))
                {
                
               ips = tokens.nextToken();
                    
			   String st="",pt="";
			   st += ips;
			   System.out.println("*******"+st);
			   for(int o=1;o<st.length();o++)pt += st.charAt(o);
			   //VideoTransmit vt = new VideoTransmit(new MediaLocator("vfw://0"),st,"20002");
			   //VideoTransmit at = new VideoTransmit(new MediaLocator("javasound://8000"),st,"20004");
			   
			   AVTransmit2 vt = new AVTransmit2(new MediaLocator("vfw://0"),pt,"20002",null);
			   
			   AVTransmit2 at = new AVTransmit2(new MediaLocator("javasound://8000"),pt,"20004",null);
               at.start();
               String result = vt.start(); 
               	if (result != null) {
	            System.out.println("Error : " + result);
	            System.exit(0);
	           }
    
    
	           System.out.println("Start transmission for 60 seconds..."); 
	          			
			   String str[] = new String[2];
			   str[0]  =  st + "/20006";
			   str[1]  =  st + "/20008";
			   AVReceive2 avReceive= new AVReceive2(str);
                
                	 if (!avReceive.initialize())    
			         {   
			             System.err.println("Failed to initialize the sessions.");   
			             System.exit(-1);   
			         }  
			         	
			         try    
			         {   
			             while (!avReceive.isDone())   
			                 Thread.sleep(60000);   
			         }    
			         catch (Exception ex)    
			         {}  
			         	
			         try {
	                  Thread.currentThread().sleep(600000);
	                 } catch (InterruptedException ie)
	                 {
	                 }
      
	             // Stop the transmission
	                vt.stop();	
			         	
                	
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
