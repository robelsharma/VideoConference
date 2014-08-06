/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author team3
 */
public class FileReceiver implements Runnable
{
    String address;
    FileReceiver(String getAddress)
    {

        address=getAddress.replace('/',' ').trim();
        System.out.println(address);
    }

    public void run()
    {
        try {
            int filesize = 6022386; // filesize temporary hardcoded
            long start = System.currentTimeMillis();
            int bytesRead;
            int current = 0;
            // localhost for testing
            Socket sock = new Socket(address,13267);
            System.out.println("Connecting...");
            // receive file
            byte[] mybytearray = new byte[filesize];
            InputStream is = sock.getInputStream();
            FileOutputStream fos = new FileOutputStream("receiveHasib.txt");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray, 0, mybytearray.length);
            current = bytesRead;
            // thanks to A. Cádiz for the bug fix
            do {
                bytesRead = is.read(mybytearray, current, mybytearray.length - current);
                if (bytesRead >= 0) {
                    current += bytesRead;
                }
            } while (bytesRead > -1);
            bos.write(mybytearray, 0, current);
            bos.flush();
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            bos.close();
            sock.close();
        } catch (UnknownHostException ex) {
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
