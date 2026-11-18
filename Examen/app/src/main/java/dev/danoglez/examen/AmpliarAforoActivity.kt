package dev.danoglez.examen

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AmpliarAforoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ampliar_aforo)

        val etId = findViewById<EditText>(R.id.etId)
        val etNuevoAforo = findViewById<EditText>(R.id.etNuevoAforo)
        val btnAmpliar = findViewById<Button>(R.id.btnAmpliar)
        val lblMensaje2 = findViewById<TextView>(R.id.lblMensaje2)

        btnAmpliar.setOnClickListener {
            val idStr = etId.text.toString().trim()
            val nuevoAforoStr = etNuevoAforo.text.toString().trim()

            // 1. Validar campos vacíos
            if (idStr.isEmpty() || nuevoAforoStr.isEmpty()) {
                Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show()
                lblMensaje2.text = "Error: Rellena el Identificador y el nuevo aforo"
                return@setOnClickListener
            }

            val nuevoTotal = nuevoAforoStr.toIntOrNull()
            if (nuevoTotal == null || nuevoTotal <= 0) {
                Toast.makeText(this, "Error: El nuevo aforo debe ser un número válido mayor a 0", Toast.LENGTH_SHORT).show()
                lblMensaje2.text = "Error: El nuevo aforo debe ser mayor a 0"
                return@setOnClickListener
            }

            val admin = AdminSQLiteOpenHelper(this, "conciertos.db", null, 1)
            val bd = admin.writableDatabase

            // 2. Comprobar si el concierto existe y obtener aforos actuales
            val cursor = bd.rawQuery(
                "SELECT aforoTotal, aforoDisponible FROM conciertos WHERE id = ?",
                arrayOf(idStr)
            )

            if (!cursor.moveToFirst()) {
                cursor.close()
                bd.close()
                lblMensaje2.text = "Error: El concierto no existe en la base de datos"
                Toast.makeText(this, "Error: El concierto no existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val antiguoTotal = cursor.getInt(0)
            val antiguoDisponible = cursor.getInt(1)
            cursor.close()

            // 3. Comprobar si el nuevo aforo es superior al anterior
            if (nuevoTotal <= antiguoTotal) {
                bd.close()
                lblMensaje2.text = "Error: El nuevo aforo total ($nuevoTotal) debe ser superior al anterior ($antiguoTotal)"
                Toast.makeText(this, "El aforo debe ser superior al actual ($antiguoTotal)", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 4. Calcular el incremento y actualizar aforo total, disponible y LLENO
            val plazasExtra = nuevoTotal - antiguoTotal
            val nuevoDisponible = antiguoDisponible + plazasExtra
            val nuevoLleno = if (nuevoDisponible > 0) "NO" else "SI"

            val registro = ContentValues().apply {
                put("aforoTotal", nuevoTotal)
                put("aforoDisponible", nuevoDisponible)
                put("lleno", nuevoLleno)
            }

            val filas = bd.update("conciertos", registro, "id = ?", arrayOf(idStr))
            bd.close()

            if (filas > 0) {
                lblMensaje2.text = "¡Aforo ampliado con éxito!\nNuevo aforo total: $nuevoTotal\nPlazas disponibles: $nuevoDisponible\nLleno: $nuevoLleno"
                Toast.makeText(this, "Aforo ampliado correctamente", Toast.LENGTH_SHORT).show()
            } else {
                lblMensaje2.text = "Error al actualizar el aforo"
            }
        }
    }
}
