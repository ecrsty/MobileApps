package ru.mirea.solovevave.buttonclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView tvOut;
    private Button btnWhoAmI, btnItIsNotMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvOut = findViewById(R.id.tvOut);
        btnWhoAmI = findViewById(R.id.btnWhoAmI);
        btnItIsNotMe = findViewById(R.id.btnItIsNotMe);
        CheckBox checkBox = findViewById(R.id.check_Box);

        View.OnClickListener oclBtnWhoAmI = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvOut.setText("Мой номер по списку №24");
                checkBox.setChecked(true);
            }
        };

        btnWhoAmI.setOnClickListener(oclBtnWhoAmI);
    }

    public void onMyButtonClick(View view){
        tvOut.setText("Кто тогда?");
        Toast.makeText(this, "Второй способ!", Toast.LENGTH_SHORT).show();
        CheckBox checkBox = findViewById(R.id.check_Box);
        checkBox.setChecked(true);
    }
}