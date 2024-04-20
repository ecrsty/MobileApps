package ru.mirea.solovevave.mireaproject;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.concurrent.TimeUnit;

public class MyWorker extends Worker{
    static final String TAG = "MyWorker";

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        Log.d(TAG, "doWork: start");

        // Проверяем доступность интернета и его скорость
        if (getNetworkSpeed() > 5) { // Проверяем, что скорость больше 5 Мбит/с
            Log.d(TAG, "Internet is available and its speed is greater than 5 Mbps");
        } else {
            Log.d(TAG, "Internet is not available or its speed is too slow");
        }

        Log.d(TAG, "doWork: end");
        return Result.success();
    }

    private int getNetworkSpeed() {
        // Для примера просто вернем число
        return 10;
    }
}
