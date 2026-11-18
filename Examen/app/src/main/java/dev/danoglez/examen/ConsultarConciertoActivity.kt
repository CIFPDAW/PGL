package dev.danoglez.examen

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ConsultarConciertoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar_concierto)

        val etId = findViewById<EditText>(R.id.etId)
        val btnConsultar = findViewById<Button>(R.id.btnConsultar)
        val lblDatos = findViewById<TextView>(R.id.lblDatos)

        btnConsultar.setOnClickListener {
            val idStr = etId.text.toString().trim()

            if (idStr.isEmpty()) {
                Toast.makeText(this, "Introduce un Identificador", Toast.LENGTH_SHORT).show()
                lblDatos.text = ""
                return@setOnClickListener
            }

            val admin = AdminSQLiteOpenHelper(this, "conciertos.db", null, 1)
            val bd = admin.readableDatabase

            val cursor = bd.rawQuery(
                "SELECT nombre, lugar, aforoTotal, aforoDisponible, lleno FROM conciertos WHERE id = ?",
                arrayOf(idStr)
            )

            if (cursor.moveToFirst()) {
                val nombre = cursor.getString(0)
                val lugar = cursor.getString(1)
                val total = cursor.getInt(2)
                val disponible = cursor.getInt(3)
                val lleno = cursor.getString(4)

                lblDatos.text = "Nombre: $nombre\n" +
                        "Lugar: $lugar\n" +
                        "Aforo Total: $total\n" +
                        "Aforo Disponible: $disponible\n" +
                        "Lleno: $lleno"
            } else {
                lblDatos.text = ""
                Toast.makeText(this, "Error: No se encuentra ningún concierto con ese Identificador", Toast.LENGTH_LONG).show()
            }

            cursor.close()
            bd.close()
        }
    }
}
