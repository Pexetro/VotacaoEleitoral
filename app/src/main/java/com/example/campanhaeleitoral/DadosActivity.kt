
package com.example.campanhaeleitoral

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
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

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class DadosActivity : AppCompatActivity() {

    private lateinit var etNomeDados: EditText
    private lateinit var etCelularDados: EditText

    private lateinit var btConfirmarDados: Button
    private lateinit var btFinalizar: Button

    private lateinit var tvLocalizacao: TextView

    // Serviço de localização
    private lateinit var fusedLocationClient:
            FusedLocationProviderClient

    // Armazenam as coordenadas obtidas
    private var latitude: Double? = null
    private var longitude: Double? = null

    // Solicita permissão de localização
    private val solicitarPermissao =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissoes ->

            val permitido =
                permissoes[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||
                        permissoes[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (permitido) {
                obterLocalizacao()
            } else {
                tvLocalizacao.text =
                    "Permissão de localização negada"

                Toast.makeText(
                    this,
                    "Permita o acesso à localização para continuar",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dados)

        // Referências dos componentes XML
        etNomeDados = findViewById(R.id.etNomeDados)
        etCelularDados = findViewById(R.id.etCelularDados)

        btConfirmarDados = findViewById(R.id.btConfirmarDados)
        btFinalizar = findViewById(R.id.btFinalizar)

        tvLocalizacao = findViewById(R.id.tvLocalizacao)

        // Inicializa o serviço de localização
        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(this)

        // Botão para solicitar a localização
        btConfirmarDados.setOnClickListener {
            verificarPermissao()
        }

        // Botão para finalizar e enviar os dados
        btFinalizar.setOnClickListener {

            val nome = etNomeDados.text.toString().trim()
            val celular = etCelularDados.text.toString().trim()

            if (nome.isEmpty() || celular.isEmpty()) {
                Toast.makeText(
                    this,
                    "Preencha o nome e o celular",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val intent = Intent(
                this,
                RespostaActivity::class.java
            )

            // Envia os dados do entrevistado
            intent.putExtra("nome", nome)
            intent.putExtra("celular", celular)

            // Envia as coordenadas, se foram obtidas
            latitude?.let {
                intent.putExtra("latitude", it)
            }

            longitude?.let {
                intent.putExtra("longitude", it)
            }

            startActivity(intent)
        }

        // Ajusta a tela para as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v, insets ->

            val systemBars =
                insets.getInsets(
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

    // Verifica se a permissão já foi concedida
    private fun verificarPermissao() {

        val permissaoPrecisa =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val permissaoAproximada =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (permissaoPrecisa || permissaoAproximada) {

            obterLocalizacao()

        } else {

            solicitarPermissao.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    // Obtém a localização atual do dispositivo
    private fun obterLocalizacao() {

        val permissaoPrecisa =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val permissaoAproximada =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        if (!permissaoPrecisa && !permissaoAproximada) {
            return
        }

        tvLocalizacao.text = "Obtendo localização..."

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            CancellationTokenSource().token
        ).addOnSuccessListener { location ->

            if (location != null) {

                // Guarda latitude e longitude
                latitude = location.latitude
                longitude = location.longitude

                // Exibe as coordenadas na tela
                tvLocalizacao.text =
                    "Localização identificada!\n" +
                            "Latitude: $latitude\n" +
                            "Longitude: $longitude"

                Toast.makeText(
                    this,
                    "Localização obtida com sucesso!",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                tvLocalizacao.text =
                    "Não foi possível obter a localização"

                Toast.makeText(
                    this,
                    "Verifique se a localização do dispositivo está ativada",
                    Toast.LENGTH_LONG
                ).show()
            }

        }.addOnFailureListener {

            tvLocalizacao.text =
                "Erro ao obter localização"

            Toast.makeText(
                this,
                "Falha ao identificar localização",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}