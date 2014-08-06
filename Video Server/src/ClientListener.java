/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package MessengSrserver;


public interface ClientListener
{
    void signIn(String userName);
    void signOut(String userNamme);
    void clientStatus(String status);
    void mapped(String nam,String ip);
}
