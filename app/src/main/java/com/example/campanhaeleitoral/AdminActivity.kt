package com.example.campanhaeleitoral

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AdminActivity : AppCompatActivity() {
    private lateinit var btVoltar: Button
    private lateinit var btEleitores: Button
    private lateinit var btResultado: Button
    private lateinit var btLimpar: Button
    private lateinit var tvTotalEntrevistados: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btVoltar = findViewById(R.id.btRetornar)
        btEleitores = findViewById(R.id.btEleitores)
        btResultado = findViewById(R.id.btResultado)
        btLimpar = findViewById(R.id.btLimpar)
        tvTotalEntrevistados = findViewById(R.id.tvQnt)


        btEleitores.setOnClickListener {
            var eleitoresActivity: Intent
            eleitoresActivity = Intent(this, EleitoresActivity::class.java)
            startActivity(eleitoresActivity)
        }
        btResultado.setOnClickListener {
            var resultadoActivity: Intent
            resultadoActivity = Intent(this, ResultadoActivity::class.java)
            startActivity(resultadoActivity)
        }
        btLimpar.setOnClickListener {

        }
        btVoltar.setOnClickListener {
            finish()
        }
    }
        override fun onResume() {
            super.onResume()
            carregarTotalDeEntrevistados()
        }

        private fun carregarTotalDeEntrevistados() {
            lifecycleScope.launch(Dispatchers.IO) {
                val total = AppDatabase.getDatabase(this@AdminActivity)
                    .entrevistadoDao()
                    .contarEntrevistados()

                withContext(Dispatchers.Main) {
                    tvTotalEntrevistados.text = "$total"
                }
            }
        }
    }
