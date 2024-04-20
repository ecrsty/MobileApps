package ru.mirea.solovevave.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import ru.mirea.solovevave.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView infoTextView = binding.textThread;
        Thread mainThread = Thread.currentThread();
        infoTextView.setText("Имя текущего потока: " + mainThread.getName());
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-04-22, НОМЕР ПО СПИСКУ: 24, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: Интерстеллар");
        infoTextView.append("\nНовое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(), "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        binding.button.setOnClickListener(v -> OnClickProcessingBtn(v));
    }

    public void OnClickProcessingBtn(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int numberThread = counter++;
                Log.d("ThreadProject", String.format("Запущен поток №%d студентом группы №%s номер по списку №%d ", numberThread, "БСБО-04-22", 24));
                String resText = "Среднее количество пар в день: ";
                try {
                    int pairCnt = Integer.parseInt(binding.pairCnt.getText().toString());
                    int daysCnt = Integer.parseInt(binding.daysCnt.getText().toString());
                    int avgPairCnt = Math.round(pairCnt / daysCnt);
                    binding.resultText.setText(resText + avgPairCnt);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    binding.resultText.setText("Заполните оба поля, чтобы узнать результат");
                }
                Log.d("ThreadProject", "Выполнен поток №" + numberThread);
            }
        }).start();
    }
}