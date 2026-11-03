package dev.danoglez.prctica3

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTexto = findViewById<EditText>(R.id.etTexto)
        val spinnerMetodo = findViewById<Spinner>(R.id.spinnerMetodo)
        val btnEncriptar = findViewById<Button>(R.id.btnEncriptar)
        val btnDesencriptar = findViewById<Button>(R.id.btnDesencriptar)

        val metodos = arrayOf("César (+3)", "Invertir texto", "ROT13")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, metodos)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMetodo.adapter = adaptador

        btnEncriptar.setOnClickListener {
            val texto = etTexto.text.toString()
            if (texto.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un texto", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ResultadoActivity::class.java).apply {
                    putExtra("texto", texto)
                    putExtra("metodo", spinnerMetodo.selectedItem.toString())
                    putExtra("operacion", "encriptar")
                }
                startActivity(intent)
            }
        }

        btnDesencriptar.setOnClickListener {
            val texto = etTexto.text.toString()
            if (texto.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un texto", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ResultadoActivity::class.java).apply {
                    putExtra("texto", texto)
                    putExtra("metodo", spinnerMetodo.selectedItem.toString())
                    putExtra("operacion", "desencriptar")
                }
                startActivity(intent)
            }
        }
    }
}
