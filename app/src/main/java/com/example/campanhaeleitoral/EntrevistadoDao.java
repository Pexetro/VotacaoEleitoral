package com.example.campanhaeleitoral;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EntrevistadoDao {

        @Query("SELECT * FROM entrevistado")
        List<Entrevistado> listar();

        @Insert
        void insertall(Entrevistado...entevistados);

        @Delete
       void delete(Entrevistado entrevistado);

}

