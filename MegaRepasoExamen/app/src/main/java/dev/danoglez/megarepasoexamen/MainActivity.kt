package dev.danoglez.megarepasoexamen

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.util.Base64

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dbHelper = DatabaseHelper(this)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)
        val etNum1 = findViewById<EditText>(R.id.etNum1)
        val etNum2 = findViewById<EditText>(R.id.etNum2)
        val btnSumar = findViewById<Button>(R.id.btnSumar)
        val tvResultadoSuma = findViewById<TextView>(R.id.tvResultadoSuma)
        val spCursos = findViewById<Spinner>(R.id.spCursos)
        val rbPresencial = findViewById<RadioButton>(R.id.rbPresencial)
        val cbBeca = findViewById<CheckBox>(R.id.cbBeca)
        val btnEnviar = findViewById<Button>(R.id.btnEnviar)
        val tvResultadoIns = findViewById<TextView>(R.id.tvResultadoInscripcion)

        btnSumar.setOnClickListener {
            val numero1 = etNum1.text.toString().toIntOrNull() ?: 0
            val numero2 = etNum2.text.toString().toIntOrNull() ?: 0

            val result = numero1 + numero2;

            tvResultadoSuma.text = ("Horas totales: $result")
        }

        val opciones = arrayOf("Kotlin", "Java", "Python")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spCursos.adapter = adapter

        btnEnviar.setOnClickListener {
            val select = spCursos.selectedItem.toString()
            val presencial = if (rbPresencial.isChecked) "Presencial" else "Online"
            val beca = if (cbBeca.isChecked) "Con Beca" else "Sin Beca"

            val nombre = etNombre.text.toString().trim()
            if (dbHelper.existeUsuario(nombre)){
                val intent = Intent(this, ResumenActivity::class.java).apply {
                    putExtra("NOMBRE", nombre)
                    putExtra("CURSO", select)
                    putExtra("MODALIDAD", presencial)
                    putExtra("BECA", beca)
                }
                startActivity(intent)
            }else{
                Toast.makeText(this, "El alumno no está registrado, dale a registrar primero.", Toast.LENGTH_SHORT).show()
            }


        }

        btnRegistrar.setOnClickListener {
            val nombre = etNombre.text.toString()

            var result = dbHelper.registrarUsuario(nombre)

            if (result) {
                Toast.makeText(this, "¡Alumno registrado con éxito!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Error: el alumno ya está registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }
}