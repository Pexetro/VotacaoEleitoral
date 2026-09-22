package com.example.campanhaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CandidatosActivity : AppCompatActivity() {

    private lateinit var btCandidato1 : Button
    private lateinit var btCandidato2 : Button
    private lateinit var btCandidato3 : Button
    private lateinit var btCandidato4 : Button
    private lateinit var btCandidato5 : Button
    private lateinit var btVotar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_candidatos)

        btCandidato1 = findViewById(R.id.btCandidato1)
        btCandidato2 = findViewById(R.id.btCandidato2)
        btCandidato3 = findViewById(R.id.btCandidato3)
        btCandidato4 = findViewById(R.id.btCandidato4)
        btCandidato5 = findViewById(R.id.btCandidato5)
        btVotar = findViewById(R.id.btVotar)

        btVotar.setOnClickListener {
            val intent = Intent(this, ProblemasActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}