package ru.mirea.solovevave.mireaproject;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker {
    static final String TAG = "MyWorker";

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: start");
        int network_speed = 0;
        String message = "";
        try {
            network_speed = getNetworkSpeed();
        } catch (InterruptedException e) {
            Log.d(TAG, "Не удалось получить скорость интернета");
            throw new RuntimeException(e);
        }
        // Проверяем доступность интернета и его скорость
        if (network_speed > 5) {
            message = "Интернет доступен и имеет хорошую скорость: "+network_speed+" Мбит/с";
            Log.d(TAG, message);
        } else {
            message = "Интернет имеет слабую скорость или недоступен: " + network_speed + " Мбит/с";
            Log.d(TAG, message);
        }

        getApplicationContext().getSharedPreferences("SpeedTest", Context.MODE_PRIVATE)
                .edit()
                .putString("Speed", message)
                .apply();

        Log.d(TAG, "doWork: end");

        return Result.success();
    }

    private int getNetworkSpeed() throws InterruptedException {
        TimeUnit.SECONDS.sleep(10);
        // Для примера просто вернем число в диапазоне от 1 до 30
        return new Random().nextInt(30) + 1;
    }
}
