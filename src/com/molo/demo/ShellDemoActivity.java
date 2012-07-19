package com.molo.demo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.molo.remote.Constant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;

public class ShellDemoActivity extends Activity
{
    public Process p = null;

    private OutputStream ops;

    private Gallery gallery;

    private Button button5;

    private Integer[] mImageIds =
    { R.drawable.photo1, R.drawable.photo2, R.drawable.photo3, R.drawable.photo4, R.drawable.photo5, R.drawable.photo6, };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // String cmd = "/system/bin/su -c /system/bin/sh -c /data/local/r.sh 6688";
        // exeShell(cmd);
        initRemoteServer();
//        try
//        {
//            InputStream inputStream = this.getAssets().open("remotecontrol.jar");
//
//            File file = new File("/data/data/com.molo.demo/remotecontrol.jar");
//            OutputStream out = new FileOutputStream(file);
//
//            int read = 0;
//            byte[] bytes = new byte[256];
//
//            while ((read = inputStream.read(bytes)) != -1)
//            {
//                out.write(bytes, 0, read);
//            }
//
//            inputStream.close();
//            out.flush();
//            out.close();
//        }
//
//        catch (Exception e1)
//        {
//            e1.printStackTrace();
//        }

        Button button = (Button) findViewById(R.id.button1);
        button.setText("常用按键模拟");
        button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Dialog dialog = new AlertDialog.Builder(ShellDemoActivity.this).setTitle("常用按键模拟").setIcon(android.R.drawable.btn_star)
                        .setSingleChoiceItems(new String[]
                        { "Home", "Back", "音量+", "音量-", "Menu" }, 0, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                String command = null;
                                switch (which)
                                {
                                    case 0:
                                        command = sendKeyActionEvent(KeyEvent.KEYCODE_HOME);
                                        break;
                                    case 1:
                                        command = sendKeyActionEvent(KeyEvent.KEYCODE_BACK);
                                        break;
                                    case 2:
                                        command = sendKeyActionEvent(KeyEvent.KEYCODE_VOLUME_UP);
                                        break;
                                    case 3:
                                        command = sendKeyActionEvent(KeyEvent.KEYCODE_VOLUME_DOWN);
                                        break;
                                    case 4:
                                        command = sendKeyActionEvent(KeyEvent.KEYCODE_MENU);
                                        break;
                                }
                                try
                                {
                                    Thread.sleep(1000);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                List<String> commands = new ArrayList<String>();
                                commands.add(command);
                                sendEvent(commands);
                            }
                        }).show();
                // System.out.println("Start power off .......");
                // new Thread(new Runnable()
                // {
                //
                // @Override
                // public void run()
                // {
                // try
                // {
                // Thread.sleep(1000);
                // }
                // catch (InterruptedException e)
                // {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // System.out.println("try to exe shell");
                // exeShell("/dev/remotecontrol.sh");
                // }
                // }).start();
            }
        });

        Button leftArrow = (Button) findViewById(R.id.button2);
        Button rightArrow = (Button) findViewById(R.id.button3);
        leftArrow.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                int[] location = new int[2];
                int x, y;
                if (gallery != null)
                {
                    gallery.getLocationInWindow(location);
                    x = location[0];
                    y = location[1];
                    Log.d("demo", "Windowx--->" + x + "  " + "Windowy--->" + y);
                    Log.d("demo", "gallery width--->" + gallery.getWidth() + "  " + "gallery height--->" + gallery.getHeight());
                    int width = getWindowManager().getDefaultDisplay().getWidth();
                    int height = getWindowManager().getDefaultDisplay().getHeight();
                    Log.d("demo", "window width--->" + width + "  " + "window height--->" + height);

                    float startX = width - 30;
                    float startY = y + (gallery.getHeight() / 2);

                    float endX = x + 30;
                    float endY = startY;
                    String command = sendMoveActionEvent(startX, startY, endX, endY);
                    List<String> commands = new ArrayList<String>();
                    commands.add(command);
                    sendEvent(commands);
                }
            }
        });

        rightArrow.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int[] location = new int[2];
                int x, y;
                if (gallery != null)
                {
                    gallery.getLocationInWindow(location);
                    x = location[0];
                    y = location[1];
                    Log.d("demo", "Windowx--->" + x + "  " + "Windowy--->" + y);
                    Log.d("demo", "gallery width--->" + gallery.getWidth() + "  " + "gallery height--->" + gallery.getHeight());
                    int width = getWindowManager().getDefaultDisplay().getWidth();
                    int height = getWindowManager().getDefaultDisplay().getHeight();
                    Log.d("demo", "window width--->" + width + "  " + "window height--->" + height);

                    float startX = x + 30;
                    float startY = y + (gallery.getHeight() / 2);

                    float endX = width - 30;
                    float endY = startY;
                    String command = sendMoveActionEvent(startX, startY, endX, endY);
                    List<String> commands = new ArrayList<String>();
                    commands.add(command);
                    sendEvent(commands);
                }
            }
        });

        gallery = (Gallery) findViewById(R.id.gallery1);
        gallery.setAdapter(new ImageAdapter());
        gallery.setSpacing(5);

        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                Dialog dialog = new AlertDialog.Builder(ShellDemoActivity.this).setTitle("”按我“按钮事件选择").setIcon(android.R.drawable.btn_star)
                        .setSingleChoiceItems(new String[]
                        { "普通Click事件", "长按LongClick事件" }, 0, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                String command = null;
                                int[] location = new int[2];
                                int x, y;
                                switch (which)
                                {
                                    case 0:
                                        if (button5 != null)
                                        {
                                            button5.getLocationInWindow(location);
                                            x = location[0];
                                            y = location[1];

                                            float clickX = x + 10;
                                            float clickY = y + 10;
                                            command = sendTouchActionEvent(clickX, clickY);
                                        }
                                        break;
                                    case 1:
                                        if (button5 != null)
                                        {
                                            button5.getLocationInWindow(location);
                                            x = location[0];
                                            y = location[1];

                                            float clickX = x + 10;
                                            float clickY = y + 10;
                                            command = sendTouchPressActionEvent(clickX, clickY, 1500);
                                        }
                                        break;
                                }
                                try
                                {
                                    Thread.sleep(1000);
                                }
                                catch (InterruptedException e)
                                {
                                    e.printStackTrace();
                                }
                                List<String> commands = new ArrayList<String>();
                                commands.add(command);
                                sendEvent(commands);
                            }
                        }).show();

            }
        });

        button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(ShellDemoActivity.this).setTitle("onClick").show();
            }
        });

        button5.setOnLongClickListener(new OnLongClickListener()
        {

            @Override
            public boolean onLongClick(View v)
            {
                new AlertDialog.Builder(ShellDemoActivity.this).setTitle("onLongClick").show();
                return true;
            }
        });

    }

    private void initRemoteServer()
    {
        InputStream inputStream = null;
        File file = null;
        try
        {
            inputStream = this.getAssets().open("remotecontrol.jar");
            file = new File("/data/data/com.molo.demo/remotecontrol.jar");
            if (!file.exists())
            {
                OutputStream out = new FileOutputStream(file);

                int read = 0;
                byte[] bytes = new byte[256];

                while ((read = inputStream.read(bytes)) != -1)
                {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();
            }

            inputStream = this.getAssets().open("remotecontrol.sh");
            file = new File("/data/data/com.molo.demo/remotecontrol.sh");

            if (!file.exists())
            {
                OutputStream out = new FileOutputStream(file);

                int read = 0;
                byte[] bytes = new byte[256];

                while ((read = inputStream.read(bytes)) != -1)
                {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();
            }

        }

        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
                if (file != null)
                {
                    file = null;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    // send command(json) to the socketserver
    public synchronized void sendEvent(List<String> json_commands)
    {
        DataOutputStream dos = null;
        Socket s = null;
        try
        {
            s = new Socket("localhost", 6000);
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

    public String sendKeyActionEvent(int keycode)
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

    public String sendTouchActionEvent(float x, float y)
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

    public String sendMoveActionEvent(float x, float y, float x2, float y2)
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

    public String sendKeyPressActionEvent(int keycode, int millsecond)
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

    public String sendTouchPressActionEvent(float x, float y, int millsecond)
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

    public void exeShell(String cmd)
    {
        try
        {
            p = Runtime.getRuntime().exec(cmd);

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = null;
            // Log.info("exeShell");
            while ((line = in.readLine()) != null)
            {
                Log.i("exeShell", line);
                // System.out.println(line);
            }
            // ops = p.getOutputStream();
            in.close();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        menu.add("菜单项一");
        menu.add("菜单项二");
        menu.add("菜单项三");
        return true;
    }

    public class ImageAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return Integer.MAX_VALUE;// mImageIds.length;
        }

        @Override
        public Object getItem(int position)
        {
            return position;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView i = new ImageView(ShellDemoActivity.this);

            i.setImageResource(mImageIds[position % mImageIds.length]);
            i.setScaleType(ImageView.ScaleType.FIT_XY);
            i.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.WRAP_CONTENT, Gallery.LayoutParams.WRAP_CONTENT));

            return i;
        }

    }
}