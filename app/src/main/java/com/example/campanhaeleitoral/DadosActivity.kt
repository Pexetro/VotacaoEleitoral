package com.example.campanhaeleitoral

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.room.Room

class DadosActivity : AppCompatActivity() {

    private lateinit var etNomeDados: EditText
    private lateinit var etCelularDados: EditText

    private lateinit var btConfirmarDados: Button
    private lateinit var btFinalizar: Button
    private lateinit var tvLocalizacao: TextView

    private var latitude: Double? = null
    private var longitude: Double? = null

    // Recebe os dados das telas anteriores
    private var intencaoRecebida: String = ""
    private var candidatoRecebido: String = ""
    private var problemasRecebidos: ArrayList<String> = arrayListOf()

    private val banco by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "banco_entrevistados"
        )
            .build()
    }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    // Solicita a permissão de localização
    private val solicitarPermissao =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissoes ->

            val permitida =
                permissoes[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissoes[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (permitida) {
                obterLocalizacao()
            } else {
                tvLocalizacao.text = "Permissão de localização negada"

                Toast.makeText(
                    this,
                    "Permissão de localização negada",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    // Verifica a permissão e obtém a localização
    private fun obterLocalizacao() {

        val precisa = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val aproximada = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Se não houver permissão, solicita ao usuário
        if (!precisa && !aproximada) {
            solicitarPermissao.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        }

        tvLocalizacao.text = "Obtendo localização..."

        try {
            val prioridade = if (precisa) {
                Priority.PRIORITY_HIGH_ACCURACY
            } else {
                Priority.PRIORITY_BALANCED_POWER_ACCURACY
            }

            fusedLocationClient.getCurrentLocation(
                prioridade,
                CancellationTokenSource().token
            ).addOnSuccessListener { localizacao ->

                if (localizacao != null) {

                    latitude = localizacao.latitude
                    longitude = localizacao.longitude

                    tvLocalizacao.text =
                        "Localização obtida!\n" +
                                "Latitude: $latitude\n" +
                                "Longitude: $longitude"

                    Toast.makeText(
                        this,
                        "Localização obtida!",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    tvLocalizacao.text =
                        "Não foi possível obter a localização."

                    Toast.makeText(
                        this,
                        "Verifique se a localização do aparelho está ativada e tente novamente.",
                        Toast.LENGTH_LONG
                    ).show()
                }

            }.addOnFailureListener {

                tvLocalizacao.text =
                    "Erro ao obter localização."

                Toast.makeText(
                    this,
                    "Erro ao obter localização",
                    Toast.LENGTH_LONG
                ).show()
            }

        } catch (e: SecurityException) {

            tvLocalizacao.text =
                "Permissão de localização não concedida."

            Toast.makeText(
                this,
                "Permissão de localização não concedida",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados)

        // Recebe os dados enviados pelas telas anteriores
        intencaoRecebida =
            intent.getStringExtra("intencao") ?: ""

        candidatoRecebido =
            intent.getStringExtra("candidato") ?: ""

        problemasRecebidos =
            intent.getStringArrayListExtra("problemas")
                ?: arrayListOf()

        // Inicializa os componentes do XML
        etNomeDados = findViewById(R.id.etNomeDados)
        etCelularDados = findViewById(R.id.etCelularDados)

        btConfirmarDados = findViewById(R.id.btConfirmarDados)
        btFinalizar = findViewById(R.id.btFinalizar)
        tvLocalizacao = findViewById(R.id.tvLocalizacao)

        // Ao clicar, solicita a localização
        btConfirmarDados.setOnClickListener {
            obterLocalizacao()
        }

        // Valida os dados antes de prosseguir
        btFinalizar.setOnClickListener {

            val nome = etNomeDados.text.toString().trim()
            val telefone = etCelularDados.text.toString().trim()

            if (nome.isEmpty()) {
                etNomeDados.error = "Digite o nome"
                return@setOnClickListener
            }

            if (telefone.isEmpty()) {
                etCelularDados.error = "Digite o celular"
                return@setOnClickListener
            }

            if (latitude == null || longitude == null) {
                Toast.makeText(
                    this,
                    "Obtenha a localização antes de finalizar.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Junta os problemas selecionados
            val problemas = problemasRecebidos.joinToString(", ")

            // Cria o objeto que será salvo no Room
            val entrevistado = Entrevistado().apply {
                this.nome = nome
                this.telefone = telefone
                this.voto = candidatoRecebido
                this.problema = problemas
                this.intencao = intencaoRecebida
                this.latitude = latitude
                this.longitude = longitude
            }

            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        banco.entrevistadoDao().insertall(entrevistado)
                    }

                    Toast.makeText(
                        this@DadosActivity,
                        "Entrevista salva com sucesso!",
                        Toast.LENGTH_LONG
                    ).show()

                    // Encerra a tela após salvar
                    finish()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@DadosActivity,
                        "Erro ao salvar: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // Ajusta a tela para as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

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