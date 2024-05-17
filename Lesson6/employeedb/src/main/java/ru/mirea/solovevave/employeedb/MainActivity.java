package ru.mirea.solovevave.employeedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppDatabase db = App.getInstance().getDatabase();
        SuperheroDAO superheroDAO = db.employeeDAO();

        Superhero spiderman = new Superhero(1, "Spiderman", "cobweb");
        Superhero batman = new Superhero(1, "Batman", "dexterity");


        superheroDAO.insert(spiderman);
        superheroDAO.insert(batman);

        List<Superhero> superheroes = superheroDAO.getAll();

        batman = superheroDAO.getById(2);
        batman.superpower = "stealth";
        superheroDAO.update(batman);
        Log.d("MainActivity", batman.name + " " + batman.superpower);
    }
}