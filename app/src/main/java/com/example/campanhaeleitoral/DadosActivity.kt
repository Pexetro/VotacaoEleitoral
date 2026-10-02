package com.example.campanhaeleitoral

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale

class DadosActivity : AppCompatActivity() {

    private lateinit var etNomeDados: EditText
    private lateinit var etCelularDados: EditText
    private lateinit var btConfirmarDados: Button
    private lateinit var btFinalizar: Button
    private lateinit var tvLocalizacao: TextView

    private var latitude: Double? = null
    private var longitude: Double? = null

    private var intencaoRecebida: String = ""
    private var candidatoRecebido: String = ""
    private var problemasRecebidos: ArrayList<String> = arrayListOf()

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val solicitarPermissaoLocalizacao =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissoes ->

            val permissaoConcedida =
                permissoes[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                        permissoes[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (permissaoConcedida) {
                obterLocalizacao()
            } else {
                Toast.makeText(
                    this,
                    "Permissão de localização negada.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados)

        etNomeDados = findViewById(R.id.etNomeDados)
        etCelularDados = findViewById(R.id.etCelularDados)
        btConfirmarDados = findViewById(R.id.btConfirmarDados)
        btFinalizar = findViewById(R.id.btFinalizar)
        tvLocalizacao = findViewById(R.id.tvLocalizacao)

        intencaoRecebida = intent.getStringExtra("intencao") ?: ""
        candidatoRecebido = intent.getStringExtra("candidato") ?: ""
        problemasRecebidos =
            intent.getStringArrayListExtra("problemas") ?: arrayListOf()

        btConfirmarDados.setOnClickListener {
            verificarPermissaoLocalizacao()
        }

        btFinalizar.setOnClickListener {
            finalizarCadastro()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }
    }

    private fun verificarPermissaoLocalizacao() {

        val permissaoPrecisao = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permissaoAproximada = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (permissaoPrecisao || permissaoAproximada) {
            obterLocalizacao()
        } else {
            solicitarPermissaoLocalizacao.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun obterLocalizacao() {

        val permissaoPrecisao = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val permissaoAproximada = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissaoPrecisao && !permissaoAproximada) {
            Toast.makeText(
                this,
                "Permissão de localização não concedida.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        tvLocalizacao.text = "Obtendo localização..."

        val prioridade = if (permissaoPrecisao) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }

        try {
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
                    tvLocalizacao.text = "Não foi possível obter a localização."

                    Toast.makeText(
                        this,
                        "Localização não encontrada.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }.addOnFailureListener { erro ->

                tvLocalizacao.text = "Erro ao obter localização."

                Toast.makeText(
                    this,
                    "Erro: ${erro.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: SecurityException) {
            Toast.makeText(
                this,
                "Permissão de localização não concedida.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun finalizarCadastro() {

        val nome = etNomeDados.text.toString().trim()
        val telefone = etCelularDados.text.toString().trim()

        if (nome.isEmpty()) {
            etNomeDados.error = "Digite o nome"
            return
        }

        if (telefone.isEmpty()) {
            etCelularDados.error = "Digite o celular"
            return
        }

        if (latitude == null || longitude == null) {
            Toast.makeText(
                this,
                "Obtenha a localização antes de finalizar.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val latitudeCapturada = latitude!!
        val longitudeCapturada = longitude!!

        val problemas = problemasRecebidos.joinToString(", ")

        val entrevistado = Entrevistado().apply {
            this.nome = nome
            this.telefone = telefone
            this.voto = candidatoRecebido
            this.problema = problemas
            this.intencao = intencaoRecebida
            this.latitude = latitudeCapturada
            this.longitude = longitudeCapturada
            this.dataHora = System.currentTimeMillis()
        }

        btFinalizar.isEnabled = false

        lifecycleScope.launch {

            try {
                withContext(Dispatchers.IO) {

                    val geocoder = Geocoder(
                        this@DadosActivity,
                        Locale.getDefault()
                    )

                    val cidade = try {
                        @Suppress("DEPRECATION")
                        val enderecos = geocoder.getFromLocation(
                            latitudeCapturada,
                            longitudeCapturada,
                            1
                        )

                        enderecos?.firstOrNull()?.let { endereco ->
                            endereco.locality
                                ?: endereco.subAdminArea
                        } ?: "Não identificada"

                    } catch (e: IOException) {
                        "Não identificada"
                    } catch (e: Exception) {
                        "Não identificada"
                    }

                    entrevistado.cidade = cidade

                    android.util.Log.d(
                        "DADOS_ENTREVISTADO",
                        """
                        Nome: ${entrevistado.nome}
                        Cidade: ${entrevistado.cidade}
                        Latitude: ${entrevistado.latitude}
                        Longitude: ${entrevistado.longitude}
                        """.trimIndent()
                    )

                    AppDatabase.getDatabase(applicationContext)
                        .entrevistadoDao()
                        .insertall(entrevistado)
                }

                Toast.makeText(
                    this@DadosActivity,
                    "Entrevistado cadastrado com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

                val intentMain = Intent(
                    this@DadosActivity,
                    MainActivity::class.java
                )

                val intentResposta = Intent(
                    this@DadosActivity,
                    RespostaActivity::class.java
                )

                startActivities(
                    arrayOf(intentMain, intentResposta)
                )

                finish()

            } catch (e: Exception) {

                btFinalizar.isEnabled = true

                Toast.makeText(
                    this@DadosActivity,
                    "Erro ao salvar: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}