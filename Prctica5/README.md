# Práctica 5 - Cuestionario 2.0 en Android (Kotlin)

**Asignatura:** Programación Multimedia y Dispositivos Móviles (PGL)  
**Centro Educativo:** CIFP César Manrique  
**Ciclo Formativo:** 2º Desarrollo de Aplicaciones Multiplataforma (DAM B)  
**Alumno:** Francisco Daniel Darias González  
**Entorno de Desarrollo:** Android Studio Iguana / Ladybug | Kotlin 1.9+ | Android SDK 34/35  

---

## Índice
1. [Descripción del Proyecto y Objetivos Técnicos](#1-descripción-del-proyecto-y-objetivos-técnicos)
2. [Arquitectura del Sistema y Flujo de Navegación](#2-arquitectura-del-sistema-y-flujo-de-navegación)
   * 2.1 Diagrama de Estados y Transición de Actividades
   * 2.2 Gestión de Memoria y Pila de Actividades (Back Stack)
3. [Diseño de la Capa de Presentación (Layouts XML)](#3-diseño-de-la-capa-de-presentación-layouts-xml)
   * 3.1 Pantalla de Login y Registro (`activity_main.xml`)
   * 3.2 Pantalla de Bienvenida (`activity_bienvenida.xml`)
   * 3.3 Plantilla Reutilizable de Pregunta (`activity_pregunta.xml`)
   * 3.4 Pantalla de Resultados y Resumen (`activity_resultados.xml`)
4. [Implementación de la Lógica del Negocio (Kotlin)](#4-implementación-de-la-lógica-del-negocio-kotlin)
   * 4.1 Capa de Persistencia Local (`DatabaseHelper.kt`)
   * 4.2 Controlador de Autenticación y Registro (`MainActivity.kt`)
   * 4.3 Control de Sesión y Bienvenida (`BienvenidaActivity.kt`)
   * 4.4 Controlador Secuencial de Preguntas (`Pregunta1Activity.kt` a `Pregunta10Activity.kt`)
   * 4.5 Motor de Corrección y Actualización de Récord (`ResultadosActivity.kt`)
5. [Configuración del Manifiesto (`AndroidManifest.xml`)](#5-configuración-del-manifiesto-androidmanifestxml)
6. [Matriz de Pruebas y Casos de Uso](#6-matriz-de-pruebas-y-casos-de-uso)
7. [Conclusiones y Competencias Técnicas Adquiridas](#7-conclusiones-y-competencias-técnicas-adquiridas)

---

## 1. Descripción del Proyecto y Objetivos Técnicos

El propósito fundamental de esta práctica consiste en diseñar e implementar una solución integral de software móvil en **Android (Kotlin)**, combinando persistencia relacional embebida mediante **SQLite**, control riguroso del ciclo de vida de componentes (**Activity Lifecycle**), navegación multicapa secuencial con transferencia de colecciones en memoria mediante **Intents**, y diseño de interfaces reactivas y adaptativas en **XML**.

### Objetivos Técnicos Principales:
*   **Persistencia Relacional Local:** Implementar una capa de base de datos relacional autónoma mediante la API nativa `SQLiteOpenHelper`, aplicando restricciones de integridad referencial (`UNIQUE`, `PRIMARY KEY AUTOINCREMENT`) y transacciones DML seguras (`insert`, `rawQuery`, `update`).
*   **Gestión del Ciclo de Vida y Back Stack:** Controlar de forma determinista la pila de navegación de Android, liberando memoria operativa tras cada transición (`finish()`) y manipulando flags de tarea (`FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK`) para invalidar el estado de sesión tras el cierre de cuenta (*Logout*).
*   **Transferencia de Estado entre Actividades:** Diseñar un pipeline de propagación de respuestas desacoplado donde un buffer dinámico (`ArrayList<String>`) viaje a través de una cadena de diez actividades independientes de evaluación sin persistencia temporal en disco, optimizando la latencia de entrada/salida.
*   **Reutilización de Layouts y Restricción de Selección:** Implementar un patrón de vista única compartida (`activity_pregunta.xml`) con lógica de exclusión mutua mediante `RadioGroup` y validaciones estrictas en tiempo de ejecución para evitar avances sin selección.
*   **Algoritmia de Evaluación y Actualización Condicional:** Contrastar el vector de respuestas del usuario frente a una matriz de referencia estática, computar métricas de acierto/fallo y persistir condicionalmente la nueva calificación si y solo si supera el récord histórico almacenado.

---

## 2. Arquitectura del Sistema y Flujo de Navegación

### 2.1 Diagrama de Estados y Transición de Actividades

El flujo de ejecución sigue una arquitectura lineal desacoplada con retorno seguro al punto de autenticación inicial:

```text
+------------------+        Credenciales Válidas         +----------------------+
|   MainActivity   | ----------------------------------> |  BienvenidaActivity  |
| (Login/Registro) | <---------------------------------+ |  (Récord Histórico)  |
+------------------+             Logout                  +----------------------+
                                (Clear Task)                         |
                                                                     | Inicializa Buffer
                                                                     v ("ANSWERS")
+----------------------+        putExtra("ANSWERS")      +----------------------+
|  Pregunta1Activity   | ------------------------------> |  Pregunta2Activity   |
+----------------------+                                 +----------------------+
           |                                                         |
           v                                                         v
         [...]                                                     [...]
           |                                                         |
+----------------------+        putExtra("ANSWERS")      +----------------------+
|  Pregunta10Activity  | ------------------------------> |  ResultadosActivity  |
+----------------------+                                 | (Evaluación & UPDATE)|
                                                         +----------------------+
```

### 2.2 Gestión de Memoria y Pila de Actividades (Back Stack)

Un error habitual en aplicaciones multi-actividad con preguntas consecutivas es permitir la acumulación de vistas en el Back Stack. Si un usuario avanzara de la pregunta 1 a la 10 sin destruir las actividades previas, el sistema mantendría 10 instancias completas en memoria RAM, permitiendo al usuario volver atrás y alterar respuestas ya emitidas.

Para resolver este desafío arquitectónico se aplican dos técnicas clave:
1.  **Destrucción Inmediata (`finish()`):** Al responder cada pregunta, tras empaquetar el `ArrayList<String>` en el `Intent`, se invoca inmediatamente `finish()`. Esto remueve la actividad emisora de la pila de tareas, liberando sus recursos gráficos y garantizando que la tecla "Atrás" del dispositivo no permita regresar a preguntas ya contestadas.
2.  **Reinicio Atómico del Back Stack en Logout:** Al pulsar el botón de Logout en `ResultadosActivity`, se configuran las banderas `FLAG_ACTIVITY_NEW_TASK` y `FLAG_ACTIVITY_CLEAR_TASK`. Esto destruye cualquier actividad remanente en la tarea actual y recrea `MainActivity` como la raíz limpia de una nueva tarea, impidiendo cualquier vulnerabilidad de sesión.

---

## 3. Diseño de la Capa de Presentación (Layouts XML)

### 3.1 Pantalla de Login y Registro (`activity_main.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

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

</androidx.constraintlayout.widget.ConstraintLayout>
```

#### Análisis Técnico de Componentes:
*   **`ConstraintLayout`:** Contenedor raíz plano y de alto rendimiento que elimina el anidamiento innecesario de vistas. Mediante `app:layout_constraintVertical_bias="0.35"`, sitúa el bloque de credenciales en el tercio superior de la pantalla, evitando solapamientos cuando el teclado virtual del sistema se despliega.
*   **`EditText` (`etUsername` / `etPassword`):** Cajas de entrada de texto parametrizadas con `android:ems="10"` para asegurar una anchura homogénea. El campo de contraseña implementa `android:inputType="textPassword"`, lo que activa la transformación nativa de caracteres en máscara de seguridad (`TransformationMethod`) e inhabilita sugerencias de texto predictivo del teclado.
*   **Distribución en Cadena Horizontal (Horizontal Chain):** Los botones `btnLogin` y `btnRegister` están vinculados mutuamente con restricciones horizontales bidireccionales y `app:layout_constraintHorizontal_bias="0.5"`, formando una cadena equilibrada que distribuye el espacio horizontal de forma idéntica en cualquier densidad de píxeles (DPI) o tamaño de pantalla.

---

### 3.2 Pantalla de Bienvenida (`activity_bienvenida.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
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

</LinearLayout>
```

#### Análisis Técnico de Componentes:
*   **`LinearLayout` (Vertical):** Contenedor idóneo para layouts de tarjeta o presentación con disposición unidireccional. La propiedad `android:gravity="center"` garantiza la alineación geométrica axial (vertical y horizontal) de todo el conjunto hijo.
*   **Jerarquía Tipográfica (`TextView`):** Se establece una separación clara de jerarquía visual utilizando `24sp` en negrita (`bold`) para la bienvenida del usuario y `18sp` para la métrica del récord histórico, proporcionando una lectura clara y accesible.
*   **`Button` (`btnComenzar`):** Elemento de acción central que inicia el cuestionario y dispara la instanciación del buffer en memoria.

---

### 3.3 Plantilla Reutilizable de Pregunta (`activity_pregunta.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp"
    android:gravity="center">

    <TextView
        android:id="@+id/tvPregunta"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Pregunta..."
        android:textSize="20sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="32dp"/>

    <RadioGroup
        android:id="@+id/rgOpciones"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="32dp">

        <RadioButton
            android:id="@+id/rbOp1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="18sp"
            android:text="Opcion 1"
            android:layout_marginBottom="8dp"/>

        <RadioButton
            android:id="@+id/rbOp2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="18sp"
            android:text="Opcion 2"
            android:layout_marginBottom="8dp"/>

        <RadioButton
            android:id="@+id/rbOp3"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="18sp"
            android:text="Opcion 3"/>
    </RadioGroup>

    <Button
        android:id="@+id/btnSiguiente"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Responder y avanzar a la siguiente pregunta"/>

</LinearLayout>
```

#### Análisis Técnico de Componentes:
*   **Patrón de Reutilización Estructural:** Este único archivo de layout es reutilizado de forma idéntica por las diez actividades (`Pregunta1Activity` a `Pregunta10Activity`), eliminando redundancia en el árbol de recursos de la aplicación (`res/layout`) y reduciendo significativamente el tamaño del paquete APK final.
*   **`RadioGroup` y Exclusión Mutua:** Actúa como controlador de estado exclusivo para sus elementos `RadioButton` secundarios. Gestiona internamente la deselección automática del resto de botones al marcar uno nuevo, exponiendo la propiedad `checkedRadioButtonId` para una verificación O(1) de la selección del usuario.
*   **`Button` (`btnSiguiente`):** Dimensionado con `android:layout_width="match_parent"` para ofrecer un área táctil amplia (*touch target*) según las directrices de accesibilidad de Material Design.

---

### 3.4 Pantalla de Resultados y Resumen (`activity_resultados.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Resultados del Cuestionario"
        android:textSize="24sp"
        android:textStyle="bold"
        android:gravity="center"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/tvNota"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Aciertos: X\nNota: Y/10"
        android:textSize="20sp"
        android:gravity="center"
        android:layout_marginBottom="16dp"/>
        
    <TextView
        android:id="@+id/tvRecord"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text=""
        android:textSize="18sp"
        android:textStyle="bold"
        android:textColor="#4CAF50"
        android:gravity="center"
        android:layout_marginBottom="16dp"/>

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:layout_marginBottom="16dp">
        
        <TextView
            android:id="@+id/tvResumen"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Resumen..."
            android:textSize="16sp"/>
    </ScrollView>

    <Button
        android:id="@+id/btnLogout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Logout (Volver al Login)"/>

</LinearLayout>
```

#### Análisis Técnico de Componentes:
*   **Gestión de Espacio mediante `ScrollView` y `layout_weight`:** La inclusión de `android:layout_height="0dp"` combinada con `android:layout_weight="1"` en el `ScrollView` indica al motor de renderizado de Android que asigne todo el espacio vertical sobrante a la lista desplazable de correcciones, fijando las cabeceras superiores y el botón inferior `btnLogout` en posiciones estáticas no desplazables.
*   **Retroalimentación Visual (`tvRecord`):** Configurado con color verde semántico (`#4CAF50`) y tipografía negrita para destacar el feedback de éxito al batir el récord histórico.
*   **Contención de Vista Desplazable:** El `ScrollView` alberga un único `TextView` (`tvResumen`) que recibe la construcción agregada del informe de aciertos y fallos de las 10 preguntas.

---

## 4. Implementación de la Lógica del Negocio (Kotlin)

### 4.1 Capa de Persistencia Local (`DatabaseHelper.kt`)

```kotlin
package dev.danoglez.prctica5

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(
    context: Context,
    name: String,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table usuarios(" +
            "id integer primary key autoincrement, " +
            "username text unique, " +
            "password text, " +
            "nota_maxima integer default 0)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists usuarios")
        onCreate(db)
    }
}
```

#### Explicación Técnica y Patrones de Persistencia:
*   **Herencia de `SQLiteOpenHelper`:** Encapsula la creación, inicialización y control de versiones del esquema de base de datos embebido en el almacenamiento interno privado de la aplicación (`/data/data/dev.danoglez.prctica5/databases/CuestionarioBD`).
*   **Diseño DDL y Restricciones de Integridad:**
    *   `id INTEGER PRIMARY KEY AUTOINCREMENT`: Garantiza un identificador único surrogate para cada tupla.
    *   `username TEXT UNIQUE`: Aplica a nivel de motor de base de datos una restricción de unicidad estricta. Cualquier intento de registrar un nombre ya existente provocará una violación de restricción que será capturada de manera no bloqueante.
    *   `nota_maxima INTEGER DEFAULT 0`: Inicializa de forma predeterminada el récord de cualquier usuario nuevo en 0 puntos.
*   **Estrategia de Migración (`onUpgrade`):** Define la política destructiva estándar de recreación de esquema ante incrementos en el entero `version`.

---

### 4.2 Controlador de Autenticación y Registro (`MainActivity.kt`)

```kotlin
package dev.danoglez.prctica5

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        // LÓGICA DE REGISTRO
        btnRegister.setOnClickListener {
            val admin = DatabaseHelper(this, "CuestionarioBD", null, 1)
            val bd = admin.writableDatabase
            val usuario = etUsername.text.toString()
            val contra = etPassword.text.toString()

            if (usuario.isNotEmpty() && contra.isNotEmpty()) {
                val registro = ContentValues()
                registro.put("username", usuario)
                registro.put("password", contra)
                
                val resultado = bd.insert("usuarios", null, registro)
                bd.close()
                
                if (resultado != -1L) {
                    Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // LÓGICA DE INICIO DE SESIÓN (LOGIN)
        btnLogin.setOnClickListener {
            val admin = DatabaseHelper(this, "CuestionarioBD", null, 1)
            val bd = admin.readableDatabase
            val usuario = etUsername.text.toString()
            val contra = etPassword.text.toString()

            if (usuario.isNotEmpty() && contra.isNotEmpty()) {
                val fila = bd.rawQuery(
                    "select id, nota_maxima from usuarios where username='${usuario}' and password='${contra}'",
                    null
                )
                if (fila.moveToFirst()) {
                    val notaMax = fila.getInt(1)
                    fila.close()
                    bd.close()
                    
                    val intent = Intent(this, BienvenidaActivity::class.java)
                    intent.putExtra("USERNAME", usuario)
                    intent.putExtra("NOTA_MAXIMA", notaMax)
                    startActivity(intent)
                    finish()
                } else {
                    fila.close()
                    bd.close()
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```

#### Explicación Técnica de la Operatoria:
*   **Transacciones DML con `ContentValues`:** En el flujo de registro, se utiliza la abstracción tipada de mapa `ContentValues` junto con `bd.insert()`. Este método devuelve el identificador de fila insertado o `-1L` si la restricción `UNIQUE` rechaza la tupla. Esto permite desacoplar el manejo de errores de colisión sin requerir bloques manuales de interrupción.
*   **Consultas de Lectura con `Cursor`:** En el flujo de login, `bd.readableDatabase` y `rawQuery` ejecutan la comprobación de credenciales. La llamada a `fila.moveToFirst()` posiciona el puntero en el primer registro del conjunto de resultados. Si devuelve `true`, extrae la columna `nota_maxima` mediante el índice de proyección tipado (`getInt(1)`).
*   **Gestión Estricta de Recursos:** Cierre explícito de los recursos nativos (`Cursor.close()` y `SQLiteDatabase.close()`) para evitar fugas de memoria o bloqueos de concurrencia en la base de datos.
*   **Transición con Cierre de Pila (`finish()`):** Al validarse el login, se envían los extras en el `Intent` y se invoca `finish()` para que el usuario no pueda pulsar "Atrás" desde la bienvenida y quedar en un estado inconsistente en la pantalla de login.

---

### 4.3 Control de Sesión y Bienvenida (`BienvenidaActivity.kt`)

```kotlin
package dev.danoglez.prctica5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BienvenidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val tvNotaMaxima = findViewById<TextView>(R.id.tvNotaMaxima)
        val btnComenzar = findViewById<Button>(R.id.btnComenzar)

        val username = intent.getStringExtra("USERNAME") ?: "Usuario"
        val notaMax = intent.getIntExtra("NOTA_MAXIMA", 0)

        tvSaludo.text = "¡Bienvenido/a, $username!"
        tvNotaMaxima.text = "Tu nota más alta hasta la fecha: $notaMax"

        btnComenzar.setOnClickListener {
            val intent = Intent(this, Pregunta1Activity::class.java)
            intent.putExtra("USERNAME", username)
            intent.putStringArrayListExtra("ANSWERS", ArrayList())
            startActivity(intent)
            finish()
        }
    }
}
```

#### Explicación Técnica:
*   **Extracción Segura de Parámetros:** Mediante el operador Elvis (`?:`) y los métodos tipados `getStringExtra` y `getIntExtra`, se garantiza la ausencia de excepciones de puntero nulo (`NullPointerException`) ante anomalías en el transporte de datos.
*   **Inicialización del Buffer de Sesión:** El botón `btnComenzar` crea un nuevo `ArrayList<String>()` vacío y lo adjunta mediante `putStringArrayListExtra("ANSWERS", ...)`. Este contenedor funcionará como el acumulador dinámico en memoria durante las diez fases del cuestionario.

---

### 4.4 Controlador Secuencial de Preguntas (`Pregunta1Activity.kt` a `Pregunta10Activity.kt`)

*(Estructura representativa implementada homogéneamente en las 10 actividades de preguntas)*

```kotlin
package dev.danoglez.prctica5

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Pregunta1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pregunta)

        val tvPregunta = findViewById<TextView>(R.id.tvPregunta)
        val rgOpciones = findViewById<RadioGroup>(R.id.rgOpciones)
        val rbOp1 = findViewById<RadioButton>(R.id.rbOp1)
        val rbOp2 = findViewById<RadioButton>(R.id.rbOp2)
        val rbOp3 = findViewById<RadioButton>(R.id.rbOp3)
        val btnSiguiente = findViewById<Button>(R.id.btnSiguiente)

        tvPregunta.text = "Pregunta 1/10\n\n¿Cuál es la capital de Francia?"
        rbOp1.text = "Madrid"
        rbOp2.text = "París"
        rbOp3.text = "Roma"
        btnSiguiente.text = "Responder y avanzar a la siguiente pregunta"

        btnSiguiente.setOnClickListener {
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
        }
    }
}
```

#### Explicación Técnica del Flujo de Preguntas:
*   **Inyección Dinámica de Contenidos:** En tiempo de ejecución (`onCreate`), se inyectan el texto de la pregunta y las etiquetas de las tres opciones en los `RadioButton` correspondientes, manteniendo el desacoplamiento entre vista y datos.
*   **Validación de Selección Obligatoria:** `rgOpciones.checkedRadioButtonId` devuelve el identificador entero del botón marcado. Si su valor es `-1`, significa que el usuario no ha realizado ninguna selección. En tal caso, se detiene la ejecución mediante un `return@setOnClickListener` temprano y se lanza un `Toast` de advertencia.
*   **Acumulación de Estado y Despacho:** Se obtiene la referencia del `RadioButton` seleccionado mediante `findViewById(selectedId)`, se extrae su propiedad `text`, se recupera la lista `ANSWERS` del intent entrante, se añade el nuevo elemento (`respuestas.add()`) y se propaga hacia la siguiente actividad (`Pregunta(N+1)Activity` o `ResultadosActivity` en la pregunta 10).
*   **Destrucción de Instancia (`finish()`):** Libera inmediatamente el contexto de la actividad actual del stack de Android.

---

### 4.5 Motor de Corrección y Actualización de Récord (`ResultadosActivity.kt`)

```kotlin
package dev.danoglez.prctica5

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadosActivity : AppCompatActivity() {

    private val correctAnswers = listOf(
        "París", "8", "Kotlin", "Amazonas", "Da Vinci", 
        "1969", "Pacífico", "Oxígeno", "Guepardo", "Marte"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultados)

        val tvNota = findViewById<TextView>(R.id.tvNota)
        val tvRecord = findViewById<TextView>(R.id.tvRecord)
        val tvResumen = findViewById<TextView>(R.id.tvResumen)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val username = intent.getStringExtra("USERNAME") ?: ""
        val userAnswers = intent.getStringArrayListExtra("ANSWERS") ?: ArrayList()

        var aciertos = 0
        val resumenBuilder = StringBuilder()

        // EVALUACIÓN DE RESPUESTAS
        for (i in correctAnswers.indices) {
            val userAnswer = if (i < userAnswers.size) userAnswers[i] else "No respondida"
            val correctAnswer = correctAnswers[i]

            resumenBuilder.append("Pregunta ${i + 1}: ")
            if (userAnswer == correctAnswer) {
                aciertos++
                resumenBuilder.append("Correcta ($userAnswer)\n")
            } else {
                resumenBuilder.append("Incorrecta. Tu respuesta: $userAnswer. Correcta: $correctAnswer\n")
            }
        }

        tvNota.text = "Aciertos: $aciertos\nNota: $aciertos/10"
        tvResumen.text = resumenBuilder.toString()

        comprobarYActualizarNota(username, aciertos, tvRecord)

        // CIERRE DE SESIÓN Y VACIADO DEL BACK STACK
        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun comprobarYActualizarNota(username: String, nuevaNota: Int, tvRecord: TextView) {
        val admin = DatabaseHelper(this, "CuestionarioBD", null, 1)
        val bd = admin.writableDatabase
        
        val fila = bd.rawQuery("select nota_maxima from usuarios where username='$username'", null)
        var notaMaximaAnterior = 0
        if (fila.moveToFirst()) {
            notaMaximaAnterior = fila.getInt(0)
        }
        fila.close()

        if (nuevaNota > notaMaximaAnterior) {
            tvRecord.text = "¡Felicidades! Has superado tu calificación más alta."
            val registro = ContentValues()
            registro.put("nota_maxima", nuevaNota)
            bd.update("usuarios", registro, "username='$username'", null)
        } else {
            tvRecord.text = "No has superado tu calificación más alta ($notaMaximaAnterior)."
        }
        bd.close()
    }
}
```

#### Explicación Técnica del Procesamiento:
*   **Vector de Corrección Estático (`correctAnswers`):** Lista inmutable en memoria que define las soluciones oficiales para las 10 preguntas.
*   **Algoritmo de Calificación y Generación del Resumen:** Se itera mediante `for (i in correctAnswers.indices)`. Se compara en tiempo lineal $O(N)$ la respuesta del usuario con la oficial. Se utiliza `StringBuilder` para optimizar la asignación de memoria evitando la creación innecesaria de objetos `String` efímeros en cada concatenación.
*   **Transacción Condicional en SQLite:**
    1.  Consulta el récord previo del usuario mediante `select nota_maxima from usuarios where username='$username'`.
    2.  Si la calificación actual supera el valor recuperado, emite una sentencia DML `bd.update()` parametrizada con `ContentValues` filtrada por `username`.
    3.  Ajusta dinámicamente el mensaje informativo presentado en `tvRecord`.
*   **Invalidación Atómica de la Tarea (`btnLogout`):** El uso de `Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK` purga por completo la pila de actividades, devolviendo el control al `MainActivity` en un contexto limpio y seguro.

---

## 5. Configuración del Manifiesto (`AndroidManifest.xml`)

```xml
<?xml version="1.0" encoding="utf-8"?>
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
        android:theme="@style/Theme.Práctica5">

        <!-- Actividad Principal: Punto de Entrada de la Aplicación -->
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    
        <!-- Actividades Internas del Cuestionario (No Exportadas) -->
        <activity android:name=".BienvenidaActivity" android:exported="false" />
        <activity android:name=".ResultadosActivity" android:exported="false" />
        <activity android:name=".Pregunta1Activity" android:exported="false" />
        <activity android:name=".Pregunta2Activity" android:exported="false" />
        <activity android:name=".Pregunta3Activity" android:exported="false" />
        <activity android:name=".Pregunta4Activity" android:exported="false" />
        <activity android:name=".Pregunta5Activity" android:exported="false" />
        <activity android:name=".Pregunta6Activity" android:exported="false" />
        <activity android:name=".Pregunta7Activity" android:exported="false" />
        <activity android:name=".Pregunta8Activity" android:exported="false" />
        <activity android:name=".Pregunta9Activity" android:exported="false" />
        <activity android:name=".Pregunta10Activity" android:exported="false" />
    </application>

</manifest>
```

#### Aspectos Clave de Seguridad y Configuración:
*   **Seguridad de Componentes (`android:exported`):**
    *   Únicamente `MainActivity` tiene `android:exported="true"` porque debe ser invocable por el sistema operativo y el lanzador (`LAUNCHER`).
    *   Todas las demás 12 actividades definen explícitamente `android:exported="false"`. Esto garantiza que aplicaciones de terceros no puedan invocar directamente pantallas intermedias del cuestionario ni eludir el flujo de autenticación.
*   **Gestión del Teclado en Pantalla:** `android:windowSoftInputMode="adjustResize"` en `MainActivity` permite que la ventana se reajuste de forma dinámica cuando el usuario hace foco en los campos de usuario y contraseña, previniendo que los botones de acción queden cubiertos por el IME (*Input Method Editor*).

---

## 6. Matriz de Pruebas y Casos de Uso

A continuación se detalla la batería de pruebas funcionales diseñada para validar la robustez del sistema:

| ID | Escenario / Caso de Prueba | Entrada / Acción | Resultado Esperado | Estado |
|---|---|---|---|---|
| **CP-01** | Registro de usuario nuevo | Usuario: `dani`, Contraseña: `123` -> Pulsar *Registro* | Registro insertado en SQLite. Mensaje Toast: *"Usuario registrado correctamente"*. | **OK** |
| **CP-02** | Registro de usuario duplicado | Usuario: `dani` (ya existente) -> Pulsar *Registro* | Violación de `UNIQUE`. Detección de `-1L`. Mensaje: *"Error: El usuario ya existe"*. | **OK** |
| **CP-03** | Autenticación incorrecta | Usuario: `dani`, Contraseña: `wrong` -> Pulsar *Login* | `moveToFirst()` devuelve `false`. Toast: *"Usuario o contraseña incorrectos"*. | **OK** |
| **CP-04** | Autenticación válida | Credenciales correctas -> Pulsar *Login* | Transición inmediata a `BienvenidaActivity`. Se extrae el récord histórico inicial (0). | **OK** |
| **CP-05** | Validación de selección obligatoria | No marcar ninguna opción en `Pregunta1Activity` -> Pulsar *Siguiente* | `checkedRadioButtonId == -1`. Ejecución bloqueada. Toast: *"Por favor, selecciona una respuesta"*. | **OK** |
| **CP-06** | Propagación secuencial de respuestas | Seleccionar opción y avanzar de Pregunta 1 a 10 | Las respuestas se añaden ordenadamente al `ArrayList`. La memoria se mantiene óptima al invocar `finish()`. | **OK** |
| **CP-07** | Corrección y superación de récord | El usuario acierta 8 preguntas (récord previo: 0) | Muestra *"Aciertos: 8, Nota: 8/10"*. Mensaje verde: *"¡Felicidades! Has superado tu calificación más alta."*. Se ejecuta `UPDATE` en SQLite. | **OK** |
| **CP-08** | Verificación de récord no superado | Mismo usuario vuelve a jugar y obtiene 4 aciertos | Muestra *"No has superado tu calificación más alta (8)."*. El valor en SQLite no se modifica. | **OK** |
| **CP-09** | Cierre de sesión seguro (Logout) | Pulsar botón *Logout (Volver al Login)* | Se limpian las actividades previas (`CLEAR_TASK`). Se redirige a `MainActivity`. Pulsar botón "Atrás" en Android cierra la app y no reabre resultados. | **OK** |

---

## 7. Conclusiones y Competencias Técnicas Adquiridas

La realización de esta práctica consolida las siguientes competencias fundamentales en el desarrollo de aplicaciones para Android con Kotlin:

1.  **Persistencia Transaccional con SQLite:** Dominio de `SQLiteOpenHelper`, modelado DDL con restricciones de clave única, gestión adecuada de `ContentValues` para inserciones y actualizaciones, y manejo seguro de cursores de lectura (`Cursor`).
2.  **Arquitectura Basada en Componentes Reutilizables:** Eficiencia en el diseño de interfaces al compartir un único recurso de layout (`activity_pregunta.xml`) en múltiples controladores de actividad, optimizando el mantenimiento y el peso del proyecto.
3.  **Manejo Avanzado del Ciclo de Vida y Navegación:** Implementación de patrones de navegación unidireccionales, gestión de la memoria operativa destruyendo actividades consumidas (`finish()`) e invalidación de la sesión mediante banderas de control de tareas (`FLAG_ACTIVITY_CLEAR_TASK`).
4.  **Validaciones Defensivas en Tiempo de Ejecución:** Tratamiento de datos de entrada vacíos, comprobación de selecciones de usuario mediante componentes `RadioGroup` y control de colisiones de datos relacionales sin interrupciones del hilo principal de ejecución.
