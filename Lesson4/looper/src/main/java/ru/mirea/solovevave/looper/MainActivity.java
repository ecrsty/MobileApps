package ru.mirea.solovevave.looper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import ru.mirea.solovevave.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Handler mainThreadHandler;
    MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textView.setText("Номер по списку №24");
        mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d("MainActivity", "Task execute. This is result: " +
                        msg.getData().getString("result"));
            }
        };
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        binding.button.setOnClickListener(v -> OnClickBtn(v));
    }

    public void OnClickBtn(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int age = Integer.parseInt(binding.ageText.getText().toString());
                    String profession = binding.profession.getText().toString();

                    Thread.sleep(age * 1000L);
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("result", "Возраст: " + age + ", профессия: " + profession);
                    msg.setData(bundle);
                    mainThreadHandler.sendMessage(msg);
                    binding.textView.setText("Данные обработаны");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("MainActivity", "Ошибка обработки");
                    binding.textView.setText("Необходимо заполнить оба поля");
                }
            }
        }).start();
    }
}
