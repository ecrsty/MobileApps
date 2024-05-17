package ru.mirea.solovevave.employeedb;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Superhero {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String superpower;

    public Superhero(long id, String name, String superpower) {
        this.id = id;
        this.name = name;
        this.superpower = superpower;
    }
}
