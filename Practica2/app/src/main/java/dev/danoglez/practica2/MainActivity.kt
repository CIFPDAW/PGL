package dev.danoglez.practica2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scrollView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias de Pregunta 1
        val rbP1Opc1 = findViewById<RadioButton>(R.id.rbP1Opc1)
        val rbP1Opc2 = findViewById<RadioButton>(R.id.rbP1Opc2)
        val rbP1Opc3 = findViewById<RadioButton>(R.id.rbP1Opc3)
        val tvFeedback1 = findViewById<TextView>(R.id.tvFeedback1)

        // Referencias de Pregunta 2 (Spinner)
        val spinnerPregunta2 = findViewById<Spinner>(R.id.spinnerPregunta2)
        val tvFeedback2 = findViewById<TextView>(R.id.tvFeedback2)

        // Referencias de Pregunta 3
        val rbP3Opc1 = findViewById<RadioButton>(R.id.rbP3Opc1)
        val rbP3Opc2 = findViewById<RadioButton>(R.id.rbP3Opc2)
        val rbP3Opc3 = findViewById<RadioButton>(R.id.rbP3Opc3)
        val tvFeedback3 = findViewById<TextView>(R.id.tvFeedback3)

        // Referencias de Pregunta 4 (Checkbox)
        val cbP4Opc1 = findViewById<CheckBox>(R.id.cbP4Opc1)
        val cbP4Opc2 = findViewById<CheckBox>(R.id.cbP4Opc2)
        val cbP4Opc3 = findViewById<CheckBox>(R.id.cbP4Opc3)
        val tvFeedback4 = findViewById<TextView>(R.id.tvFeedback4)

        // Referencias de Pregunta 5
        val rbP5Opc1 = findViewById<RadioButton>(R.id.rbP5Opc1)
        val rbP5Opc2 = findViewById<RadioButton>(R.id.rbP5Opc2)
        val rbP5Opc3 = findViewById<RadioButton>(R.id.rbP5Opc3)
        val tvFeedback5 = findViewById<TextView>(R.id.tvFeedback5)

        // Referencias de Pregunta 6
        val rbP6Opc1 = findViewById<RadioButton>(R.id.rbP6Opc1)
        val rbP6Opc2 = findViewById<RadioButton>(R.id.rbP6Opc2)
        val rbP6Opc3 = findViewById<RadioButton>(R.id.rbP6Opc3)
        val tvFeedback6 = findViewById<TextView>(R.id.tvFeedback6)

        // Referencias de Pregunta 7 (Spinner)
        val spinnerPregunta7 = findViewById<Spinner>(R.id.spinnerPregunta7)
        val tvFeedback7 = findViewById<TextView>(R.id.tvFeedback7)

        // Referencias de Pregunta 8
        val rbP8Opc1 = findViewById<RadioButton>(R.id.rbP8Opc1)
        val rbP8Opc2 = findViewById<RadioButton>(R.id.rbP8Opc2)
        val rbP8Opc3 = findViewById<RadioButton>(R.id.rbP8Opc3)
        val tvFeedback8 = findViewById<TextView>(R.id.tvFeedback8)

        // Referencias de Pregunta 9 (Checkbox)
        val cbP9Opc1 = findViewById<CheckBox>(R.id.cbP9Opc1)
        val cbP9Opc2 = findViewById<CheckBox>(R.id.cbP9Opc2)
        val cbP9Opc3 = findViewById<CheckBox>(R.id.cbP9Opc3)
        val tvFeedback9 = findViewById<TextView>(R.id.tvFeedback9)

        // Referencias de Pregunta 10
        val rbP10Opc1 = findViewById<RadioButton>(R.id.rbP10Opc1)
        val rbP10Opc2 = findViewById<RadioButton>(R.id.rbP10Opc2)
        val rbP10Opc3 = findViewById<RadioButton>(R.id.rbP10Opc3)
        val tvFeedback10 = findViewById<TextView>(R.id.tvFeedback10)

        val btnCorregir = findViewById<Button>(R.id.btnCorregir)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // Configurar Spinners
        val opcionesSpinner2 = arrayOf("Selecciona una opción", "1960", "1969", "1975", "1980")
        val adapterSpinner2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesSpinner2)
        adapterSpinner2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPregunta2.adapter = adapterSpinner2

        val opcionesSpinner7 = arrayOf("Selecciona una opción", "iOS", "Android", "Windows Phone")
        val adapterSpinner7 = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcionesSpinner7)
        adapterSpinner7.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPregunta7.adapter = adapterSpinner7

        // Lógica del botón Corregir
        btnCorregir.setOnClickListener {
            var aciertos = 0

            // P1: Capital de Francia -> París
            if (rbP1Opc2.isChecked) {
                aciertos++
                tvFeedback1.text = "¡Correcto!"
            } else if (rbP1Opc1.isChecked || rbP1Opc3.isChecked) {
                tvFeedback1.text = "Incorrecto. La respuesta correcta era: París"
            } else {
                tvFeedback1.text = "No respondida. Correcta: París"
            }

            // P2: Año Luna -> 1969
            val sel2 = spinnerPregunta2.selectedItem.toString()
            if (sel2 == "1969") {
                aciertos++
                tvFeedback2.text = "¡Correcto!"
            } else if (sel2 != "Selecciona una opción") {
                tvFeedback2.text = "Incorrecto. La respuesta correcta era: 1969"
            } else {
                tvFeedback2.text = "No seleccionado. Correcta: 1969"
            }

            // P3: Android nativo -> Kotlin
            if (rbP3Opc2.isChecked) {
                aciertos++
                tvFeedback3.text = "¡Correcto!"
            } else if (rbP3Opc1.isChecked || rbP3Opc3.isChecked) {
                tvFeedback3.text = "Incorrecto. La respuesta correcta era: Kotlin"
            } else {
                tvFeedback3.text = "No respondida. Correcta: Kotlin"
            }

            // P4: POO -> Java y Kotlin
            if (cbP4Opc1.isChecked && cbP4Opc2.isChecked && !cbP4Opc3.isChecked) {
                aciertos++
                tvFeedback4.text = "¡Correcto!"
            } else {
                tvFeedback4.text = "Incorrecto. Correctas: Java, Kotlin"
            }

            // P5: Lista desplegable cerrada -> Spinner
            if (rbP5Opc2.isChecked) {
                aciertos++
                tvFeedback5.text = "¡Correcto!"
            } else if (rbP5Opc1.isChecked || rbP5Opc3.isChecked) {
                tvFeedback5.text = "Incorrecto. La respuesta correcta era: Spinner"
            } else {
                tvFeedback5.text = "No respondida. Correcta: Spinner"
            }

            // P6: Posicionar con bordes -> ConstraintLayout
            if (rbP6Opc2.isChecked) {
                aciertos++
                tvFeedback6.text = "¡Correcto!"
            } else if (rbP6Opc1.isChecked || rbP6Opc3.isChecked) {
                tvFeedback6.text = "Incorrecto. La respuesta correcta era: ConstraintLayout"
            } else {
                tvFeedback6.text = "No respondida. Correcta: ConstraintLayout"
            }

            // P7: SO Google -> Android
            val sel7 = spinnerPregunta7.selectedItem.toString()
            if (sel7 == "Android") {
                aciertos++
                tvFeedback7.text = "¡Correcto!"
            } else if (sel7 != "Selecciona una opción") {
                tvFeedback7.text = "Incorrecto. La respuesta correcta era: Android"
            } else {
                tvFeedback7.text = "No seleccionado. Correcta: Android"
            }

            // P8: Crear Activity -> onCreate
            if (rbP8Opc2.isChecked) {
                aciertos++
                tvFeedback8.text = "¡Correcto!"
            } else if (rbP8Opc1.isChecked || rbP8Opc3.isChecked) {
                tvFeedback8.text = "Incorrecto. La respuesta correcta era: onCreate"
            } else {
                tvFeedback8.text = "No respondida. Correcta: onCreate"
            }

            // P9: Almacenamiento -> SQLite y SharedPreferences
            if (cbP9Opc1.isChecked && cbP9Opc2.isChecked && !cbP9Opc3.isChecked) {
                aciertos++
                tvFeedback9.text = "¡Correcto!"
            } else {
                tvFeedback9.text = "Incorrecto. Correctas: SQLite, SharedPreferences"
            }

            // P10: Extensión diseño visual -> xml
            if (rbP10Opc3.isChecked) {
                aciertos++
                tvFeedback10.text = "¡Correcto!"
            } else if (rbP10Opc1.isChecked || rbP10Opc2.isChecked) {
                tvFeedback10.text = "Incorrecto. La respuesta correcta era: xml"
            } else {
                tvFeedback10.text = "No respondida. Correcta: xml"
            }

            // Nota final sobre 10
            val nota = (aciertos * 10.0) / 10.0
            tvResultado.text = String.format(Locale.getDefault(), "Aciertos: %d / 10\nNota: %.1f / 10", aciertos, nota)
        }
    }
}
