/**
 *
 * SocketHandler.java
 *
 */
package com.molo.remote;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Kane.G (gongchen.cn@gmail.com)
 * @date 2012-7-12
 */
public class SocketHandler implements Runnable
{
    private Socket socket;

    public SocketHandler(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        DataInputStream dis = null;
        try
        {
            System.out.println("连接成功 :" + this.hashCode());
            dis = new DataInputStream(socket.getInputStream());

            while (dis.readUTF() != null)
            {
                String command = dis.readUTF();
                Queue.getInstance().push(command);
            }
            dis.close();
            socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (dis != null)
                {
                    dis.close();
                    dis = null;
                }
                if (socket != null && !socket.isClosed())
                {
                    socket.close();
                    socket = null;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
