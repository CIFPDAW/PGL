package dev.danoglez.prctica5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Pregunta7Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pregunta)

        val tvPregunta = findViewById<TextView>(R.id.tvPregunta)
        val rgOpciones = findViewById<RadioGroup>(R.id.rgOpciones)
        val rbOp1 = findViewById<RadioButton>(R.id.rbOp1)
        val rbOp2 = findViewById<RadioButton>(R.id.rbOp2)
        val rbOp3 = findViewById<RadioButton>(R.id.rbOp3)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)

        tvPregunta.text = "Pregunta 7/10\n\n¿Cuál es el océano más grande?"
        rbOp1.text = "Atlántico"
        rbOp2.text = "Índico"
        rbOp3.text = "Pacífico"
        btnSiguiente.text = "Responder y avanzar a la siguiente pregunta"

        btnSiguiente.setOnClickListener {
            val selectedId = rgOpciones.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Por favor, selecciona una respuesta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val rbSeleccionado = findViewById<RadioButton>(selectedId)
            val respuestaTexto = rbSeleccionado.text.toString()

            val username = intent.getStringExtra("USERNAME") ?: ""
            val respuestas = intent.getStringArrayListExtra("ANSWERS") ?: ArrayList()
            respuestas.add(respuestaTexto)

            val intent = Intent(this, Pregunta8Activity::class.java)
            intent.putExtra("USERNAME", username)
            intent.putStringArrayListExtra("ANSWERS", respuestas)
            startActivity(intent)
            finish()
        }
    }
}
