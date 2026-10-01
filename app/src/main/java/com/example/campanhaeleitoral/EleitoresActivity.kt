package com.example.campanhaeleitoral

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EleitoresActivity : AppCompatActivity() {

    private lateinit var rvEntrevistados: RecyclerView
    private lateinit var tvNenhumRegistro: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eleitores)

        rvEntrevistados = findViewById(R.id.rvEntrevistados)
        tvNenhumRegistro = findViewById(R.id.tvNenhumRegistro)

        rvEntrevistados.layoutManager =
            LinearLayoutManager(this)

        carregarEntrevistados()
    }

    private fun carregarEntrevistados() {

        lifecycleScope.launch {

            val entrevistados = withContext(Dispatchers.IO) {

                AppDatabase.getDatabase(applicationContext)
                    .entrevistadoDao()
                    .listar()
            }

            if (entrevistados.isEmpty()) {

                tvNenhumRegistro.visibility = View.VISIBLE
                rvEntrevistados.visibility = View.GONE

            } else {

                tvNenhumRegistro.visibility = View.GONE
                rvEntrevistados.visibility = View.VISIBLE

                rvEntrevistados.adapter =
                    EleitorAdapter(entrevistados)
            }
        }
    }
}