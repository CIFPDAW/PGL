package dev.danoglez.prctica5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BienvenidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val tvNotaMaxima = findViewById<TextView>(R.id.tvNotaMaxima)
        val btnComenzar = findViewById<Button>(R.id.btnComenzar)

        val username = intent.getStringExtra("USERNAME") ?: "Usuario"
        val notaMax = intent.getIntExtra("NOTA_MAXIMA", 0)

        tvSaludo.text = "¡Bienvenido/a, $username!"
        tvNotaMaxima.text = "Tu nota más alta hasta la fecha: $notaMax"

        btnComenzar.setOnClickListener {
            val intent = Intent(this, Pregunta1Activity::class.java)
            intent.putExtra("USERNAME", username)
            // Creamos un array vacio de strings para guardar las respuestas
            intent.putStringArrayListExtra("ANSWERS", ArrayList())
            startActivity(intent)
            finish()
        }
    }
}
