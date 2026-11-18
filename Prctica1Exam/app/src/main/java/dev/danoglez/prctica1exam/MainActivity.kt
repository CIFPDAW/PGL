package dev.danoglez.prctica1exam

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Numeros
        val etNum1 = findViewById<EditText>(R.id.etNum1)
        val etNum2 = findViewById<EditText>(R.id.etNum2)
        val etNum3 = findViewById<EditText>(R.id.etNum3)

        // Resultado
        val tvResult = findViewById<TextView>(R.id.tvResult)

        // Botones
        val btnSum = findViewById<Button>(R.id.btnSum)
        val btnSub = findViewById<Button>(R.id.btnSub)
        val btnMul = findViewById<Button>(R.id.btnMul)
        val btnDiv = findViewById<Button>(R.id.btnDiv)
        val btnPow = findViewById<Button>(R.id.btnPow)
        val btnReset = findViewById<Button>(R.id.btnReset)

        // Llamar Funciones
        btnSum.setOnClickListener { calcularSuma() }
        btnSub.setOnClickListener { calcularResta() }

        btnReset.setOnClickListener {
            etNum1.setText("")
            etNum2.setText("")
            etNum3.setText("")
            tvResult.text = "Resultado: "
        }

        // Obtener numeros
        fun ObtenerNumeros(): DoubleArray {
            var contador = 0

            if(etNum1.text.toString() != "") contador++
            if(etNum2.text.toString() != "") contador++
            if (etNum3.text.toString() != "") contador++

            val array = DoubleArray(contador)
            var i = 0

            if(etNum1.text.toString() != ""){
                array[i] = etNum1.text.toString().toDouble()
                i++
            }
            if(etNum2.text.toString() != ""){
                array[i] = etNum2.text.toString().toDouble()
                i++
            }
            if (etNum3.text.toString() != ""){
                array[i] = etNum3.text.toString().toDouble()
                i++
            }

            return  array
        }

        fun calcularSuma(){
            val numeros = ObtenerNumeros()

            // Comprobacion
            if(numeros.size < 2){
                tvResult.text = "Resultado: Faltan Numeros"
                Toast.makeText(this, "Resultado invalido, faltan número por asignar", Toast.LENGTH_SHORT).show()
            } else{
                var resultado = numeros[0]
                for(i in 1..numeros.size - 1){
                    resultado = resultado + numeros[i]
                }

                tvResult.text = "Resultado: $resultado"
            }
        }

        fun calcularResta(){
            val numeros = ObtenerNumeros()

            // Comrpobacion
            if(numeros.size < 2){
                tvResult.text = "Resultado: Faltan Numeros"
                Toast.makeText(this, "Resultado invalido, faltan número por asignar", Toast.LENGTH_SHORT).show()
            }else {
                var resultado = numeros[0]
                for (i in 1..numeros.size - 1){
                    resultado = resultado - numeros[i]
                }

                tvResult.text = "Resultado: $resultado"
            }
        }

        fun calcularMultiplicacion(){}
    }
}