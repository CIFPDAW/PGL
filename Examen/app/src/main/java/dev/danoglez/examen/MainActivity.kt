package dev.danoglez.examen

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAnadir = findViewById<Button>(R.id.btnAnadir)
        val btnConsultar = findViewById<Button>(R.id.btnConsultar)
        val btnEntrada = findViewById<Button>(R.id.btnEntrada)
        val btnAmpliar = findViewById<Button>(R.id.btnAmpliar)

        btnAnadir.setOnClickListener {
            val intent = Intent(this, AnadirConciertoActivity::class.java)
            startActivity(intent)
        }

        btnConsultar.setOnClickListener {
            val intent = Intent(this, ConsultarConciertoActivity::class.java)
            startActivity(intent)
        }

        btnEntrada.setOnClickListener {
            val intent = Intent(this, PedirEntradaActivity::class.java)
            startActivity(intent)
        }

        btnAmpliar.setOnClickListener {
            val intent = Intent(this, AmpliarAforoActivity::class.java)
            startActivity(intent)
        }
    }
}
