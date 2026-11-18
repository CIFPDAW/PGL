package dev.danoglez.megarepasoexamen

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResumenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        val tvResumen = findViewById<TextView>(R.id.tvResumen)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        // 1. Recibimos los datos del Intent
        val nombre = intent.getStringExtra("NOMBRE") ?: ""
        val curso = intent.getStringExtra("CURSO") ?: ""
        val modalidad = intent.getStringExtra("MODALIDAD") ?: ""
        val beca = intent.getStringExtra("BECA") ?: ""

        // 2. Mostramos los datos en pantalla
        tvResumen.text = "Alumno: $nombre\nCurso: $curso\nModalidad: $modalidad\nEstado: $beca"

        // 3. Botón Volver (cierra esta pantalla y vuelve a la anterior)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
