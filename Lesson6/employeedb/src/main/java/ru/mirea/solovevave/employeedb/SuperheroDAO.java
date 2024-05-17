package ru.mirea.solovevave.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SuperheroDAO {
    @Query("SELECT * FROM Superhero")
    List<Superhero> getAll();
    @Query("SELECT * FROM Superhero WHERE id = :id")
    Superhero getById(long id);
    @Insert
    void insert(Superhero superhero);
    @Update
    void update(Superhero superhero);
    @Delete
    void delete(Superhero superhero);
}
