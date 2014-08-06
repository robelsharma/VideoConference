/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;

/**
 *
 * @author team3
 */
public interface ClientWindowListener
{
    public void openWindow(String message);
    public void closeWindow(String message);
    public void fileStatus(String filesStatus);
}
