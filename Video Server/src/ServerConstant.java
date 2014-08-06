/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MessengSrserver;


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
