package ru.mirea.solovevave.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class MyLooper extends Thread{
    public Handler mHandler;
    private Handler mainHandler;
    public MyLooper(Handler mainThreaHandler){
        mainHandler = mainThreaHandler;
    }


    public void run(){
        Log.d("MyLooper", "run");
        Looper.prepare();
        mHandler = new Handler(Looper.myLooper()){
            public void HandleMessage(Message msg){
                String data = msg.getData().getString("KEY");
                Log.d("LooperMSG","MyLooper get message: " + data);

                int count = data.length();
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("result", String.format("The number of letters in the word %s is %d", data, count));
                message.setData(bundle);
                mainHandler.sendMessage(message);
            }
        };
        Looper.loop();
    }
}