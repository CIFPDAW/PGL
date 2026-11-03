package dev.danoglez.prctica5

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadosActivity : AppCompatActivity() {

    // Las mismas respuestas que se inyectaron en la generación del Cuestionario
    private val correctAnswers = listOf(
        "París", "8", "Kotlin", "Amazonas", "Da Vinci", 
        "1969", "Pacífico", "Oxígeno", "Guepardo", "Marte"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val tvNota = findViewById<TextView>(R.id.tvNota)
        val tvRecord = findViewById<TextView>(R.id.tvRecord)
        val tvResumen = findViewById<TextView>(R.id.tvResumen)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val username = intent.getStringExtra("USERNAME") ?: ""
        val userAnswers = intent.getStringArrayListExtra("ANSWERS") ?: ArrayList()

        var aciertos = 0
        val resumenBuilder = StringBuilder()

        for (i in correctAnswers.indices) {
            val userAnswer = if (i < userAnswers.size) userAnswers[i] else "No respondida"
            val correctAnswer = correctAnswers[i]

            resumenBuilder.append("Pregunta ${i + 1}: ")
            if (userAnswer == correctAnswer) {
                aciertos++
                resumenBuilder.append("Correcta ($userAnswer)\n")
            } else {
                resumenBuilder.append("Incorrecta. Tu respuesta: $userAnswer. Correcta: $correctAnswer\n")
            }
        }

        tvNota.text = "Aciertos: $aciertos\nNota: $aciertos/10"
        tvResumen.text = resumenBuilder.toString()

        // Comprobamos la nota en la BBDD
        comprobarYActualizarNota(username, aciertos, tvRecord)

        // Botón Opcional de Logout
        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            // Esto borra el historial de activities para que no pueda darle a "atrás" y volver
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun comprobarYActualizarNota(username: String, nuevaNota: Int, tvRecord: TextView) {
        val admin = DatabaseHelper(this, "CuestionarioBD", null, 1)
        val bd = admin.writableDatabase
        
        val fila = bd.rawQuery("select nota_maxima from usuarios where username='$username'", null)
        var notaMaximaAnterior = 0
        if (fila.moveToFirst()) {
            notaMaximaAnterior = fila.getInt(0)
        }
        fila.close()

        if (nuevaNota > notaMaximaAnterior) {
            tvRecord.text = "¡Felicidades! Has superado tu calificación más alta."
            
            // Actualizamos la base de datos con la nueva nota
            val registro = ContentValues()
            registro.put("nota_maxima", nuevaNota)
            bd.update("usuarios", registro, "username='$username'", null)
        } else {
            tvRecord.text = "No has superado tu calificación más alta ($notaMaximaAnterior)."
        }
        
        bd.close()
    }
}
