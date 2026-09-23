package com.example.campanhaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DadosActivity : AppCompatActivity() {

    private lateinit var etNomeDados: EditText
    private lateinit var etCelularDados: EditText

    private lateinit var btConfirmarDados: Button
    private lateinit var btFinalizar: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados)

        etNomeDados = findViewById(R.id.etNomeDados)
        etCelularDados = findViewById(R.id.etCelularDados)

        btConfirmarDados = findViewById(R.id.btConfirmarDados)
        btFinalizar = findViewById(R.id.btFinalizar)


        btFinalizar.setOnClickListener {
            val intent = Intent(this, RespostaActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}