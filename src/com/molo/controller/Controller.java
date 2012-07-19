/**
 *
 * Controller.java
 *
 */
package com.molo.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Kane.G (gongchen.cn@gmail.com)
 * @date 2012-7-18
 */
public class Controller
{
    // send command(json) to the socketserver (use default port)
    public static synchronized void sendEvent(List<String> json_commands)
    {
        sendEvent(json_commands, 6000);
    }

    // send command(json) to the socketserver
    public static synchronized void sendEvent(List<String> json_commands, int port)
    {
        DataOutputStream dos = null;
        Socket s = null;
        try
        {
            s = new Socket("localhost", port);
            // 获取输出流，用于客户端向服务器端发送数据
            dos = new DataOutputStream(s.getOutputStream());
            // 客户端向服务器端发送数据
            // for (String json_command : json_commands)
            for (int i = 0; i < json_commands.size(); i++)
            {
                // dos.writeUTF(json_command);
                String json_command = json_commands.get(i);
                System.out.println("客户端向服务器端发送数据 : " + json_command);
                
                
                dos.write((json_command + "\n").getBytes());
            }
            dos.close();
            s.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (dos != null)
            {
                try
                {
                    dos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (s != null)
            {
                try
                {
                    s.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String sendKeyActionEvent(int keycode)
    {
        JSONObject base = new JSONObject();
        try
        {
            base.put("type", Constant.TYPE_KEY_ACTION);
            base.put("keycode", keycode);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return base.toString();
    }

    public static String sendTouchActionEvent(float x, float y)
    {
        JSONObject base = new JSONObject();
        try
        {
            base.put("type", Constant.TYPE_TOUCH_ACTION);
            base.put("x", String.valueOf(x));
            base.put("y", String.valueOf(y));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return base.toString();
    }

    public static String sendMoveActionEvent(float x, float y, float x2, float y2)
    {
        JSONObject base = new JSONObject();
        try
        {
            base.put("type", Constant.TYPE_MOVE_ACTION);
            base.put("x", String.valueOf(x));
            base.put("y", String.valueOf(y));
            base.put("x2", String.valueOf(x2));
            base.put("y2", String.valueOf(y2));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return base.toString();
    }

    public static String sendKeyPressActionEvent(int keycode, int millsecond)
    {
        JSONObject base = new JSONObject();
        try
        {
            base.put("type", Constant.TYPE_KEY_PRESS_ACTION);
            base.put("keycode", keycode);
            base.put("millsecond", millsecond);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return base.toString();
    }

    public static String sendTouchPressActionEvent(float x, float y, int millsecond)
    {
        JSONObject base = new JSONObject();
        try
        {
            base.put("type", Constant.TYPE_TOUCH_PRESS_ACTION);
            base.put("x", String.valueOf(x));
            base.put("y", String.valueOf(y));
            base.put("millsecond", millsecond);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return base.toString();
    }
}
