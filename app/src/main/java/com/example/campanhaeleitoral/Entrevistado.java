package com.example.campanhaeleitoral;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "entrevistado")
public class Entrevistado {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nome")
    public String nome = "";

    @ColumnInfo(name = "telefone")
    public String telefone = "";

    @ColumnInfo(name = "voto")
    public String voto = "";

    @ColumnInfo(name = "problema")
    public String problema = "";

    @ColumnInfo(name = "intencao")
    public String intencao = "";

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

}




