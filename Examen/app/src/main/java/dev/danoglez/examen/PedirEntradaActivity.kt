package dev.danoglez.examen

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PedirEntradaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedir_entrada)

        val etId = findViewById<EditText>(R.id.etId)
        val btnPedir = findViewById<Button>(R.id.btnPedir)
        val lblMensaje = findViewById<TextView>(R.id.lblMensaje)

        btnPedir.setOnClickListener {
            val idStr = etId.text.toString().trim()

            if (idStr.isEmpty()) {
                Toast.makeText(this, "Introduce un Identificador", Toast.LENGTH_SHORT).show()
                lblMensaje.text = "Error: Debes introducir un Identificador"
                return@setOnClickListener
            }

            val admin = AdminSQLiteOpenHelper(this, "conciertos.db", null, 1)
            val bd = admin.writableDatabase

            // 1. Consultar si el concierto existe y su aforo disponible
            val cursor = bd.rawQuery("SELECT aforoDisponible FROM conciertos WHERE id = ?", arrayOf(idStr))

            if (!cursor.moveToFirst()) {
                cursor.close()
                bd.close()
                lblMensaje.text = "Error: El Identificador no existe en la base de datos"
                Toast.makeText(this, "Error: El concierto no existe", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val aforoActual = cursor.getInt(0)
            cursor.close()

            // 2. Comprobar si el aforo actual es igual a cero
            if (aforoActual <= 0) {
                bd.close()
                lblMensaje.text = "Error: Se ha alcanzado el aforo máximo (0 entradas disponibles)"
                Toast.makeText(this, "Aforo máximo alcanzado", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            // 3. Restar 1 al aforo disponible y actualizar "lleno" si queda a 0
            val nuevoDisponible = aforoActual - 1
            val nuevoLleno = if (nuevoDisponible == 0) "SI" else "NO"

            val registro = ContentValues().apply {
                put("aforoDisponible", nuevoDisponible)
                put("lleno", nuevoLleno)
            }

            val filas = bd.update("conciertos", registro, "id = ?", arrayOf(idStr))
            bd.close()

            if (filas > 0) {
                val mensajeExito = if (nuevoDisponible == 0) {
                    "¡Entrada reservada! El aforo se ha completado (Lleno: SI)."
                } else {
                    "¡Entrada reservada con éxito! Quedan $nuevoDisponible entradas disponibles."
                }
                lblMensaje.text = mensajeExito
                Toast.makeText(this, "Entrada reservada", Toast.LENGTH_SHORT).show()
            } else {
                lblMensaje.text = "Error al actualizar la base de datos"
            }
        }
    }
}
