/**
 *
 * EventDispatcher.java
 *
 */
package com.molo.remote;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * @author Kane.G (gongchen.cn@gmail.com)
 * @date 2012-7-12
 */
public class EventDispatcher implements Runnable
{
    private static Instrumentation inst = new Instrumentation();

    public EventDispatcher()
    {
        
    }

    // {
    // "type" : “key|touch|move”, // 事件类型
    // "x" : "100.11", // x坐标
    // "y" : "100.22", // y坐标
    // "x2" : "100.11", // x2坐标
    // "y2" : "100.22", // y2坐标
    // "action" : 0, // ACTION_DOWN ...
    // "keycode" :1,// KeyEvent.KEYCODE_0 int
    // "millsecond":3000 //按键时间 int
    // "note" : "保留字段"
    // }

    private void dispatch()
    {
        while (true)
        {
            String json_command = Queue.getInstance().poll();
            if (json_command != null)
            {
                System.out.println("Command is :" + json_command);
                try
                {
                    if (json_command.startsWith("stop")) // stop dispatch event
                    {
                        break;
                    }
                    
                    JSONObject demoJson = new JSONObject(json_command);
                    String type = demoJson.getString("type");
                    System.out.println("Type is :" + type);
                    if (Constant.TYPE_KEY_ACTION.equalsIgnoreCase(type))
                    {
                        int keyCode = demoJson.getInt("keycode");
                        simulateKeystroke(keyCode);
                    }
                    else if (Constant.TYPE_TOUCH_ACTION.equalsIgnoreCase(type))
                    {
                        // int action = demoJson.getInt("action");
                        String x = demoJson.getString("x");
                        String y = demoJson.getString("y");

                        MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN, Float.parseFloat(x), Float.parseFloat(y), 0);
                        simulateMotionstroke(event);
                        event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
                            Float.parseFloat(x), Float.parseFloat(y), 0);
                        simulateMotionstroke(event);
                    }
                    else if (Constant.TYPE_MOVE_ACTION.equalsIgnoreCase(type))
                    {
                        String x = demoJson.getString("x");
                        String y = demoJson.getString("y");
                        String x2 = demoJson.getString("x2");
                        String y2 = demoJson.getString("y2");

                        MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN, Float.parseFloat(x), Float.parseFloat(y), 0);
                        simulateMotionstroke(event);
                        event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE,
                            Float.parseFloat(x), Float.parseFloat(y), 0);
                        simulateMotionstroke(event);
                        event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE,
                            Float.parseFloat(x), Float.parseFloat(y), 0);
                        simulateMotionstroke(event);
                        event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE,
                            Float.parseFloat(x2), Float.parseFloat(y2), 0);
                        simulateMotionstroke(event);
                        event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE,
                            Float.parseFloat(x2), Float.parseFloat(y2), 0);
                        simulateMotionstroke(event);
                        event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
                            Float.parseFloat(x2), Float.parseFloat(y2), 0);
                        simulateMotionstroke(event);
                    }
                    else if (Constant.TYPE_KEY_PRESS_ACTION.equalsIgnoreCase(type))
                    {
                        int keyCode = demoJson.getInt("keycode");
                        int millsecond = demoJson.getInt("millsecond");
                        KeyEvent k = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                        simulateKeySync(k);
                        try
                        {
                            Thread.sleep(millsecond);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        k = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                        simulateKeySync(k);
                    }
                    else if (Constant.TYPE_TOUCH_PRESS_ACTION.equalsIgnoreCase(type))
                    {
                        String x = demoJson.getString("x");
                        String y = demoJson.getString("y");
                        int millsecond = demoJson.getInt("millsecond");
                        MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                            MotionEvent.ACTION_DOWN, Float.parseFloat(x), Float.parseFloat(y), 0);
                        simulateMotionstroke(event);
                        try
                        {
                            Thread.sleep(millsecond);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP,
                            Float.parseFloat(x), Float.parseFloat(y), 0);
                        simulateMotionstroke(event);
                    }
//                    else if (json_command.equalsIgnoreCase("stop")) // stop dispatch event
//                    {
//                        break;
//                    }
                }
                catch (JSONException e1)
                {
                    e1.printStackTrace();
                }
            }
            // wait 500ms to get next command
            try
            {
                Thread.sleep(500);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        System.out.println("Stop Dispatch....");
    }

    @Override
    public void run()
    {
        dispatch();
    }

    public static void simulateKeystroke(final int KeyCode)
    {
        try
        {
            inst.sendKeyDownUpSync(KeyCode);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void simulateKeySync(final KeyEvent event)
    {
        try
        {
            inst.sendKeySync(event);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void simulateMotionstroke(final MotionEvent event)
    {
        try
        {
            inst.sendPointerSync(event);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void simulateTrackballstroke(final MotionEvent event)
    {
        try
        {
            inst.sendTrackballEventSync(event);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
