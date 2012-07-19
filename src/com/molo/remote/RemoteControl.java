/**
 *
 * RemoteControl.java
 *
 */
package com.molo.remote;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Kane.G (gongchen.cn@gmail.com)
 * @date 2012-7-3
 */
public class RemoteControl
{

    private static final int PORT = 6000;

    private ServerSocket ss = null;

    private Socket s = null;

    private static Logger log = Logger.getLogger("RemoteControl");

    public static void main(String[] args)
    {
        log.setLevel(Level.INFO);
        log.info("RemoteControl::start");
        int code = new RemoteControl().init(args);
        log.info("RemoteControl::exit");
        System.exit(code);

    }

    /**
     * Run the command!
     * 
     * @param args The socket port[optional]
     * @return Returns a posix-style result code. 0 for no error.
     */
    private int init(String[] args)
    {
        try
        {
            if (ss == null || ss.isClosed())
            {
                if (args.length > 0)
                {
                    try
                    {
                        int custom_port = Integer.parseInt(args[0]);
                        if (custom_port > 1024)
                        {
                            ss = new ServerSocket(custom_port);
                        }
                    }
                    catch (NumberFormatException e1)
                    {
                        log.info("RemoteControl::NumberFormatException" + e1);
                        return -1;
                    }
                    catch (BindException e2)
                    {
                        log.info("RemoteControl::BindException" + e2);
                        return -1;
                    }
                }
                else
                {
                    ss = new ServerSocket(PORT);
                }
            }

            Thread dispatch_thread = new Thread(new EventDispatcher());
            dispatch_thread.start();

            System.out.println("等待连接 ");
            boolean isfinish = false;
            while (!isfinish)
            {
                s = ss.accept();
                System.out.println("连接成功 ");
                DataInputStream dis = new DataInputStream(s.getInputStream());
                String info = null;
                while ((info = dis.readLine()) != null)
                {
                    System.out.println("客户端发来消息： " + info);
                    if (info.equalsIgnoreCase("stop"))
                    {
                        isfinish = true;
                        Queue.getInstance().push("stop");// let dispatch know.
                    }
                    else
                    {
                        // new Thread(new SocketHandler(s)).start();
                        Queue.getInstance().push(info);
                    }
                }
                dis.close();
            }

            try
            {
                dispatch_thread.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            System.out.println("客户端已关闭");
            s.close();
            ss.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return -1;
        }
        finally
        {
            try
            {
                if (s != null)
                {
                    s.close();
                    s = null;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            try
            {
                if (ss != null)
                {
                    ss.close();
                    ss = null;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }

}
