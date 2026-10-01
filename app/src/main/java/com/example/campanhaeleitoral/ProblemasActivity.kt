package com.example.campanhaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProblemasActivity : AppCompatActivity() {

    private lateinit var cbSaude: CheckBox
    private lateinit var cbEducacao: CheckBox
    private lateinit var cbSeguranca: CheckBox
    private lateinit var cbTransporte: CheckBox
    private lateinit var cbDesemprego: CheckBox
    private lateinit var cbMoradia: CheckBox
    private lateinit var cbSaneamento: CheckBox
    private lateinit var cbMeioAmbiente: CheckBox
    private lateinit var cbInfraestrutura: CheckBox
    private lateinit var cbOutros: CheckBox

    private lateinit var etProblemas: EditText
    private lateinit var btConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_problemas)

        // Recebe os dados da tela anterior
        val intencaoRecebida =
            intent.getStringExtra("intencao") ?: ""

        val candidatoRecebido =
            intent.getStringExtra("candidato") ?: ""

        // Inicializa os CheckBoxes
        cbSaude = findViewById(R.id.cbSaude)
        cbEducacao = findViewById(R.id.cbEducacao)
        cbSeguranca = findViewById(R.id.cbSeguranca)
        cbTransporte = findViewById(R.id.cbTransporte)
        cbDesemprego = findViewById(R.id.cbDesemprego)
        cbMoradia = findViewById(R.id.cbMoradia)
        cbSaneamento = findViewById(R.id.cbSaneamento)
        cbMeioAmbiente = findViewById(R.id.cbMeioAmbiente)
        cbInfraestrutura = findViewById(R.id.cbInfraestrutura)
        cbOutros = findViewById(R.id.cbOutros)

        etProblemas = findViewById(R.id.etProblemas)
        btConfirmar = findViewById(R.id.btConfirmar)

        btConfirmar.setOnClickListener {

            // Lista todos os CheckBoxes
            val checkBoxes = listOf(
                cbSaude,
                cbEducacao,
                cbSeguranca,
                cbTransporte,
                cbDesemprego,
                cbMoradia,
                cbSaneamento,
                cbMeioAmbiente,
                cbInfraestrutura,
                cbOutros
            )

            // Conta os CheckBoxes marcados
            val quantidadeMarcada = checkBoxes.count {
                it.isChecked
            }

            // Verifica o limite de 3 opções
            if (quantidadeMarcada > 3) {
                Toast.makeText(
                    this,
                    "Selecione no máximo 3 problemas!",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Verifica se pelo menos uma opção foi marcada
            if (quantidadeMarcada == 0) {
                Toast.makeText(
                    this,
                    "Selecione pelo menos um problema!",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            // Valida o campo Outros
            val outroProblema =
                etProblemas.text.toString().trim()

            if (cbOutros.isChecked && outroProblema.isEmpty()) {
                etProblemas.error = "Descreva o problema"
                return@setOnClickListener
            }

            // Cria a lista dos problemas selecionados
            val problemasSelecionados = mutableListOf<String>()

            if (cbSaude.isChecked)
                problemasSelecionados.add("Saúde")

            if (cbEducacao.isChecked)
                problemasSelecionados.add("Educação")

            if (cbSeguranca.isChecked)
                problemasSelecionados.add("Segurança")

            if (cbTransporte.isChecked)
                problemasSelecionados.add("Transporte")

            if (cbDesemprego.isChecked)
                problemasSelecionados.add("Desemprego")

            if (cbMoradia.isChecked)
                problemasSelecionados.add("Moradia")

            if (cbSaneamento.isChecked)
                problemasSelecionados.add("Saneamento básico")

            if (cbMeioAmbiente.isChecked)
                problemasSelecionados.add("Meio ambiente")

            if (cbInfraestrutura.isChecked)
                problemasSelecionados.add("Infraestrutura")

            if (cbOutros.isChecked)
                problemasSelecionados.add("Outros: $outroProblema")

            // Cria o Intent para DadosActivity
            val proximaTela = Intent(
                this,
                DadosActivity::class.java
            )

            // Encaminha a intenção
            proximaTela.putExtra(
                "intencao",
                intencaoRecebida
            )

            // Encaminha o candidato
            proximaTela.putExtra(
                "candidato",
                candidatoRecebido
            )

            // Encaminha os problemas selecionados
            proximaTela.putStringArrayListExtra(
                "problemas",
                ArrayList(problemasSelecionados)
            )

            startActivity(proximaTela)
        }

        // Configuração das margens da tela
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }
}
