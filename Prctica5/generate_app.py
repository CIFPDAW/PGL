import os
import xml.etree.ElementTree as ET

base_dir = "/home/danoglez/Documentos/2 DAM/PGL/Prctica5/app/src/main/java/dev/danoglez/prctica5/"
res_layout_dir = "/home/danoglez/Documentos/2 DAM/PGL/Prctica5/app/src/main/res/layout/"
manifest_path = "/home/danoglez/Documentos/2 DAM/PGL/Prctica5/app/src/main/AndroidManifest.xml"

questions = [
    ("¿Cuál es la capital de Francia?", ["Madrid", "París", "Roma"], "París"),
    ("¿Cuántos planetas hay en el sistema solar?", ["7", "8", "9"], "8"),
    ("¿Qué lenguaje se usa en Android Studio principalmente hoy?", ["Kotlin", "Swift", "C++"], "Kotlin"),
    ("¿Cuál es el río más largo del mundo?", ["Nilo", "Amazonas", "Misisipi"], "Amazonas"),
    ("¿Quién pintó la Mona Lisa?", ["Van Gogh", "Da Vinci", "Picasso"], "Da Vinci"),
    ("¿En qué año llegó el hombre a la luna?", ["1969", "1959", "1979"], "1969"),
    ("¿Cuál es el océano más grande?", ["Atlántico", "Índico", "Pacífico"], "Pacífico"),
    ("¿Qué gas respiramos para vivir?", ["Oxígeno", "Dióxido de carbono", "Nitrógeno"], "Oxígeno"),
    ("¿Cuál es el animal terrestre más rápido?", ["León", "Guepardo", "Caballo"], "Guepardo"),
    ("¿Cuál es el planeta rojo?", ["Marte", "Júpiter", "Venus"], "Marte")
]

# 1. Generate 10 Question Activities
for i in range(1, 11):
    q = questions[i-1]
    next_act = f"Pregunta{i+1}Activity" if i < 10 else "ResultadosActivity"
    btn_text = "Responder y avanzar a la siguiente pregunta" if i < 10 else "Finalizar cuestionario y ver resultados"

    code = f"""package dev.danoglez.prctica5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Pregunta{i}Activity : AppCompatActivity() {{
    override fun onCreate(savedInstanceState: Bundle?) {{
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pregunta)

        val tvPregunta = findViewById<TextView>(R.id.tvPregunta)
        val rgOpciones = findViewById<RadioGroup>(R.id.rgOpciones)
        val rbOp1 = findViewById<RadioButton>(R.id.rbOp1)
        val rbOp2 = findViewById<RadioButton>(R.id.rbOp2)
        val rbOp3 = findViewById<RadioButton>(R.id.rbOp3)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)

        tvPregunta.text = "Pregunta {i}/10\\n\\n{q[0]}"
        rbOp1.text = "{q[1][0]}"
        rbOp2.text = "{q[1][1]}"
        rbOp3.text = "{q[1][2]}"
        btnSiguiente.text = "{btn_text}"

        btnSiguiente.setOnClickListener {{
            val selectedId = rgOpciones.checkedRadioButtonId
            if (selectedId == -1) {{
                Toast.makeText(this, "Por favor, selecciona una respuesta", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }}

            val rbSeleccionado = findViewById<RadioButton>(selectedId)
            val respuestaTexto = rbSeleccionado.text.toString()

            val username = intent.getStringExtra("USERNAME") ?: ""
            val respuestas = intent.getStringArrayListExtra("ANSWERS") ?: ArrayList()
            respuestas.add(respuestaTexto)

            val intent = Intent(this, {next_act}::class.java)
            intent.putExtra("USERNAME", username)
            intent.putStringArrayListExtra("ANSWERS", respuestas)
            startActivity(intent)
            finish()
        }}
    }}
}}
"""
    with open(f"{base_dir}Pregunta{i}Activity.kt", "w") as f:
        f.write(code)

# 2. Add Activities to Manifest safely
with open(manifest_path, 'r') as f:
    manifest_content = f.read()

activities_to_add = ["BienvenidaActivity", "ResultadosActivity"] + [f"Pregunta{i}Activity" for i in range(1, 11)]

# Insert before </application> if not already there
insertion_index = manifest_content.rfind("</application>")
if insertion_index != -1:
    xml_to_insert = ""
    for act in activities_to_add:
        if act not in manifest_content:
            xml_to_insert += f'\n        <activity android:name=".{(act)}" android:exported="false" />'
    
    new_manifest = manifest_content[:insertion_index] + xml_to_insert + "\n    " + manifest_content[insertion_index:]
    with open(manifest_path, 'w') as f:
        f.write(new_manifest)

