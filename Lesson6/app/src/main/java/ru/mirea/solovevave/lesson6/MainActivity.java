package ru.mirea.solovevave.lesson6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ru.mirea.solovevave.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final String SP_NAME = "MySharedPreferences";
    private static final String KEY_GROUP = "group";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_FILM = "film";
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    EditText group;
    EditText number;
    EditText film;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sp = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        group = binding.group;
        number = binding.number;
        film = binding.film;

        binding.saveBtn.setOnClickListener(v -> onSaveBtnClick(v));
    }

    @Override
    protected void onStart() {
        super.onStart();
        final SharedPreferences settings = getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        group.setText(sp.getString(KEY_GROUP, ""));
        number.setText(String.valueOf(sp.getInt(KEY_NUMBER, 0)));
        film.setText(settings.getString(KEY_FILM, ""));
    }

    public void onSaveBtnClick(View view) {
        editor.putString(KEY_GROUP, group.getText().toString());
        editor.putInt(KEY_NUMBER, Integer.parseInt(number.getText().toString()));
        editor.putString(KEY_FILM, film.getText().toString());

        editor.apply();

        Toast.makeText(this, "Данные сохранены!", Toast.LENGTH_SHORT).show();
    }
}