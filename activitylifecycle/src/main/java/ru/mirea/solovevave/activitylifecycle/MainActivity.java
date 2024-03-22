package ru.mirea.solovevave.activitylifecycle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private void showLog(String nameLog){
        EditText editText = findViewById(R.id.editText);
        editText.setText(nameLog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String name = "onCreate()";
        setContentView(R.layout.activity_main);
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String name = "onStart()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String name = "onRestoreInstanceState()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String name = "onPostCreate()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String name = "onResume()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        String name = "onPostResume()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        String name = "onAttachedToWindow()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String name = "onPause()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String name = "onSaveInstanceState()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onStop() {
        super.onStop();
        String name = "onStop()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String name = "onDestroy()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        String name = "onDetachedFromWindow()";
        Log.i(TAG, name);
        showLog(name);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String name = "onRestart()";
        Log.i(TAG, name);
        showLog(name);
    }
}