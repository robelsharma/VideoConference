/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MessengSrserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static MessengSrserver.ServerConstant.*;

public class ServerManager implements MessageListener
{
    ExecutorService serverExeCutor;
    ServerSocket server;
    Socket clientSocket;
    Clients[] client;
    int clientNumber=0;
    static String[] clientTracker;
    String users="";

    public ServerManager()
    {
        client=new Clients[CLIENT_NUMBER];
        clientTracker=new String [CLIENT_NUMBER];
        serverExeCutor=Executors.newCachedThreadPool();
    }

    public void startServer(ServerStatusListener statusListener,ClientListener clientListener)
    {
        try
        {
            statusListener.status("Server is Listening on port : "+SERVER_PORT);

            server=new ServerSocket(SERVER_PORT,BACKLOG);
            serverExeCutor.execute(new ConnectionController(statusListener,clientListener));
        }
        catch(IOException ioe)
        {
            statusListener.status("IOException occured When Server start");
        }
    }

    public void stopServer(ServerStatusListener statusListener)
    {
        try 
        {
            server.close();
            statusListener.status("Server is stoped");
        }
        catch(SocketException ex)
        {
            //ex.printStackTrace();
            statusListener.status("SocketException Occured When Server is going to stoped");
        }
        catch (IOException ioe)
        {
            //ioe.printStackTrace();
            statusListener.status("IOException Occured When Server is going to stoped");
        }
    }

    public void controllConnection(ServerStatusListener statusListener,ClientListener clientListener)
    {
        while(clientNumber<CLIENT_NUMBER)
        { 
            try
            {
                clientSocket= server.accept();
                client[clientNumber]=new Clients(clientListener,clientSocket,this,clientNumber);
                serverExeCutor.execute(client[clientNumber]);
                clientNumber++;
                //System.out.println(clientNumber);
            }
            catch(SocketException ex)
            {
                ex.printStackTrace();
                break;
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
                statusListener.status("Some Problem Occured When connection received");
                break;
            }
        }
    }

       // for sending particular Client
    public void sendInfo(String message)
    {
        StringTokenizer tokens=new StringTokenizer(message);
        String to=tokens.nextToken();

        for(int i=0;i<clientNumber;i++)
        {
            if(clientTracker[i].equalsIgnoreCase(to))
            {
                try
                {
                    client[i].output.writeObject(message);
                    client[i].output.flush();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void sendFile(String sendTo,int file)
    {
        for(int i=0;i<clientNumber;i++)
        {
            if(clientTracker[i].equalsIgnoreCase(sendTo))
            {
                try
                {
                    client[i].output.writeInt(file);
                    client[i].output.flush();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    
    // send all;

    public void sendNameToAll(String message)
    {
        for(int i=0;i<clientNumber;i++)
        {
            try
            {
                System.out.println("Server is sending   "+ message);
                client[i].output.writeObject(message);
                client[i].output.flush();
            }
            catch (IOException ex)
            {}
        }
    }

    class ConnectionController implements Runnable
    {
        ServerStatusListener statusListener;
        ClientListener clientListener;

        ConnectionController(ServerStatusListener getStatusListener,ClientListener getClientListener)
        {
            statusListener=getStatusListener;
            clientListener=getClientListener;
        }

        public void run()
        {
            controllConnection(statusListener,clientListener);
        }
    }
}
