/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;

/**
 *
 * @author  sharma
 */
public interface ClientListListener
{
    void addToList(String userName);
    void removeFromList(String userName);
}
