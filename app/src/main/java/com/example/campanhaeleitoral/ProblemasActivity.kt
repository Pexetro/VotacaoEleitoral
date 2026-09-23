package com.example.campanhaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProblemasActivity : AppCompatActivity() {

    private lateinit var cbSaude : CheckBox
    private lateinit var cbEducacao : CheckBox
    private lateinit var cbSeguranca : CheckBox
    private lateinit var cbTransporte : CheckBox
    private lateinit var cbDesemprego : CheckBox
    private lateinit var cbOutros : CheckBox

    private lateinit var etProblemas: EditText

    private lateinit var btConfirmar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_problemas)

        cbSaude = findViewById(R.id.cbSaude)
        cbEducacao = findViewById(R.id.cbEducacao)
        cbSeguranca = findViewById(R.id.cbSeguranca)
        cbTransporte = findViewById(R.id.cbTransporte)
        cbDesemprego = findViewById(R.id.cbDesemprego)
        cbOutros = findViewById(R.id.cbOutros)
        etProblemas = findViewById(R.id.etProblemas)
        btConfirmar = findViewById(R.id.btConfirmar)

        btConfirmar.setOnClickListener {
            val intent = Intent(this, DadosActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}