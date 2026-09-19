package com.example.campanhaeleitoral

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var btLogin : Button
    private lateinit var etNome : EditText
    private lateinit var etSenha : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        btLogin = findViewById(R.id.btLogin)
        etNome = findViewById(R.id.edNome)
        etSenha = findViewById(R.id.edSenha)


        btLogin.setOnClickListener {
            var nome: String
            var senha: String

            nome = etNome.getText().toString()
            senha =etSenha.getText().toString()

            if (nome =="Usuario" && senha=="123") {
                var respostaActivity: Intent
                respostaActivity = Intent(this, RespostaActivity::class.java)
                startActivity(respostaActivity)
            }else if(nome =="Admin" && senha=="123"){
                var jogoActivity: Intent
                jogoActivity = Intent(this, AdminActivity::class.java)
                startActivity(jogoActivity)
            }
            else{
                Toast.makeText(this, "Login errado", Toast.LENGTH_SHORT).show()
            }

        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}