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
            self.drawString(54, 750, "Práctica 5 - Cuestionario 2.0 (SQLite & Multi-Activity) | 2º DAM B (PGL)")
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
        fontSize=16,
        leading=20,
        textColor=colors.HexColor("#1A237E"),
        spaceBefore=14,
        spaceAfter=8,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'Heading2_Custom',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=15,
        textColor=colors.HexColor("#3F51B5"),
        spaceBefore=10,
        spaceAfter=5,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'Body_Custom',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=13.5,
        textColor=colors.HexColor("#222222"),
        spaceAfter=5
    )

    code_style = ParagraphStyle(
        'Code_Custom',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=7.5,
        leading=9.5,
        textColor=colors.HexColor("#0D47A1"),
        backColor=colors.HexColor("#F5F5F5"),
        borderColor=colors.HexColor("#E0E0E0"),
        borderWidth=0.8,
        borderPadding=5,
        spaceBefore=5,
        spaceAfter=6
    )

    bullet_style = ParagraphStyle(
        'Bullet_Custom',
        parent=body_style,
        leftIndent=15,
        firstLineIndent=-10,
        spaceAfter=3
    )

    story = []

    # ==================== PORTADA ====================
    story.append(Spacer(1, 40))
    story.append(Paragraph("Práctica 5", title_style))
    story.append(Spacer(1, 10))
    story.append(Paragraph("Cuestionario 2.0 en Android (Kotlin)", subtitle_style))
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
        ["1. Descripción del Proyecto y Objetivos Técnicos", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "3"],
        ["2. Arquitectura del Sistema y Flujo de Navegación", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "3"],
        ["3. Diseño de la Capa de Presentación (Layouts XML)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "4"],
        ["    3.1 Pantalla de Login y Registro (activity_main.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "4"],
        ["    3.2 Pantalla de Bienvenida (activity_bienvenida.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "5"],
        ["    3.3 Plantilla Reutilizable de Pregunta (activity_pregunta.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "6"],
        ["    3.4 Pantalla de Resultados y Resumen (activity_resultados.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "7"],
        ["4. Implementación de la Lógica del Negocio (Kotlin)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "8"],
        ["    4.1 Capa de Persistencia Local (DatabaseHelper.kt)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "8"],
        ["    4.2 Controlador de Autenticación y Registro (MainActivity.kt)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "9"],
        ["    4.3 Control de Sesión y Bienvenida (BienvenidaActivity.kt)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "10"],
        ["    4.4 Controlador Secuencial de Preguntas (Pregunta1 a 10)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "11"],
        ["    4.5 Motor de Corrección y Récord (ResultadosActivity.kt)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "12"],
        ["5. Configuración del Manifiesto (AndroidManifest.xml)", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "13"],
        ["6. Matriz de Pruebas y Casos de Uso", ". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .", "14"],
    ]

    t_index = Table(index_data, colWidths=[240, 230, 34])
    t_index.setStyle(TableStyle([
        ('FONTNAME', (0,0), (-1,-1), 'Helvetica'),
        ('FONTSIZE', (0,0), (-1,-1), 9.5),
        ('TEXTCOLOR', (0,0), (-1,-1), colors.HexColor("#333333")),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('BOTTOMPADDING', (0,0), (-1,-1), 5),
    ]))
    story.append(t_index)
    story.append(PageBreak())

    # ==================== 1. DESCRIPCIÓN ====================
    story.append(Paragraph("1. Descripción del Proyecto y Objetivos Técnicos", h1_style))
    story.append(Paragraph(
        "Esta práctica consolida el desarrollo de aplicaciones nativas en Android utilizando <b>Kotlin</b>, "
        "con foco en la persistencia relacional local (<b>SQLite</b>), la navegación multi-actividad con intercambio "
        "de colecciones dinámicas de datos mediante <b>Intents</b>, y el control determinista de la pila de tareas "
        "(<b>Back Stack</b>).",
        body_style
    ))
    story.append(Paragraph("● <b>Persistencia Local Embebida:</b> Gestión de cuentas de usuario y récords históricos mediante <i>SQLiteOpenHelper</i> con restricciones de integridad (<i>UNIQUE</i>).", bullet_style))
    story.append(Paragraph("● <b>Flujo de Navegación Desacoplado:</b> Cadena lineal de 10 actividades de evaluación independientes con propagación de estado en memoria mediante un <i>ArrayList&lt;String&gt;</i>.", bullet_style))
    story.append(Paragraph("● <b>Optimización de Recursos:</b> Liberación inmediata de actividades mediante <i>finish()</i> y reseteo atómico de la pila tras el cierre de sesión con <i>FLAG_ACTIVITY_CLEAR_TASK</i>.", bullet_style))
    story.append(Spacer(1, 10))

    # ==================== 2. ARQUITECTURA ====================
    story.append(Paragraph("2. Arquitectura del Sistema y Flujo de Navegación", h1_style))
    story.append(Paragraph(
        "El sistema implementa una arquitectura secuencial y unidireccional para garantizar la integridad del examen:",
        body_style
    ))
    story.append(Paragraph("1. <b>MainActivity:</b> Autenticación o registro de credenciales contra la base de datos interna.", bullet_style))
    story.append(Paragraph("2. <b>BienvenidaActivity:</b> Presentación del saludo personalizado y recuperación del récord más alto histórico.", bullet_style))
    story.append(Paragraph("3. <b>Pregunta1Activity a Pregunta10Activity:</b> Diez pantallas consecutivas que validan la selección obligatoria en un <i>RadioGroup</i>, añaden la respuesta a la lista compartida y destruyen su propia instancia.", bullet_style))
    story.append(Paragraph("4. <b>ResultadosActivity:</b> Corrección frente al patrón de respuestas, actualización condicional en SQLite mediante sentencia <i>UPDATE</i> si se supera la calificación histórica, y opción de <i>Logout</i> seguro.", bullet_style))
    story.append(PageBreak())

    # ==================== 3. LAYOUTS ====================
    story.append(Paragraph("3. Diseño de la Capa de Presentación (Layouts XML)", h1_style))
    story.append(Paragraph("3.1 Pantalla de Login y Registro (activity_main.xml)", h2_style))

    code_main_xml = """<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <EditText
        android:id="@+id/etUsername"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:ems="10"
        android:inputType="text"
        android:hint="Usuario"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.35" />

    <EditText
        android:id="@+id/etPassword"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        android:ems="10"
        android:inputType="textPassword"
        android:hint="Contraseña"
        app:layout_constraintEnd_toEndOf="@+id/etUsername"
        app:layout_constraintStart_toStartOf="@+id/etUsername"
        app:layout_constraintTop_toBottomOf="@+id/etUsername" />

    <Button
        android:id="@+id/btnLogin"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="32dp"
        android:text="Login"
        app:layout_constraintEnd_toStartOf="@+id/btnRegister"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/etPassword" />

    <Button
        android:id="@+id/btnRegister"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Registro"
        app:layout_constraintBottom_toBottomOf="@+id/btnLogin"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.5"
        app:layout_constraintStart_toEndOf="@+id/btnLogin"
        app:layout_constraintTop_toTopOf="@+id/btnLogin" />

</androidx.constraintlayout.widget.ConstraintLayout>"""
    story.append(Preformatted(code_main_xml, code_style))
    story.append(Paragraph("● <b>ConstraintLayout:</b> Facilita un diseño plano y adaptable con alineación vertical del 35% para evitar colisiones con el teclado virtual.", bullet_style))
    story.append(Paragraph("● <b>inputType=\"textPassword\":</b> Activa la máscara de seguridad para caracteres privados.", bullet_style))
    story.append(Paragraph("● <b>Cadena Horizontal:</b> Botones equilibrados con sesgo horizontal simétrico (0.5).", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("3.2 Pantalla de Bienvenida (activity_bienvenida.xml)", h2_style))
    code_bienvenida_xml = """<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="32dp"
    android:gravity="center">

    <TextView
        android:id="@+id/tvSaludo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="¡Hola, usuario!"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/tvNotaMaxima"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Tu nota más alta hasta la fecha: 0"
        android:textSize="18sp"
        android:gravity="center"
        android:layout_marginBottom="48dp"/>

    <Button
        android:id="@+id/btnComenzar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Comenzar cuestionario"/>

</LinearLayout>"""
    story.append(Preformatted(code_bienvenida_xml, code_style))
    story.append(Paragraph("● <b>LinearLayout Centrado:</b> Alineación vertical y horizontal de tarjeta de bienvenida.", bullet_style))
    story.append(Paragraph("● <b>TextViews Dinámicos:</b> Vistas enlazadas con el nombre del usuario autenticado y su puntuación récord.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("3.3 Plantilla Reutilizable de Pregunta (activity_pregunta.xml)", h2_style))
    code_pregunta_xml = """<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp"
    android:gravity="center">

    <TextView
        android:id="@+id/tvPregunta"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="20sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="32dp"/>

    <RadioGroup
        android:id="@+id/rgOpciones"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="32dp">

        <RadioButton android:id="@+id/rbOp1" android:layout_width="match_parent"
            android:layout_height="wrap_content" android:textSize="18sp" android:layout_marginBottom="8dp"/>
        <RadioButton android:id="@+id/rbOp2" android:layout_width="match_parent"
            android:layout_height="wrap_content" android:textSize="18sp" android:layout_marginBottom="8dp"/>
        <RadioButton android:id="@+id/rbOp3" android:layout_width="match_parent"
            android:layout_height="wrap_content" android:textSize="18sp"/>
    </RadioGroup>

    <Button
        android:id="@+id/btnSiguiente"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Responder y avanzar a la siguiente pregunta"/>

</LinearLayout>"""
    story.append(Preformatted(code_pregunta_xml, code_style))
    story.append(Paragraph("● <b>Reutilización de Recursos:</b> Este layout único es instanciado por las diez clases de preguntas.", bullet_style))
    story.append(Paragraph("● <b>RadioGroup:</b> Asegura exclusión mutua estricta, impidiendo selecciones múltiples simultáneas.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("3.4 Pantalla de Resultados y Resumen (activity_resultados.xml)", h2_style))
    code_resultados_xml = """<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView android:layout_width="match_parent" android:layout_height="wrap_content"
        android:text="Resultados del Cuestionario" android:textSize="24sp" android:textStyle="bold"
        android:gravity="center" android:layout_marginBottom="16dp"/>

    <TextView android:id="@+id/tvNota" android:layout_width="match_parent"
        android:layout_height="wrap_content" android:textSize="20sp" android:gravity="center"
        android:layout_marginBottom="16dp"/>

    <TextView android:id="@+id/tvRecord" android:layout_width="match_parent"
        android:layout_height="wrap_content" android:textSize="18sp" android:textStyle="bold"
        android:textColor="#4CAF50" android:gravity="center" android:layout_marginBottom="16dp"/>

    <ScrollView android:layout_width="match_parent" android:layout_height="0dp"
        android:layout_weight="1" android:layout_marginBottom="16dp">
        <TextView android:id="@+id/tvResumen" android:layout_width="match_parent"
            android:layout_height="wrap_content" android:textSize="16sp"/>
    </ScrollView>

    <Button android:id="@+id/btnLogout" android:layout_width="match_parent"
        android:layout_height="wrap_content" android:text="Logout (Volver al Login)"/>

</LinearLayout>"""
    story.append(Preformatted(code_resultados_xml, code_style))
    story.append(Paragraph("● <b>ScrollView con layout_weight=\"1\":</b> Distribución dinámica del área restante para permitir desplazamiento fluido del informe completo sin alterar las cabeceras ni el botón de Logout.", bullet_style))
    story.append(PageBreak())

    # ==================== 4. KOTLIN ====================
    story.append(Paragraph("4. Implementación de la Lógica del Negocio (Kotlin)", h1_style))
    story.append(Paragraph("4.1 Capa de Persistencia Local (DatabaseHelper.kt)", h2_style))
    code_db_kt = """class DatabaseHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int)
    : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table usuarios(" +
                   "id integer primary key autoincrement, " +
                   "username text unique, " +
                   "password text, " +
                   "nota_maxima integer default 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists usuarios")
        onCreate(db)
    }
}"""
    story.append(Preformatted(code_db_kt, code_style))
    story.append(Paragraph("● <b>SQLiteOpenHelper:</b> Administra la creación y actualización del esquema relacional interno.", bullet_style))
    story.append(Paragraph("● <b>Restricción UNIQUE:</b> Protege a nivel de base de datos contra cuentas con nombres duplicados.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("4.2 Controlador de Autenticación y Registro (MainActivity.kt)", h2_style))
    code_login_kt = """// Registro con inserción segura en ContentValues
val registro = ContentValues().apply {
    put("username", usuario)
    put("password", contra)
}
val resultado = bd.insert("usuarios", null, registro)
if (resultado != -1L) {
    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
} else {
    Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show()
}

// Login con consulta parametrizada y extracción de récord
val fila = bd.rawQuery("select id, nota_maxima from usuarios where username='${usuario}' and password='${contra}'", null)
if (fila.moveToFirst()) {
    val notaMax = fila.getInt(1)
    fila.close()
    bd.close()
    val intent = Intent(this, BienvenidaActivity::class.java).apply {
        putExtra("USERNAME", usuario)
        putExtra("NOTA_MAXIMA", notaMax)
    }
    startActivity(intent)
    finish()
}"""
    story.append(Preformatted(code_login_kt, code_style))
    story.append(Paragraph("● <b>Manejo de Inserción:</b> Uso de <i>ContentValues</i> y detección de colisión (-1L).", bullet_style))
    story.append(Paragraph("● <b>Consultas Cursor:</b> Apertura y lectura segura de tuplas con <i>moveToFirst()</i> y liberación con <i>close()</i>.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("4.3 Control de Sesión y Bienvenida (BienvenidaActivity.kt)", h2_style))
    code_bienvenida_kt = """val username = intent.getStringExtra("USERNAME") ?: "Usuario"
val notaMax = intent.getIntExtra("NOTA_MAXIMA", 0)

tvSaludo.text = "¡Bienvenido/a, $username!"
tvNotaMaxima.text = "Tu nota más alta hasta la fecha: $notaMax"

btnComenzar.setOnClickListener {
    val intent = Intent(this, Pregunta1Activity::class.java)
    intent.putExtra("USERNAME", username)
    intent.putStringArrayListExtra("ANSWERS", ArrayList())
    startActivity(intent)
    finish()
}"""
    story.append(Preformatted(code_bienvenida_kt, code_style))
    story.append(Paragraph("● <b>Inicialización de Buffer:</b> Instanciación del <i>ArrayList&lt;String&gt;</i> inicial como acumulador de respuestas.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("4.4 Controlador Secuencial de Preguntas (Pregunta1 a 10)", h2_style))
    code_preg_kt = """btnSiguiente.setOnClickListener {
    val selectedId = rgOpciones.checkedRadioButtonId
    if (selectedId == -1) {
        Toast.makeText(this, "Por favor, selecciona una respuesta", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }

    val rbSeleccionado = findViewById<RadioButton>(selectedId)
    val respuestaTexto = rbSeleccionado.text.toString()

    val username = intent.getStringExtra("USERNAME") ?: ""
    val respuestas = intent.getStringArrayListExtra("ANSWERS") ?: ArrayList()
    respuestas.add(respuestaTexto)

    val intent = Intent(this, Pregunta2Activity::class.java)
    intent.putExtra("USERNAME", username)
    intent.putStringArrayListExtra("ANSWERS", respuestas)
    startActivity(intent)
    finish()
}"""
    story.append(Preformatted(code_preg_kt, code_style))
    story.append(Paragraph("● <b>Validación Defensiva:</b> Comprobación de selección no nula mediante <i>checkedRadioButtonId</i>.", bullet_style))
    story.append(Paragraph("● <b>Propagación en Memoria:</b> Enlace y paso secuencial del array de respuestas hacia la siguiente actividad.", bullet_style))
    story.append(PageBreak())

    story.append(Paragraph("4.5 Motor de Corrección y Récord (ResultadosActivity.kt)", h2_style))
    code_res_kt = """// Corrección iterativa
for (i in correctAnswers.indices) {
    val userAnswer = if (i < userAnswers.size) userAnswers[i] else "No respondida"
    if (userAnswer == correctAnswers[i]) {
        aciertos++
        resumenBuilder.append("Pregunta ${i + 1}: Correcta ($userAnswer)\\n")
    } else {
        resumenBuilder.append("Pregunta ${i + 1}: Incorrecta. Tuya: $userAnswer. Correcta: ${correctAnswers[i]}\\n")
    }
}

// Actualización condicional en SQLite
if (nuevaNota > notaMaximaAnterior) {
    tvRecord.text = "¡Felicidades! Has superado tu calificación más alta."
    val registro = ContentValues().apply { put("nota_maxima", nuevaNota) }
    bd.update("usuarios", registro, "username='$username'", null)
}

// Logout con limpieza de Back Stack
btnLogout.setOnClickListener {
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}"""
    story.append(Preformatted(code_res_kt, code_style))
    story.append(Paragraph("● <b>Algoritmo de Corrección:</b> Comparación lineal contra el patrón de respuestas y renderizado dinámico en <i>ScrollView</i>.", bullet_style))
    story.append(Paragraph("● <b>Persistencia Condicional:</b> Sentencia <i>UPDATE</i> ejecutada exclusivamente al superar el récord registrado.", bullet_style))
    story.append(Paragraph("● <b>Limpieza de Pila:</b> Empleo de <i>FLAG_ACTIVITY_CLEAR_TASK</i> para reiniciar el estado de la sesión.", bullet_style))
    story.append(PageBreak())

    # ==================== 5. MANIFEST ====================
    story.append(Paragraph("5. Configuración del Manifiesto (AndroidManifest.xml)", h1_style))
    code_manifest = """<application ...>
    <activity
        android:name=".MainActivity"
        android:exported="true"
        android:windowSoftInputMode="adjustResize">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>

    <activity android:name=".BienvenidaActivity" android:exported="false" />
    <activity android:name=".ResultadosActivity" android:exported="false" />
    <activity android:name=".Pregunta1Activity" android:exported="false" />
    <!-- Pregunta2Activity a Pregunta10Activity android:exported="false" -->
</application>"""
    story.append(Preformatted(code_manifest, code_style))
    story.append(Paragraph("● <b>Seguridad (android:exported=\"false\"):</b> Protección de actividades internas contra invocaciones externas no autorizadas.", bullet_style))
    story.append(Paragraph("● <b>adjustResize:</b> Ajuste suave del layout ante la apertura del teclado virtual.", bullet_style))
    story.append(PageBreak())

    # ==================== 6. PRUEBAS ====================
    story.append(Paragraph("6. Matriz de Pruebas y Casos de Uso", h1_style))

    test_data = [
        ["ID", "Caso de Prueba", "Acción Realizada", "Resultado Esperado", "Estado"],
        ["CP-01", "Registro nuevo", "Usuario 'dani', Pass '123'", "Insert en SQLite. Toast de éxito.", "OK"],
        ["CP-02", "Registro duplicado", "Usuario existente 'dani'", "Violación UNIQUE (-1L). Mensaje de error.", "OK"],
        ["CP-03", "Login inválido", "Credenciales incorrectas", "Cursor vacío. Toast de credenciales erróneas.", "OK"],
        ["CP-04", "Login válido", "Credenciales correctas", "Abre Bienvenida y muestra récord previo.", "OK"],
        ["CP-05", "Validación vacía", "Avanzar sin responder", "checkedRadioButtonId == -1. Bloqueo y aviso.", "OK"],
        ["CP-06", "Flujo preguntas", "Avanzar de 1 a 10", "Acumulación en ArrayList. Liberación con finish().", "OK"],
        ["CP-07", "Superar récord", "Aciertos > Récord previo", "Mensaje de felicitación. UPDATE en SQLite.", "OK"],
        ["CP-08", "No superar récord", "Aciertos <= Récord", "Mensaje de no superación. Récord intacto.", "OK"],
        ["CP-09", "Cierre de sesión", "Pulsar Logout", "CLEAR_TASK. Vuelve a Login sin retorno posible.", "OK"]
    ]

    t_tests = Table(test_data, colWidths=[38, 92, 120, 204, 38])
    t_tests.setStyle(TableStyle([
        ('FONTNAME', (0,0), (-1,0), 'Helvetica-Bold'),
        ('FONTSIZE', (0,0), (-1,-1), 8),
        ('BACKGROUND', (0,0), (-1,0), colors.HexColor("#1A237E")),
        ('TEXTCOLOR', (0,0), (-1,0), colors.white),
        ('ALIGN', (0,0), (-1,-1), 'LEFT'),
        ('ALIGN', (0,0), (0,-1), 'CENTER'),
        ('ALIGN', (-1,0), (-1,-1), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CCCCCC")),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, colors.HexColor("#F9F9F9")]),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
        ('TOPPADDING', (0,0), (-1,-1), 4),
        ('BOTTOMPADDING', (0,0), (-1,-1), 4),
    ]))
    story.append(t_tests)

    doc.build(story, canvasmaker=NumberedCanvas)
    print("PDF generated successfully at:", filename)

if __name__ == '__main__':
    artifact_path = '/home/danoglez/Documentos/2 DAM/PGL/Prctica5/Práctica 5 - Cuestionario 2.0.pdf'
    build_pdf(artifact_path)
    downloads_path = '/home/danoglez/Descargas/Práctica 5 - Cuestionario 2.0.pdf'
    build_pdf(downloads_path)
