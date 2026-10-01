package com.example.campanhaeleitoral;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface EntrevistadoDao {

        @Query("SELECT * FROM entrevistado ORDER BY data_hora DESC")
        List<Entrevistado> listar();

        @Insert
        void insertall(Entrevistado...entevistados);

        @Query("SELECT COUNT(id) FROM entrevistado")
        int contarEntrevistados();

        @Query("SELECT voto AS candidato, COUNT(id) AS quantidadeVotos FROM entrevistado GROUP BY voto")
        List<VotoCandidato> obterResultadoPesquisa();



}

