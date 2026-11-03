import os
from reportlab.lib.pagesizes import letter
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, Preformatted
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_number(num_pages)
            super().showPage()
        super().save()

    def draw_page_number(self, page_count):
        self.saveState()
        self.setFont("Helvetica", 9)
        self.setFillColor(colors.HexColor("#555555"))
        # Header (pages > 1)
        if self._pageNumber > 1:
            self.drawString(54, 750, "Práctica 3 - Múltiples Activities | 2º DAM B (PGL)")
            self.setStrokeColor(colors.HexColor("#CCCCCC"))
            self.setLineWidth(0.5)
            self.line(54, 742, 558, 742)

        # Footer (all pages)
        footer_text = f"Francisco Daniel Darias González         2ºDAMB         PGL                                                                                 {self._pageNumber}"
        self.drawString(54, 36, footer_text)
        self.restoreState()

def build_pdf(filename):
    doc = SimpleDocTemplate(
        filename,
        pagesize=letter,
        leftMargin=54,
        rightMargin=54,
        topMargin=72,
        bottomMargin=54
    )

    styles = getSampleStyleSheet()

    # Custom styles
    title_style = ParagraphStyle(
        'CoverTitle',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=28,
        leading=34,
        alignment=1, # Center
        textColor=colors.HexColor("#1A237E")
    )

    subtitle_style = ParagraphStyle(
        'CoverSubtitle',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=18,
        leading=22,
        alignment=1,
        textColor=colors.HexColor("#3F51B5")
    )

    meta_style = ParagraphStyle(
        'CoverMeta',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=12,
        leading=18,
        alignment=0,
        textColor=colors.HexColor("#333333")
    )

    h1_style = ParagraphStyle(
        'Heading1_Custom',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=18,
        leading=22,
        textColor=colors.HexColor("#1A237E"),
        spaceBefore=15,
        spaceAfter=10,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'Heading2_Custom',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=13,
        leading=16,
        textColor=colors.HexColor("#3F51B5"),
        spaceBefore=12,
        spaceAfter=6,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body_Custom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=10,
        leading=14,
        textColor=colors.HexColor("#222222"),
        spaceAfter=6
    )

    code_style = ParagraphStyle(
        'Code_Custom',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=8.5,
        leading=11,
        textColor=colors.HexColor("#0D47A1"),
        backColor=colors.HexColor("#F5F5F5"),
        borderColor=colors.HexColor("#E0E0E0"),
        borderWidth=1,
        borderPadding=6,
        spaceBefore=6,
        spaceAfter=8
    )

    bullet_style = ParagraphStyle(
        'Bullet_Custom',
        parent=body_style,
        leftIndent=15,
        firstLineIndent=-10,
        spaceAfter=4
    )

    story = []

    # ==================== PORTADA ====================
    story.append(Spacer(1, 40))
    story.append(Paragraph("Práctica 3", title_style))
    story.append(Spacer(1, 10))
    story.append(Paragraph("Múltiples Activities y Cifrado", subtitle_style))
    story.append(Spacer(1, 120))

    meta_text = """
    <b>Asignatura:</b> Programación Multimedia y Dispositivos Móviles (PGL)<br/><br/>
    <b>Centro:</b> CIFP César Manrique<br/><br/>
    <b>Curso / Grupo:</b> 2º DAM B<br/><br/>
    <b>Alumno:</b> Francisco Daniel Darias González<br/><br/>
    <b>Fecha:</b> 2026<br/>
    """

    story.append(Paragraph(meta_text, meta_style))
    story.append(PageBreak())

    # ==================== ÍNDICE ====================
    story.append(Paragraph("Índice", h1_style))
    story.append(Spacer(1, 10))

    index_data = [
        ["1. Descripción de la Actividad", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "3"],
        ["2. Diseño de la Interfaz (Layouts XML)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "4"],
        ["    2.1 Contenedor Principal y Vistas (activity_main.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "4"],
        ["    2.2 Layout de Resultados (activity_resultado.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "5"],
        ["3. Implementación de la Lógica (Kotlin)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "6"],
        ["    3.1 Actividad Principal (MainActivity.kt)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "6"],
        ["    3.2 Segunda Actividad y Algoritmos (ResultadoActivity.kt)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "8"],
        ["4. Configuración del Manifiesto (AndroidManifest.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "10"],
        ["5. Visualización y Funcionamiento", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "11"],
    ]

    t_index = Table(index_data, colWidths=[240, 230, 34])
    t_index.setStyle(TableStyle([
        ('FONTNAME', (0,0), (-1,-1), 'Helvetica'),
        ('FONTSIZE', (0,0), (-1,-1), 10),
        ('TEXTCOLOR', (0,0), (-1,-1), colors.HexColor("#333333")),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('BOTTOMPADDING', (0,0), (-1,-1), 6),
    ]))
    story.append(t_index)
    story.append(PageBreak())

    # ==================== 1. DESCRIPCIÓN ====================
    story.append(Paragraph("1. Descripción de la Actividad", h1_style))
    story.append(Paragraph(
        "En esta práctica se profundiza en el uso de múltiples actividades (Activities) dentro de una aplicación Android, "
        "así como en el intercambio de información entre ellas utilizando objetos de la clase <b>Intent</b> mediante métodos como <i>putExtra</i> y <i>intent.extras</i>.",
        body_style
    ))
    story.append(Paragraph(
        "La aplicación consta de una actividad principal (<b>MainActivity</b>) que permite al usuario introducir un texto, "
        "seleccionar un método de cifrado mediante un control <b>Spinner</b> (César +3, Invertir texto o ROT13), y elegir entre "
        "dos acciones: <b>ENCRIPTAR</b> o <b>DESENCRIPTAR</b>.",
        body_style
    ))
    story.append(Paragraph(
        "Al pulsar cualquiera de los dos botones, la aplicación navega hacia una segunda actividad (<b>ResultadoActivity</b>), "
        "la cual procesa el texto recibido según el algoritmo seleccionado y muestra el resultado en pantalla. "
        "Asimismo, incluye un botón para volver fácilmente a la pantalla principal liberando los recursos mediante <i>finish()</i>.",
        body_style
    ))
    story.append(Spacer(1, 15))

    # ==================== 2. LAYOUTS ====================
    story.append(Paragraph("2. Diseño de la Interfaz (Layouts XML)", h1_style))
    story.append(Paragraph("2.1 Contenedor Principal y Vistas (activity_main.xml)", h2_style))

    code_main_xml = """<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/tvTitulo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Práctica 3 - Múltiples Activities"
        android:textSize="20sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="32dp" />

    <EditText
        android:id="@+id/etTexto"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="Ingrese el texto a procesar"
        android:inputType="text"
        android:autofillHints="text"
        app:layout_constraintTop_toBottomOf="@id/tvTitulo"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="32dp" />

    <TextView
        android:id="@+id/tvSpinnerLabel"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Seleccione método de cifrado:"
        app:layout_constraintTop_toBottomOf="@id/etTexto"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="24dp" />

    <Spinner
        android:id="@+id/spinnerMetodo"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toBottomOf="@id/tvSpinnerLabel"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp" />

    <Button
        android:id="@+id/btnEncriptar"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="ENCRIPTAR"
        app:layout_constraintTop_toBottomOf="@id/spinnerMetodo"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="32dp" />

    <Button
        android:id="@+id/btnDesencriptar"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="DESENCRIPTAR"
        app:layout_constraintTop_toBottomOf="@id/btnEncriptar"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp" />

</androidx.constraintlayout.widget.ConstraintLayout>"""

    story.append(Preformatted(code_main_xml, code_style))
    story.append(Spacer(1, 8))

    story.append(Paragraph("<b>Explicación de los componentes de activity_main.xml:</b>", body_style))
    story.append(Paragraph("● <b>ConstraintLayout:</b> Contenedor raíz que organiza los elementos mediante restricciones relativas.", bullet_style))
    story.append(Paragraph("● <b>EditText (etTexto):</b> Caja de texto donde el usuario introduce el mensaje a cifrar o descifrar.", bullet_style))
    story.append(Paragraph("● <b>Spinner (spinnerMetodo):</b> Control desplegable que permite elegir entre los 3 algoritmos disponibles.", bullet_style))
    story.append(Paragraph("● <b>Botones (btnEncriptar / btnDesencriptar):</b> Accionan el lanzamiento de la segunda actividad enviando los datos.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("2.2 Layout de Resultados (activity_resultado.xml)", h2_style))

    code_res_xml = """<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="16dp"
    tools:context=".ResultadoActivity">

    <TextView
        android:id="@+id/tvResultadoTitulo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Resultado de la Operación"
        android:textSize="22sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="32dp" />

    <TextView
        android:id="@+id/tvMetodoUsado"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Método: "
        android:textSize="16sp"
        app:layout_constraintTop_toBottomOf="@id/tvResultadoTitulo"
        app:layout_constraintStart_toStartOf="parent"
        android:layout_marginTop="24dp" />

    <TextView
        android:id="@+id/tvResultado"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="Texto procesado..."
        android:textSize="18sp"
        android:background="#E0E0E0"
        android:padding="16dp"
        app:layout_constraintTop_toBottomOf="@id/tvMetodoUsado"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp" />

    <Button
        android:id="@+id/btnVolver"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="VOLVER"
        app:layout_constraintTop_toBottomOf="@id/tvResultado"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="32dp" />

</androidx.constraintlayout.widget.ConstraintLayout>"""

    story.append(Preformatted(code_res_xml, code_style))
    story.append(Spacer(1, 8))

    story.append(Paragraph("<b>Explicación de los componentes de activity_resultado.xml:</b>", body_style))
    story.append(Paragraph("● <b>tvResultadoTitulo:</b> Encabezado que indica la sección de resultados.", bullet_style))
    story.append(Paragraph("● <b>tvMetodoUsado:</b> Muestra el método de cifrado seleccionado y si se trató de una encriptación o desencriptación.", bullet_style))
    story.append(Paragraph("● <b>tvResultado:</b> Caja de texto con fondo gris suave donde se expone el resultado del algoritmo aplicado.", bullet_style))
    story.append(Paragraph("● <b>btnVolver:</b> Botón para cerrar la actividad actual y regresar a la pantalla principal.", bullet_style))
    story.append(PageBreak())

    # ==================== 3. KOTLIN ====================
    story.append(Paragraph("3. Implementación de la Lógica (Kotlin)", h1_style))
    story.append(Paragraph("3.1 Actividad Principal (MainActivity.kt)", h2_style))

    code_main_kt = """package dev.danoglez.prctica3

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etTexto = findViewById<EditText>(R.id.etTexto)
        val spinnerMetodo = findViewById<Spinner>(R.id.spinnerMetodo)
        val btnEncriptar = findViewById<Button>(R.id.btnEncriptar)
        val btnDesencriptar = findViewById<Button>(R.id.btnDesencriptar)

        // Configuración del Spinner con los 3 métodos de cifrado
        val metodos = arrayOf("César (+3)", "Invertir texto", "ROT13")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, metodos)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMetodo.adapter = adaptador

        btnEncriptar.setOnClickListener {
            val texto = etTexto.text.toString()
            if (texto.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un texto", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ResultadoActivity::class.java).apply {
                    putExtra("texto", texto)
                    putExtra("metodo", spinnerMetodo.selectedItem.toString())
                    putExtra("operacion", "encriptar")
                }
                startActivity(intent)
            }
        }

        btnDesencriptar.setOnClickListener {
            val texto = etTexto.text.toString()
            if (texto.isEmpty()) {
                Toast.makeText(this, "Por favor, introduce un texto", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, ResultadoActivity::class.java).apply {
                    putExtra("texto", texto)
                    putExtra("metodo", spinnerMetodo.selectedItem.toString())
                    putExtra("operacion", "desencriptar")
                }
                startActivity(intent)
            }
        }
    }
}"""

    story.append(Preformatted(code_main_kt, code_style))
    story.append(Spacer(1, 8))

    story.append(Paragraph("<b>Explicación de MainActivity.kt:</b>", body_style))
    story.append(Paragraph("● <b>Configuración del Spinner:</b> Se carga un array de strings con los 3 algoritmos y se vincula mediante un <i>ArrayAdapter</i>.", bullet_style))
    story.append(Paragraph("● <b>Validación de campos:</b> Comprueba que el EditText no esté vacío antes de continuar; de lo contrario, muestra un mensaje Toast.", bullet_style))
    story.append(Paragraph("● <b>Paso de parámetros (Intent):</b> Crea un objeto <i>Intent</i> indicando el destino (<i>ResultadoActivity::class.java</i>) y adjunta el texto, método y operación con <i>putExtra</i>.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("3.2 Segunda Actividad y Algoritmos (ResultadoActivity.kt)", h2_style))

    code_res_kt = """package dev.danoglez.prctica3

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        val tvMetodoUsado = findViewById<TextView>(R.id.tvMetodoUsado)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val btnVolver = findViewById<Button>(R.id.btnVolver)

        val bundle = intent.extras
        val texto = bundle?.getString("texto") ?: ""
        val metodo = bundle?.getString("metodo") ?: "César (+3)"
        val operacion = bundle?.getString("operacion") ?: "encriptar"

        val resultadoProcesado = when (metodo) {
            "César (+3)" -> cifradoCesar(texto, operacion == "encriptar")
            "Invertir texto" -> texto.reversed()
            "ROT13" -> rot13(texto)
            else -> texto
        }

        tvMetodoUsado.text = "Método: $metodo (${operacion.uppercase()})"
        tvResultado.text = resultadoProcesado

        btnVolver.setOnClickListener {
            finish()
        }
    }

    private fun cifradoCesar(texto: String, encriptar: Boolean): String {
        val shift = if (encriptar) 3 else -3
        val sb = StringBuilder()
        for (char in texto) {
            if (char.isLetter()) {
                val base = if (char.isLowerCase()) 'a' else 'A'
                val shifted = ('a'.code + (char.code - base.code + shift + 26) % 26).toChar()
                sb.append(shifted)
            } else {
                sb.append(char)
            }
        }
        return sb.toString()
    }

    private fun rot13(texto: String): String {
        val sb = StringBuilder()
        for (char in texto) {
            sb.append(
                when (char) {
                    in 'a'..'m' -> char + 13
                    in 'n'..'z' -> char - 13
                    in 'A'..'M' -> char + 13
                    in 'N'..'Z' -> char - 13
                    else -> char
                }
            )
        }
        return sb.toString()
    }
}"""

    story.append(Preformatted(code_res_kt, code_style))
    story.append(Spacer(1, 8))

    story.append(Paragraph("<b>Explicación de ResultadoActivity.kt:</b>", body_style))
    story.append(Paragraph("● <b>Recuperación de Extras:</b> Lee el Bundle del Intent con <i>intent.extras</i> para obtener el texto, método y tipo de operación.", bullet_style))
    story.append(Paragraph("● <b>Selección de Algoritmo (when):</b> Ejecuta la función correspondiente (César, Inversión o ROT13).", bullet_style))
    story.append(Paragraph("● <b>Cifrado César y ROT13:</b> Algoritmos de sustitución que desplazan caracteres alfabéticos manteniendo mayúsculas y minúsculas.", bullet_style))
    story.append(Paragraph("● <b>Botón Volver (finish):</b> Destruye la actividad actual y regresa a la anterior de manera eficiente.", bullet_style))
    story.append(PageBreak())

    # ==================== 4. MANIFEST ====================
    story.append(Paragraph("4. Configuración del Manifiesto (AndroidManifest.xml)", h1_style))
    story.append(Paragraph(
        "Para que Android reconozca y permita la navegación hacia la segunda actividad, esta debe estar declarada dentro del archivo <b>AndroidManifest.xml</b>:",
        body_style
    ))

    code_manifest = """<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Práctica3">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <activity
            android:name=".ResultadoActivity"
            android:exported="false" />

    </application>

</manifest>"""

    story.append(Preformatted(code_manifest, code_style))
    story.append(Spacer(1, 8))
    story.append(Paragraph("● <b>&lt;activity android:name=\".ResultadoActivity\" /&gt;:</b> Registra la segunda actividad en la aplicación. No requiere filtro de intents por ser llamada explícitamente desde MainActivity.", bullet_style))
    story.append(Spacer(1, 20))

    # ==================== 5. VISUALIZACIÓN ====================
    story.append(Paragraph("5. Visualización y Funcionamiento", h1_style))
    story.append(Paragraph(
        "A continuación se describe el comportamiento de la aplicación durante su ejecución:",
        body_style
    ))
    story.append(Paragraph("1. <b>Pantalla Principal:</b> El usuario introduce un texto (por ejemplo, <i>\"Hola mundo\"</i>), selecciona el método deseado en el desplegable (ej. <i>César (+3)</i>) y pulsa <b>ENCRIPTAR</b> o <b>DESENCRIPTAR</b>.", bullet_style))
    story.append(Paragraph("2. <b>Pantalla de Resultados:</b> Se abre instantáneamente la segunda actividad, mostrando en la parte superior el método empleado y en la caja de texto central el resultado procesado (<i>\"Krod pxqgr\"</i>).", bullet_style))
    story.append(Paragraph("3. <b>Retorno:</b> Al pulsar el botón <b>VOLVER</b>, la actividad se cierra mediante <i>finish()</i>, regresando limpiamente a la pantalla principal.", bullet_style))

    doc.build(story, canvasmaker=NumberedCanvas)
    print("PDF generated successfully at:", filename)

if __name__ == '__main__':
    output_path = '/home/danoglez/Descargas/Práctica 3 - Múltiples Activities.pdf'
    build_pdf(output_path)
    # Also save a copy in project artifacts
    artifact_path = '/home/danoglez/Documentos/2 DAM/PGL/Prctica3/Práctica 3 - Múltiples Activities.pdf'
    build_pdf(artifact_path)
