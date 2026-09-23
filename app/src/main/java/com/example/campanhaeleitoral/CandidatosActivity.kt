package com.example.campanhaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CandidatosActivity : AppCompatActivity() {

    private lateinit var rbCandidato1 : RadioButton
    private lateinit var rbCandidato2 : RadioButton
    private lateinit var rbCandidato3 : RadioButton
    private lateinit var rbCandidato4 : RadioButton
    private lateinit var rbCandidato5 : RadioButton
    private lateinit var rbCandidato6 : RadioButton
    private lateinit var rgCandidatos : RadioGroup
    private lateinit var btVotarCandidatos : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_candidatos)

        rbCandidato1 = findViewById(R.id.rbCandidato1)
        rbCandidato2 = findViewById(R.id.rbCandidato2)
        rbCandidato3 = findViewById(R.id.rbCandidato3)
        rbCandidato4 = findViewById(R.id.rbCandidato4)
        rbCandidato5 = findViewById(R.id.rbCandidato5)
        rbCandidato6 = findViewById(R.id.rbCandidato6)
        btVotarCandidatos = findViewById(R.id.btVotarCandidatos)

        btVotarCandidatos.setOnClickListener {
            val idSelecionado = rgCandidatos.checkedRadioButtonId

            if (idSelecionado != -1) {
                val radioButton = findViewById<RadioButton>(idSelecionado)
                val textoEscolhido = radioButton.text.toString()
                Toast.makeText(this, "Escolha: $textoEscolhido", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProblemasActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Nenhuma opção escolhida", Toast.LENGTH_SHORT).show()
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}