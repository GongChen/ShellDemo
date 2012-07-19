/**
 *
 * Queue.java
 *
 */
package com.molo.remote;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Kane.G (gongchen.cn@gmail.com)
 * @date 2012-7-12
 */
public class Queue
{
    private ConcurrentLinkedQueue<String> m_Queue = null;

    private static Queue instance = null;

    private Queue()
    {
        m_Queue = new ConcurrentLinkedQueue<String>();
    }

    public static synchronized Queue getInstance()
    {
        if (instance == null)
        {
            instance = new Queue();
        }
        return instance;
    }
    
    public synchronized boolean push(String command)
    {
        return m_Queue.offer(command);
    }

    public synchronized String poll()
    {
        return m_Queue.poll();
    }
}
