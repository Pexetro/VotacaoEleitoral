package com.example.campanhaeleitoral;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Entrevistado.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract EntrevistadoDao entrevistadoDao();
}
