package com.example.campanhaeleitoral;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Entrevistado {
    @PrimaryKey (autoGenerate = true)
    public int id;

    @ColumnInfo (name = "nome")
    public String nome;

    @ColumnInfo (name = "telefone")
    public String telefone;
    public String voto;
    public String problema;
    public String intencao;




}
