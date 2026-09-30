package com.example.campanhaeleitoral

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultadoActivity : AppCompatActivity() {

    private lateinit var tvTotalEntrevistados: TextView
    private lateinit var textView6: TextView
    private lateinit var btReturn: Button
    private lateinit var pcGrafico: PieChart
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_resultado)

        // Inicialização das Views
        tvTotalEntrevistados = findViewById(R.id.tvQuant)
        textView6 = findViewById(R.id.textView6)
        btReturn = findViewById(R.id.btReturn)
        pcGrafico = findViewById(R.id.pcGrafico)

        // Inicialização do Banco de Dados
        database = AppDatabase.getDatabase(this)

        // Aplicar margens das barras de sistema (Edge-to-Edge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        configurarEstiloGrafico()

        btReturn.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        carregarDadosDoBanco()
    }

    private fun configurarEstiloGrafico() {
        pcGrafico.description.isEnabled = false
        pcGrafico.centerText = "Resultado\nPesquisa"
        pcGrafico.setCenterTextSize(18f)
        pcGrafico.animateY(1000)
        pcGrafico.setUsePercentValues(true)
    }

    private fun carregarDadosDoBanco() {
        lifecycleScope.launch(Dispatchers.IO) {
            val total = database.entrevistadoDao().contarEntrevistados()
            val listaVotos = database.entrevistadoDao().obterResultadoPesquisa()

            withContext(Dispatchers.Main) {
                tvTotalEntrevistados.text = total.toString()
                atualizarInterfaceComDados(listaVotos, total)
            }
        }
    }

    private fun atualizarInterfaceComDados(lista: List<VotoCandidato>, total: Int) {
        val entries = ArrayList<PieEntry>()
        val textoDetalhado = StringBuilder("Quantidade de votos para cada candidato:\n\n")

        for (item in lista) {
            val nomeCandidato = if (item.candidato.isNullOrEmpty()) "Indeciso" else item.candidato

            entries.add(PieEntry(item.quantidadeVotos.toFloat(), nomeCandidato))

            val porcentagem = if (total > 0) (item.quantidadeVotos.toFloat() / total) * 100 else 0f
            textoDetalhado.append(String.format("%s: %d votos (%.1f%%)\n", nomeCandidato, item.quantidadeVotos, porcentagem))
        }

        textView6.text = textoDetalhado.toString()

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 14f

        val legenda = pcGrafico.legend
        legenda.textColor = Color.WHITE

        val data = PieData(dataSet)

        data.setValueFormatter(PercentFormatter(pcGrafico))

        pcGrafico.data = data
        pcGrafico.notifyDataSetChanged()
        pcGrafico.invalidate()
    }
}
