package ru.mirea.solovevave.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.mirea.solovevave.musicplayer.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    int SongIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.author.setText("Lana Del Rey");
        binding.song.setText("Summertime Sadness");

        binding.pauseBtn.setOnClickListener(v -> {
            String toastText = "Вы нажали на паузу";
            Toast toast = Toast.makeText(getApplicationContext(),
                    toastText,
                    Toast.LENGTH_LONG);
            toast.show();
        });

        binding.nextBtn.setOnClickListener(v -> onClickSwitch(v));
        binding.previousBtn.setOnClickListener(v -> onClickSwitch(v));
    }

    public void onClickSwitch(View view){
        binding.seekBar.setProgress(0);
        binding.author.setText("Lana Del Rey");
        if (SongIndex == 1) {
            binding.song.setText("Summertime Sadness");
            SongIndex = 0;
        }
        else {
            binding.song.setText("Dark Paradise");
            SongIndex = 1;
        }
    }
}