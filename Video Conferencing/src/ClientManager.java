/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import static messengerclient.ClientConstant.*;

/**
 *
 * @author team3
 */
public class ClientManager
{
    ExecutorService clientExecutor;
    Socket clientSocket ;
    boolean isConnected=false;

    ObjectInputStream input;
    ObjectOutputStream output;
    MessageRecever messageRecever;

    public ClientManager()
    {
        clientExecutor=Executors.newCachedThreadPool();
    }

    public void connect(ClientStatusListener clientStatus)
    {
        try
        {
            if(isConnected)
                return;
            else
            {
                clientSocket=new Socket(SERVER_ADDRESS,SERVER_PORT);
                clientStatus.loginStatus("You r connected to :"+SERVER_ADDRESS);
                isConnected=true;
            }
        }
        catch (UnknownHostException ex)
        {
            clientStatus.loginStatus("No Server found");
        }
        catch (IOException ex)
        {
            clientStatus.loginStatus("No Server found");
        }
    }

    public void disconnect(ClientStatusListener clientStatus)
    {
        messageRecever.stopListening();
        try
        {
            clientStatus.loginStatus("You r no longer connected to Server");
            clientSocket.close();
        }
        catch (IOException ex)
        {
        }
    }

    public void sendMessage(String message)
    {
        clientExecutor.execute(new MessageSender(message));
    }

    public void sendFile(String fileName)
    {
        clientExecutor.execute(new FileSender(fileName));
    }

    boolean flageoutput=true;
    class MessageSender implements Runnable
    {
        String message;
        public MessageSender(String getMessage)
        {
            if(flageoutput)
            {
                try
                {
                    output = new ObjectOutputStream(clientSocket.getOutputStream());
                    output.flush();
                    flageoutput=false;
                }
                catch (IOException ex)
                {
                }
            }
            message=getMessage;
            System.out.println("user is sending   "+ message);
        }
        public void run()
        {
            try
            {
                output.writeObject(message);
                output.flush();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public void receiveMessage(ClientListListener getClientListListener ,ClientWindowListener getClientWindowListener)
    {
        messageRecever=new MessageRecever(clientSocket,getClientListListener, getClientWindowListener,this);
        clientExecutor.execute(messageRecever);
    }
}
