package dev.danoglez.a3ag

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var et1: EditText
    lateinit var et2: EditText
    lateinit var tv1: TextView
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets11
        }
Nuestra aplicación pedirá dos números al usuario y en esta ocasión mostrará cuatro botones con las
        et1 = findViewById(R.id.et1)
        et2 = findViewById(R.id.et2)
        tv1 = findViewById(R.id.tv1)
        button = findViewById(R.id.button)
    }

    fun sumanumeros(v: View?) {
        val nro1 = et1.text.toString().toInt()
        val nro2 = et2.text.toString().toInt()
        val suma = nro1 + nro2
        tv1.text = "Resultado: ${suma.toString()}"
    }
}
