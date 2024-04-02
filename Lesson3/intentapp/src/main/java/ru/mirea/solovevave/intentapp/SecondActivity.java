package ru.mirea.solovevave.intentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        String currTime = (String) getIntent().getSerializableExtra("currTime");
        TextView textFromSecActivity = findViewById(R.id.textFromMain);
        String str = "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ " + "СОСТАВЛЯЕТ "
                +  Math.round(Math.pow(24, 2)) + ", а текущее время " + currTime;
        textFromSecActivity.setText(str);
    }
}