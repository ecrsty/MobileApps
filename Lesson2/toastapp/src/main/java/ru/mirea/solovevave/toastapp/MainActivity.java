package ru.mirea.solovevave.toastapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickCountButton(View view){
        EditText userText = findViewById(R.id.userText);
        int cnt = userText.getText().toString().length();
        String toastText = "СТУДЕНТ №24 ГРУППА БСБО-04-22 Количество символов " + cnt;
        Toast toast = Toast.makeText(getApplicationContext(),
                toastText,
                Toast.LENGTH_SHORT);
        toast.show();
    }
}