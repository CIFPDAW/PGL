package dev.danoglez.prctica3

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        val tvMetodoUsado = findViewById<TextView>(R.id.tvMetodoUsado)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val bundle = intent.extras
        val texto = bundle?.getString("texto") ?: ""
        val metodo = bundle?.getString("metodo") ?: "César (+3)"
        val operacion = bundle?.getString("operacion") ?: "encriptar"

        val resultadoProcesado = when (metodo) {
            "César (+3)" -> cifradoCesar(texto, operacion == "encriptar")
            "Invertir texto" -> texto.reversed()
            "ROT13" -> rot13(texto)
            else -> texto
        }

        tvMetodoUsado.text = "Método: $metodo (${operacion.uppercase()})"
        tvResultado.text = resultadoProcesado

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cifradoCesar(texto: String, encriptar: Boolean): String {
        val shift = if (encriptar) 3 else -3
        val sb = StringBuilder()
        for (char in texto) {
            if (char.isLetter()) {
                val base = if (char.isLowerCase()) 'a' else 'A'
                val shifted = ('a'.code + (char.code - base.code + shift + 26) % 26).toChar()
                sb.append(shifted)
            } else {
                sb.append(char)
            }
        }
        return sb.toString()
    }

    private fun rot13(texto: String): String {
        val sb = StringBuilder()
        for (char in texto) {
            sb.append(
                when (char) {
                    in 'a'..'m' -> char + 13
                    in 'n'..'z' -> char - 13
                    in 'A'..'M' -> char + 13
                    in 'N'..'Z' -> char - 13
                    else -> char
                }
            )
        }
        return sb.toString()
    }
}
