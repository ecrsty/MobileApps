package ru.mirea.solovevave.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickShowDialog(View view){
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "mirea");
    }

    public void onOkClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Иду дальше\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onCancelClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"Нет\"!",
                Toast.LENGTH_LONG).show();
    }
    public void onNeutralClicked() {
        Toast.makeText(getApplicationContext(), "Вы выбрали кнопку \"На паузе\"!",
                Toast.LENGTH_LONG).show();
    }

    public void onClickTimeDialog(View view){
        MyTimeDialogFragment myTimeDialogFragment = new MyTimeDialogFragment();
        myTimeDialogFragment.show(getSupportFragmentManager(),"timeDialog");
    }

    public void onClickDateDialog(View view){
        MyDateDialogFragment myDateDialogFragment = new MyDateDialogFragment();
        myDateDialogFragment.show(getSupportFragmentManager(),"dateDialog");
    }

    public void onClickProgressDialog(View view){
        MyProgressDialogFragment myProgressDialogFragment = new MyProgressDialogFragment();
        myProgressDialogFragment.show(getSupportFragmentManager(),"progressDialog");
    }

    public void onClickSnackbar(View view){
        Snackbar.make( view, "Это Snackbar!", Snackbar.LENGTH_LONG).show();
    }
}