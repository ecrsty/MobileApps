package ru.mirea.solovevave.simplefragmentapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Fragment fragment1, fragment2;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragment1 = new FirstFragment();
        fragment2 = new SecondFragment();
    }

    public void onClick(View view){
        fragmentManager = getSupportFragmentManager();
        int btnID = view.getId();
        if (btnID == R.id.btnFirstFragment) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment1).
                    commit();
        } else if (btnID == R.id.btnSecondFragment) {
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment2).
                    commit();
        }
    }
}