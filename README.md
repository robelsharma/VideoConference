
ShowMe
======

Video Conferencing Project in Java with JMF.


This is a video conferencing project.It is not fully completed yet. 
By this anyone can have a video chat in a same network, can have text chat in any network , also can send file.

#################################################################################
FEATURE
#################################################################################

1.Multi Chat(Used Threadpole)
2.P2P Chat
3.P2P Audio Chat
4.P2P Video Chat
5.Completely Automated
6.H263 compression Video
7.raw audio

Descriptive feature:

i. Text chat, File transfer are done through server, where server is the middle-man or a relay. Meaning, all of the data are goes through the server. So if anyone has the server running on a public IP , then it can be use in any network. This feature is not true for Video chat.


PREREQUISITE:

1. JUST INSTALL jmf-2.1.1 e
2. Require Java SDK installed.


Compatibility:

IDE:
Tested with JCreator Pro.
Tested with Eclipse.


COMPONENT:

1. A Relay Server.
2. A Client. 


1. Relay Server:
----------------

i.   ClientListener.java
ii.  ClientMap.java
iii. Clients.java
iv.  Main.java
v.   MessageListener.java
vi.  ServerConstant.java
vii. ServerMonitor.java
viii.ServerStatusListener.java


2. Client:
----------

i.   AVReceive2.java
ii.  AVTransmit2.java
iii. ClientConstant.java
iv.  ClientListListener.java
v.   ClientListPanel.java
vi.  ClientManager.java
vii. ClientStatusListener.java
viii.ClientWindowListener.java
ix.  FileReceiver.java
x.   FileSender.java
xi.  LoginFrame.java
xii. LogInPanel.java
xiii.Main.java
xiv. MessageRecever.java
xv.  MessagingFrame.java
xvi. VideoTransmit.java


Configure:
----------

To run this project you must have 3 different PC. 
ex: 
1. Server PC
2. Client 1 PC
3. Client 2 PC

You also need to install jmf-2.1.1 e in every PC. After that installation you must check your webcam with JMF application available on your PC. If the webcam is accessible through your JMF app then this project will certainly work.

After that you need to build the Server and Client code with the correct IP and Port in file ServerConstant.java and ClientConstant.java. You can build this project with any JAVA IDE. But I have encouraged to use JCreator Pro.

For Server settings in file ServerConstant.java -

```
public interface ServerConstant
{
    public static final int SERVER_PORT=12345;
    public static final int BACKLOG=100;
    public static final int CLIENT_NUMBER=100;
    public static final int MULTICAST_SENDING_PORT=5555;
    public static final int MULTICAST_LISTENING_PORT=5554;
    public static final String MULTICAST_ADDRESS="239.0.0.1";
    public static final String DISCONNECT_STRING="DISCONNECT";
    public static final String MESSAGE_SEPARATOR=" >> ";
    
}
```

SERVER_PORT=12345 is the port in which server is listening/waitting for client.


For Server settings in file ClientConstant.java -
```
public interface ClientConstant
{
    public static final String SERVER_ADDRESS="172.17.0.32";
    public static final int SERVER_PORT=12345;
    public static final int CLIENT_NUMBER=100;
    public static final String DISCONNECT_STRING="DISCONNECT";
    public static final String MESSAGE_SEPARATOR=" >> ";
}
```

SERVER_ADDRESS="172.17.0.32" is the address of the server PC and SERVER_PORT=12345 is the server listening port.

Try Out:
--------

After all of the installation, settings and build just RUN the server on a PC by server Jar or from your IDE run. Then from Server GUI just click on *Start* button.

Now from another 2 PC use the client jar or run from IDE and provide Username on the User window and click *Signin*.
Now you are done. Now any user login in your server can see the other user who are logged in to your server.

Then from friendlist click on the friend and a new window will arise for chat. On that window there will be a video chat start button and a text chat send button. You can use any one for you specific cause.



Known Issue:

1. We don't have SIP.
2. We don't have STUN, TURN.
3. When in a video call you can't have a text chat session.
4. We don't have any authentication mechanism for user sign in.
5. File transfer is disabled.




