package dev.danoglez.prctica4

import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etInput = findViewById<EditText>(R.id.etInput)
        val spinnerMethod = findViewById<Spinner>(R.id.spinnerMethod)
        val btnEncrypt = findViewById<Button>(R.id.btnEncrypt)
        val btnDecrypt = findViewById<Button>(R.id.btnDecrypt)

        val methods = arrayOf(
            getString(R.string.method_caesar),
            getString(R.string.method_reverse),
            getString(R.string.method_base64)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, methods)
        spinnerMethod.adapter = adapter

        btnEncrypt.setOnClickListener {
            val input = etInput.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(this, "Introduce un texto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val method = spinnerMethod.selectedItemPosition
            val result = processText(input, method, true)
            
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("RESULT_TEXT", result)
            }
            startActivity(intent)
        }

        btnDecrypt.setOnClickListener {
            val input = etInput.text.toString()
            if (input.isEmpty()) {
                Toast.makeText(this, "Introduce un texto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val method = spinnerMethod.selectedItemPosition
            val result = processText(input, method, false)
            
            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("RESULT_TEXT", result)
            }
            startActivity(intent)
        }
    }

    private fun processText(input: String, method: Int, encrypt: Boolean): String {
        return when (method) {
            0 -> caesarCipher(input, if (encrypt) 3 else -3)
            1 -> input.reversed() // Reverse is the same for encrypt and decrypt
            2 -> if (encrypt) {
                Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
            } else {
                try {
                    String(Base64.decode(input, Base64.DEFAULT))
                } catch (_: Exception) {
                    "Error al decodificar Base64"
                }
            }
            else -> input
        }
    }

    private fun caesarCipher(input: String, shift: Int): String {
        val result = StringBuilder()
        for (char in input) {
            if (char.isLetter()) {
                val base = if (char.isUpperCase()) 'A' else 'a'
                var shifted = (char - base + shift) % 26
                if (shifted < 0) {
                    shifted += 26
                }
                result.append((base.code + shifted).toChar())
            } else {
                result.append(char)
            }
        }
        return result.toString()
    }
}
