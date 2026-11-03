package dev.danoglez.practica1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Tal como indica la Actividad Guiada, usamos lateinit var para declarar las vistas
    lateinit var etNum1: EditText
    lateinit var etNum2: EditText
    lateinit var etNum3: EditText
    lateinit var tvResult: TextView
    lateinit var btnSum: Button
    lateinit var btnSub: Button
    lateinit var btnMul: Button
    lateinit var btnDiv: Button
    lateinit var btnPow: Button
    lateinit var btnRoot: Button
    lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recuperamos los elementos del XML (findViewById)
        etNum1 = findViewById(R.id.etNum1)
        etNum2 = findViewById(R.id.etNum2)
        etNum3 = findViewById(R.id.etNum3)
        tvResult = findViewById(R.id.tvResult)
        
        btnSum = findViewById(R.id.btnSum)
        btnSub = findViewById(R.id.btnSub)
        btnMul = findViewById(R.id.btnMul)
        btnDiv = findViewById(R.id.btnDiv)
        btnPow = findViewById(R.id.btnPow)
        btnRoot = findViewById(R.id.btnRoot)
        btnReset = findViewById(R.id.btnReset)

        // Asignamos las acciones a los botones (setOnClickListener)
        btnSum.setOnClickListener { calcularSuma() }
        btnSub.setOnClickListener { calcularResta() }
        btnMul.setOnClickListener { calcularMultiplicacion() }
        btnDiv.setOnClickListener { calcularDivision() }
        btnPow.setOnClickListener { calcularElevar() }
        btnRoot.setOnClickListener { calcularRaiz() }
        
        btnReset.setOnClickListener { 
            etNum1.setText("")
            etNum2.setText("")
            etNum3.setText("")
            tvResult.text = "Resultado: "
        }
    }

    // Esta función lee las 3 cajas. Solo guarda en un Array de Kotlin las que tengan texto.
    // Así si rellenas la 1 y la 3, funciona perfectamente.
    fun obtenerNumerosRellenos(): DoubleArray {
        var contador = 0
        if (etNum1.text.toString() != "") contador = contador + 1
        if (etNum2.text.toString() != "") contador = contador + 1
        if (etNum3.text.toString() != "") contador = contador + 1
        
        val array = DoubleArray(contador)
        var i = 0
        if (etNum1.text.toString() != "") {
            array[i] = etNum1.text.toString().toDouble()
            i = i + 1
        }
        if (etNum2.text.toString() != "") {
            array[i] = etNum2.text.toString().toDouble()
            i = i + 1
        }
        if (etNum3.text.toString() != "") {
            array[i] = etNum3.text.toString().toDouble()
            i = i + 1
        }
        return array
    }

    // Oculta el .0 si el número es exacto
    fun formato(resultado: Double): String {
        if (resultado % 1.0 == 0.0) {
            val valorEntero = resultado.toLong()
            return valorEntero.toString()
        } else {
            return resultado.toString()
        }
    }

    fun calcularSuma() {
        val numeros = obtenerNumerosRellenos()
        if (numeros.size < 2) {
            tvResult.text = "Resultado: Faltan números"
        } else {
            var resultado = numeros[0]
            for (i in 1..numeros.size - 1) {
                resultado = resultado + numeros[i]
            }
            tvResult.text = "Resultado: " + formato(resultado)
        }
    }

    fun calcularResta() {
        val numeros = obtenerNumerosRellenos()
        if (numeros.size < 2) {
            tvResult.text = "Resultado: Faltan números"
        } else {
            var resultado = numeros[0]
            for (i in 1..numeros.size - 1) {
                resultado = resultado - numeros[i]
            }
            tvResult.text = "Resultado: " + formato(resultado)
        }
    }

    fun calcularMultiplicacion() {
        val numeros = obtenerNumerosRellenos()
        if (numeros.size < 2) {
            tvResult.text = "Resultado: Faltan números"
        } else {
            var resultado = numeros[0]
            for (i in 1..numeros.size - 1) {
                resultado = resultado * numeros[i]
            }
            tvResult.text = "Resultado: " + formato(resultado)
        }
    }

    fun calcularDivision() {
        val numeros = obtenerNumerosRellenos()
        if (numeros.size < 2) {
            tvResult.text = "Resultado: Faltan números"
        } else {
            var resultado = numeros[0]
            for (i in 1..numeros.size - 1) {
                if (numeros[i] == 0.0) {
                    tvResult.text = "Resultado: No se puede dividir por cero"
                    return
                }
                resultado = resultado / numeros[i]
            }
            tvResult.text = "Resultado: " + formato(resultado)
        }
    }

    fun calcularElevar() {
        val numeros = obtenerNumerosRellenos()
        if (numeros.size < 2) {
            tvResult.text = "Resultado: Faltan números"
        } else {
            var resultado = numeros[0]
            for (i in 1..numeros.size - 1) {
                resultado = Math.pow(resultado, numeros[i])
            }
            tvResult.text = "Resultado: " + formato(resultado)
        }
    }

    fun calcularRaiz() {
        val numeros = obtenerNumerosRellenos()
        if (numeros.size < 2) {
            tvResult.text = "Resultado: Faltan números"
        } else {
            var resultado = numeros[0]
            for (i in 1..numeros.size - 1) {
                if (numeros[i] == 0.0) {
                    tvResult.text = "Resultado: La raíz no puede ser cero"
                    return
                }
                // Matemáticamente, la Raíz N de X es igual a elevar X a (1 / N).
                resultado = Math.pow(resultado, 1.0 / numeros[i])
            }
            tvResult.text = "Resultado: " + formato(resultado)
        }
    }
}