package dev.danoglez.examen

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AnadirConciertoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_concierto)

        val etId = findViewById<EditText>(R.id.etId)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etLugar = findViewById<EditText>(R.id.etLugar)
        val etTotal = findViewById<EditText>(R.id.etTotal)
        val rbSi = findViewById<RadioButton>(R.id.rbSi)
        val rbNo = findViewById<RadioButton>(R.id.rbNo)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val idStr = etId.text.toString().trim()
            val nombre = etNombre.text.toString().trim()
            val lugar = etLugar.text.toString().trim()
            val totalStr = etTotal.text.toString().trim()

            // 1. Validar campos vacíos
            if (idStr.isEmpty() || nombre.isEmpty() || lugar.isEmpty() || totalStr.isEmpty()) {
                Toast.makeText(this, "Error: Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = idStr.toIntOrNull()
            val aforoTotal = totalStr.toIntOrNull()

            if (id == null || aforoTotal == null || aforoTotal <= 0) {
                Toast.makeText(this, "Error: Los números deben ser válidos y mayores a 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Comprobar concordancia entre Lleno y Aforo Disponible
            val estaLleno = rbSi.isChecked
            val lleno = if (estaLleno) "SI" else "NO"
            val aforoDisponible = if (estaLleno) 0 else aforoTotal

            // 3. Abrir BD y comprobar si el ID ya existe
            val admin = AdminSQLiteOpenHelper(this, "conciertos.db", null, 1)
            val bd = admin.writableDatabase

            val consulta = bd.rawQuery("SELECT id FROM conciertos WHERE id = ?", arrayOf(id.toString()))
            if (consulta.moveToFirst()) {
                consulta.close()
                bd.close()
                Toast.makeText(this, "Error: El Identificador ya existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            consulta.close()

            // 4. Insertar en la BD
            val registro = ContentValues().apply {
                put("id", id)
                put("nombre", nombre)
                put("lugar", lugar)
                put("aforoTotal", aforoTotal)
                put("aforoDisponible", aforoDisponible)
                put("lleno", lleno)
            }

            val resultado = bd.insert("conciertos", null, registro)
            bd.close()

            if (resultado != -1L) {
                Toast.makeText(this, "Concierto dado de alta correctamente", Toast.LENGTH_SHORT).show()
                // Limpiar formulario
                etId.text.clear()
                etNombre.text.clear()
                etLugar.text.clear()
                etTotal.text.clear()
                rbNo.isChecked = true
            } else {
                Toast.makeText(this, "Error: No se pudo insertar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
