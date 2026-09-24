package com.example.campanhaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RespostaActivity : AppCompatActivity() {

    private lateinit var etIntencao: EditText

    private lateinit var btIntencao: Button

    private lateinit var btVoltarInt: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resposta)

        etIntencao = findViewById(R.id.etIntencao)

        btIntencao = findViewById(R.id.btIntencao)

        btVoltarInt = findViewById(R.id.btVoltarInt)

        btIntencao.setOnClickListener {
            val intent = Intent(this, CandidatosActivity::class.java)
            startActivity(intent)
        }

        btVoltarInt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}