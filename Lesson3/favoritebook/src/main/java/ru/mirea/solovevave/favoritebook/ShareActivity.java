package ru.mirea.solovevave.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView ageView = findViewById(R.id.devFavBook);
            String devFavBook = extras.getString(MainActivity.KEY);
            ageView.setText("Любимая книга разработчика – " + devFavBook);
        }
    }

    public void onClickSendData(View view){
        EditText userFavBook = findViewById(R.id.userFavBook);
        String text = userFavBook.getText().toString();
        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, text);
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}