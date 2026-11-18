# 📘 GUÍA DEFINITIVA DE ANDROID & KOTLIN (PGL / DAM)
> **Manual de Supervivencia para Examen: Prácticas 1 a 5**  
> *Calculadora, Controles (Radio/Check/Spinner), Múltiples Activities (Intents), SQLite (AdminSQLiteOpenHelper) y Cifrado.*

---

## 📑 ÍNDICE RÁPIDO
1. [Regla de Oro: Ciclo de vida y anatomía de un Activity](#1-regla-de-oro-anatomía-de-un-activity)
2. [Bloque 1: Enlazar Vistas (`findViewById`) y Eventos (`Click`)](#bloque-1-enlazar-vistas-y-eventos-click)
3. [Bloque 2: Textos, Números y Cálculos (Práctica 1)](#bloque-2-textos-números-y-cálculos-práctica-1)
4. [Bloque 3: Controles de Selección: Radios, Checks y Spinners (Práctica 2)](#bloque-3-controles-de-selección-práctica-2)
5. [Bloque 4: Múltiples Pantallas (`Intents` y paso de datos) (Práctica 3)](#bloque-4-múltiples-pantallas-intents-y-datos-práctica-3)
6. [Bloque 5: Base de Datos SQLite con `AdminSQLiteOpenHelper` (Prácticas 4 y 5)](#bloque-5-base-de-datos-sqlite-con-adminsqliteopenhelper)
7. [Bloque 6: Algoritmos de Cifrado y Manipulación de Textos (Práctica 3 y 4)](#bloque-6-algoritmos-de-cifrado-y-manipulación-de-textos)
8. [🚨 Top 7 Errores Típicos de Examen (¡No caigas aquí!)](#-top-7-errores-típicos-de-examen)
9. [⚡ Chuleta Relámpago de Código (Copy-Paste Mental)](#-chuleta-relámpago-de-código)

---

## 1. REGLA DE ORO: ANATOMÍA DE UN ACTIVITY

En el examen te darán el XML ya diseñado y la clase Kotlin vacía o con `onCreate`.
Cualquier pantalla en Android siempre tiene esta estructura mínima:

```kotlin
package com.tu.paquete

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Carga el XML

        // TODO TU CÓDIGO DEL EXAMEN SE ESCRIBE AQUÍ DENTRO
    }
}
```

> ⚠️ **Importante:** Nada de lo que hay en el XML existe en Kotlin hasta que no se ejecuta `setContentView`. Por tanto, **todos los `findViewById` y `setOnClickListener` van dentro de `onCreate`**.

---

## BLOQUE 1: ENLAZAR VISTAS Y EVENTOS CLICK

### 1.1 `findViewById`: El puente entre XML y Kotlin
Conecta el `android:id="@+id/miElemento"` del XML con una variable de Kotlin:

```kotlin
// Cajas de texto donde el usuario escribe
val etNombre = findViewById<EditText>(R.id.etNombre)

// Textos estáticos o etiquetas de resultados
val tvResultado = findViewById<TextView>(R.id.tvResultado)

// Botones para ejecutar acciones
val btnAceptar = findViewById<Button>(R.id.btnAceptar)

// Desplegables
val spOpciones = findViewById<Spinner>(R.id.spOpciones)

// Radios y Checks
val rbOpcion1 = findViewById<RadioButton>(R.id.rbOpcion1)
val cbAcepto = findViewById<CheckBox>(R.id.cbAcepto)
```

### 1.2 Detectar clics (`setOnClickListener`)
```kotlin
btnAceptar.setOnClickListener {
    // Código que se ejecuta cuando el usuario pulsa el botón
}
```

### 1.3 Mensajes emergentes (`Toast`)
Para avisar al usuario de que algo salió bien o falta rellenar un dato:
```kotlin
// Toast corto (desaparece rápido)
Toast.makeText(this, "Operación realizada con éxito", Toast.LENGTH_SHORT).show()

// Toast largo (se queda más tiempo en pantalla)
Toast.makeText(this, "Error: Rellena todos los campos obligatorios", Toast.LENGTH_LONG).show()
```
> ⚠️ **¡CUIDADO!** Si olvidas poner `.show()` al final, el Toast **no saldrá en pantalla**. Es el error más común en exámenes.

---

## BLOQUE 2: TEXTOS, NÚMEROS Y CÁLCULOS (PRÁCTICA 1)

### 2.1 Leer texto de un EditText
```kotlin
val texto = etNombre.text.toString().trim() // .trim() elimina espacios sobrantes al inicio y final

if (texto.isEmpty()) {
    Toast.makeText(this, "Escribe algo primero", Toast.LENGTH_SHORT).show()
}
```

### 2.2 Conversión segura de números (Evita que la App se cierre)
Si haces `etNumero.text.toString().toInt()` y la caja está vacía, **la app revienta**. 
**Usa siempre `toIntOrNull()` o `toDoubleOrNull()` con el operador Elvis `?:`**:

```kotlin
// Si está vacío o no es un número válido, toma por defecto el valor 0
val n1 = etNum1.text.toString().toIntOrNull() ?: 0
val n2 = etNum2.text.toString().toIntOrNull() ?: 0

// Para decimales:
val decimal = etDecimal.text.toString().toDoubleOrNull() ?: 0.0
```

### 2.3 Operaciones matemáticas habituales
```kotlin
// Suma, resta, multiplicación
val suma = n1 + n2
val resta = n1 - n2
val multi = n1 * n2

// División (¡Cuidado con la división por cero!)
if (n2 == 0) {
    tvResultado.text = "No se puede dividir por cero"
} else {
    val div = n1.toDouble() / n2.toDouble()
    tvResultado.text = "División: $div"
}

// Potencia y Raíz
val potencia = Math.pow(n1.toDouble(), n2.toDouble())
val raiz = Math.sqrt(n1.toDouble())
```

### 2.4 Escribir el resultado en un TextView (Interpolación `$`)
```kotlin
tvResultado.text = "El total es: $suma"
tvDetalle.text = "Hola $nombre, tu saldo actual es de $saldo €"
```

---

## BLOQUE 3: CONTROLES DE SELECCIÓN (PRÁCTICA 2)

### 3.1 RadioButton y RadioGroup (Selección exclusiva: solo 1 opción)

#### Forma A: Comprobar el RadioButton directamente con `.isChecked`
```kotlin
val modalidad = if (rbPresencial.isChecked) {
    "Presencial"
} else {
    "Online"
}
```

#### Forma B: Obtener el marcado desde el `RadioGroup` (Ideal para exámenes con muchas opciones)
```kotlin
val rgOpciones = findViewById<RadioGroup>(R.id.rgOpciones)

val idSeleccionado = rgOpciones.checkedRadioButtonId
if (idSeleccionado == -1) {
    Toast.makeText(this, "Por favor, selecciona una opción", Toast.LENGTH_SHORT).show()
} else {
    val rbElegido = findViewById<RadioButton>(idSeleccionado)
    val respuesta = rbElegido.text.toString()
}
```

---

### 3.2 CheckBox (Casillas de verificación independientes)
Devuelve `true` si está marcado o `false` si no:

```kotlin
val quiereDiploma = cbDiploma.isChecked

if (cbTerminos.isChecked) {
    // Ha aceptado los términos
} else {
    Toast.makeText(this, "Debes aceptar los términos para continuar", Toast.LENGTH_SHORT).show()
}
```
> ⚠️ **NUNCA uses `.isSelected` en un CheckBox**. Se usa siempre **`.isChecked`**.

---

### 3.3 Spinner (Menú Desplegable)
El Spinner necesita un **`ArrayAdapter`** para vincular la lista de opciones con la interfaz visual. **Son 3 líneas fijas**:

```kotlin
// 1. Array con las opciones
val cursos = arrayOf("Kotlin", "Java", "Python", "C#")

// 2. Crear el ArrayAdapter con el diseño estándar de Android
val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cursos)
adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

// 3. Asignar el adaptador al Spinner
spCursos.adapter = adapter
```

#### ¿Cómo leer la opción seleccionada por el usuario?
```kotlin
// Como texto
val opcionTexto = spCursos.selectedItem.toString()

// Como posición numérica (0 para el primero, 1 para el segundo...)
val posicion = spCursos.selectedItemPosition
```

---

## BLOQUE 4: MÚLTIPLES PANTALLAS (INTENTS Y DATOS) (PRÁCTICA 3)

Un `Intent` es el mensajero que permite cambiar de pantalla y enviarle información.

### 4.1 Enviar datos desde la Pantalla 1 (`MainActivity.kt`):
```kotlin
val intent = Intent(this, SegundaActivity::class.java).apply {
    putExtra("CLAVE_USUARIO", "Carlos")
    putExtra("CLAVE_EDAD", 25)
    putExtra("CLAVE_PRECIO", 19.99)
    putExtra("CLAVE_BECA", true)
}
startActivity(intent)

// Opcional: si es una pantalla de Login y NO quieres que vuelva al pulsar atrás:
// finish()
```

### 4.2 Recibir datos en la Pantalla 2 (`SegundaActivity.kt`):
Dentro de su `onCreate`, se leen usando el mismo nombre de clave:

```kotlin
// Para texto (Strings) -> Si es null, le damos valor por defecto con ?:
val usuario = intent.getStringExtra("CLAVE_USUARIO") ?: "Anónimo"

// Para números enteros (Int) -> Requiere un valor por defecto si no existe (ej: 0)
val edad = intent.getIntExtra("CLAVE_EDAD", 0)

// Para números decimales (Double)
val precio = intent.getDoubleExtra("CLAVE_PRECIO", 0.0)

// Para booleanos (Boolean)
val tieneBeca = intent.getBooleanExtra("CLAVE_BECA", false)
```

### 4.3 Botón para Volver atrás (`finish`)
```kotlin
val btnVolver = findViewById<Button>(R.id.btnVolver)
btnVolver.setOnClickListener {
    finish() // Destruye la pantalla actual y vuelve a la anterior
}
```

> ⚠️ **REGLA CRUCIAL DE MANIFEST**: Cada Activity que crees debe estar registrada en `app/src/main/AndroidManifest.xml`:
> ```xml
> <activity android:name=".SegundaActivity" android:exported="false" />
> ```
> *(Si creas la Activity desde Android Studio con Clic Derecho > New > Activity > Empty Views Activity, se registra sola).*

---

## BLOQUE 5: BASE DE DATOS SQLITE CON `AdminSQLiteOpenHelper`

En tus clases y exámenes, el profesor utiliza la clase **`AdminSQLiteOpenHelper`**.

### 5.1 La Clase Helper (`AdminSQLiteOpenHelper.kt`)
```kotlin
package com.tu.paquete

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteOpenHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : 
    SQLiteOpenHelper(context, name, factory, version) {

    // Se ejecuta una sola vez al crear la base de datos
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE usuarios (usuario TEXT PRIMARY KEY, contrasena TEXT)")
    }

    // Se ejecuta al actualizar la versión de la base de datos
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }
}
```

---

### 5.2 Operaciones CRUD en los Botones:

#### 🟢 INSERT (Registrar un usuario)
```kotlin
btnRegistrar.setOnClickListener {
    // 1. Instanciar la BD
    val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
    val bd = admin.writableDatabase // Permiso de ESCRITURA

    val usu = etUsuario.text.toString().trim()
    val pass = etContrasena.text.toString().trim()

    if (usu.isNotEmpty() && pass.isNotEmpty()) {
        // 2. Meter los valores en el contenedor ContentValues
        val registro = ContentValues()
        registro.put("usuario", usu)
        registro.put("contrasena", pass)

        // 3. Insertar en la tabla "usuarios"
        // Devuelve el id de la fila insertada o -1L si falló (ej: usuario duplicado)
        val resultado = bd.insert("usuarios", null, registro)
        bd.close() // SIEMPRE CERRAR

        if (resultado != -1L) {
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
    }
}
```

---

#### 🔵 SELECT (Login / Comprobar si existe)
```kotlin
btnLogin.setOnClickListener {
    val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
    val bd = admin.readableDatabase // Permiso de LECTURA

    val usu = etUsuario.text.toString().trim()
    val pass = etContrasena.text.toString().trim()

    if (usu.isNotEmpty() && pass.isNotEmpty()) {
        // 1. Ejecutar consulta SQL con parámetros seguros (?)
        val cursor = bd.rawQuery(
            "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?", 
            arrayOf(usu, pass)
        )

        // 2. moveToFirst() devuelve true si encontró la fila
        if (cursor.moveToFirst()) {
            cursor.close()
            bd.close()

            // Login correcto: navegar a la siguiente pantalla
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USUARIO", usu)
            startActivity(intent)
            finish() // Cierra el login
        } else {
            cursor.close()
            bd.close()
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
    }
}
```

---

#### 🟡 UPDATE (Modificar contraseña o datos)
```kotlin
val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
val bd = admin.writableDatabase

val registro = ContentValues()
registro.put("contrasena", "nuevaPassword123")

// Modifica la contraseña donde el usuario coincida
val cant = bd.update("usuarios", registro, "usuario = ?", arrayOf(usu))
bd.close()

if (cant > 0) {
    Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
} else {
    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
}
```

---

#### 🔴 DELETE (Borrar un usuario)
```kotlin
val admin = AdminSQLiteOpenHelper(this, "administracion", null, 1)
val bd = admin.writableDatabase

// Borra la fila donde el usuario coincida
val cant = bd.delete("usuarios", "usuario = ?", arrayOf(usu))
bd.close()

if (cant > 0) {
    Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
} else {
    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
}
```

---

## BLOQUE 6: ALGORITMOS DE CIFRADO Y MANIPULACIÓN DE TEXTOS

En la Práctica 3 y 4 se piden métodos de transformación de texto (cifrado / descifrado):

### 6.1 Invertir texto (El más simple)
```kotlin
val original = "Hola Mundo"
val invertido = original.reversed() // "odnuM aloH"
```

### 6.2 Base64 (Android util)
Requiere importar: `import android.util.Base64`

```kotlin
// CODIFICAR (Encriptar):
val textoOriginal = "MiClaveSecreta"
val cifrado = Base64.encodeToString(textoOriginal.toByteArray(), Base64.DEFAULT)

// DECODIFICAR (Desencriptar):
val descifrado = String(Base64.decode(cifrado, Base64.DEFAULT))
```

### 6.3 Cifrado César (+3 letras) y el uso de `.code`
En Kotlin, el código ASCII/Unicode de un caracter se obtiene con **`char.code`**:
```kotlin
// Desplazar 3 letras hacia adelante
fun cifradoCesar(texto: String): String {
    val resultado = StringBuilder()
    for (c in texto) {
        if (c.isLetter()) {
            val base = if (c.isUpperCase()) 'A' else 'a'
            val nuevaLetra = (base.code + (c.code - base.code + 3) % 26).toChar()
            resultado.append(nuevaLetra)
        } else {
            resultado.append(c)
        }
    }
    return resultado.toString()
}
```

---

## 🚨 TOP 7 ERRORES TÍPICOS DE EXAMEN

| # | Error en Código | Consecuencia | Solución Correcta |
|---|----------------|--------------|-------------------|
| **1** | `Toast.makeText(this, "Texto", Toast.LENGTH_SHORT)` | No sale el mensaje en pantalla | Añadir **`.show()`** al final |
| **2** | `et.text.toString().toInt()` con caja vacía | **CRASH:** `NumberFormatException` | Usar **`.toIntOrNull() ?: 0`** |
| **3** | `cbOpcion.isSelected` | Casi siempre devuelve `false` | Usar siempre **`.isChecked`** |
| **4** | No declarar la Activity en `AndroidManifest.xml` | **CRASH:** `ActivityNotFoundException` | Añadir `<activity android:name=".MiActivity" />` |
| **5** | Olvidar cerrar `cursor` o `bd` en SQLite | Fuga de memoria / warning grave | Poner siempre **`cursor.close()`** y **`bd.close()`** |
| **6** | `CREATE TABLE tabla (sin columnas)` | **CRASH:** `SQLiteException: syntax error` | Definir columnas: `CREATE TABLE tabla (id TEXT PRIMARY KEY)` |
| **7** | Claves diferentes en Intent | Devuelve valor por defecto / nulo | Usar el **mismo nombre exacto**: `putExtra("KEY", x)` -> `getStringExtra("KEY")` |

---

## ⚡ CHULETA RELÁMPAGO DE CÓDIGO

```kotlin
// 1. Enlazar
val btn = findViewById<Button>(R.id.btn)
val et = findViewById<EditText>(R.id.et)
val tv = findViewById<TextView>(R.id.tv)

// 2. Leer número seguro
val n = et.text.toString().toIntOrNull() ?: 0

// 3. Click y Toast
btn.setOnClickListener {
    Toast.makeText(this, "Hola", Toast.LENGTH_SHORT).show()
}

// 4. Radios y Checks
val radioMarcado = rb1.isChecked
val checkMarcado = cb1.isChecked

// 5. Spinner
val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf("A", "B"))
sp.adapter = adapter
val seleccion = sp.selectedItem.toString()

// 6. Pasar a otra Activity
val i = Intent(this, OtraActivity::class.java).apply {
    putExtra("DATO", "Hola")
}
startActivity(i)

// 7. Recibir en otra Activity
val dato = intent.getStringExtra("DATO") ?: ""
btnVolver.setOnClickListener { finish() }

// 8. SQLite Insert
val bd = AdminSQLiteOpenHelper(this, "administracion", null, 1).writableDatabase
val reg = ContentValues().apply { put("usuario", "admin"); put("contrasena", "1234") }
bd.insert("usuarios", null, reg)
bd.close()

// 9. SQLite Select
val bdRead = AdminSQLiteOpenHelper(this, "administracion", null, 1).readableDatabase
val cursor = bdRead.rawQuery("SELECT * FROM usuarios WHERE usuario = ?", arrayOf("admin"))
if (cursor.moveToFirst()) { /* Existe */ }
cursor.close()
bdRead.close()
```
