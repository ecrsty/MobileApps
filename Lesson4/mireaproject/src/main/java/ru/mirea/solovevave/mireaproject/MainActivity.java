package ru.mirea.solovevave.mireaproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WorkRequest uploadWorkRequest =
                new OneTimeWorkRequest.Builder(MyWorker.class).build();
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);
    }

    public void OnClick(View v) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build();

        WorkRequest customWorkRequest = new OneTimeWorkRequest
                .Builder(MyWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this)
                .enqueue(customWorkRequest);
    }
}