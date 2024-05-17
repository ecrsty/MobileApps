package ru.mirea.solovevave.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import ru.mirea.solovevave.notebook.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    EditText quote;
    EditText fileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quote = binding.quote;
        fileName = binding.fileName;

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExternalStorageWritable()) {
                    writeFileToExternalStorage(fileName.getText().toString(), quote.getText().toString());
                    Toast.makeText(MainActivity.this, "Данные сохранены", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Храниоище не доступно. Данные не сохранены.", Toast.LENGTH_LONG).show();
            }
        });

        binding.loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExternalStorageReadable()) {
                    String quoteText = readFileFromExternalStorage(fileName.getText().toString());
                    quote.setText(quoteText);
                    Toast.makeText(MainActivity.this, "Данные загружены", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this, "Храниоище не доступно. Не удалось загрузить данные.", Toast.LENGTH_LONG).show();
            }
        });

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            return true;
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
            return true;
        return false;
    }

    public void writeFileToExternalStorage(String nameOfFile, String quoteText) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, nameOfFile);
        try {
            FileOutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
            outWriter.write(quoteText);
            outWriter.close();
        } catch (IOException e) {
            Log.w("ExternalStorage", "error writing " + file, e);
            throw new RuntimeException(e);
        }
    }

    public String readFileFromExternalStorage(String nameOfFile) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, nameOfFile);
        try {
            FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
            List<String> lines = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            Log.w("ExternalStorage", String.format("Read from file %s successful", lines.toString()));
            return lines.toString();
        } catch (IOException e) {
            Log.w("ExternalStorage", String.format("Read from file %s failed", e.getMessage()));
            throw new RuntimeException(e);
        }
    }
}