package ru.mirea.solovevave.internalfilestorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.solovevave.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String fileName = "russianhistoryfacts.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText inputFact = binding.inputFact;
        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileOutputStream out;
                try {
                    out = openFileOutput(fileName, Context.MODE_PRIVATE);
                    out.write(inputFact.getText().toString().getBytes());
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}