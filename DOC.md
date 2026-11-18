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
10. [🏆 Ejemplo Completo Resuelto: PizzApp (todo junto)](#-ejemplo-completo-resuelto-pizzapp-todo-junto)

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

---

## 🏆 EJEMPLO COMPLETO RESUELTO: PIZZAPP (TODO JUNTO)

> Un ejercicio tipo examen que junta **TODO** lo de las Prácticas 1-5 en una sola app: login con SQLite y contraseña cifrada, formulario con Spinner, Radio y Check, cálculos con validación, paso de datos entre 3 pantallas, INSERT/SELECT/UPDATE/DELETE y recorrido de un cursor con varias filas.
> Si entiendes este ejemplo línea a línea, tienes el examen. 💪

---

### 10.1 📝 Enunciado (tal y como lo pondría el profe)

> **PizzApp: pedidos de una pizzería**
>
> Desarrolla una app Android en Kotlin con **3 pantallas**:
>
> **Pantalla 1: Login (`LoginActivity`)**
> - Campos usuario y contraseña. Botones **Entrar** y **Registrarse**.
> - Los usuarios se guardan en SQLite. La contraseña se guarda **cifrada en Base64**.
> - No se permite registrar un usuario repetido ni dejar campos vacíos. Contraseña de 4 caracteres como mínimo.
> - Al entrar se pasa el nombre del usuario a la siguiente pantalla, y el login **no debe quedarse en la pila** (al pulsar Atrás no se vuelve a él).
>
> **Pantalla 2: Pedido (`PedidoActivity`)**
> - Saludo con el nombre del usuario.
> - **Spinner** de pizzas: Margarita (8 €), Barbacoa (10,50 €), Cuatro Quesos (11 €), Vegetal (9,50 €). Al cambiar de pizza se muestra su precio base.
> - **RadioGroup** de tamaño: Pequeña (+0 €), Mediana (+2 €, marcada por defecto), Familiar (+4 €).
> - **CheckBox** de extras: Extra queso (+1,50 € por pizza), Bebida (+2 € por pizza), Envío a domicilio (+3 € por pedido).
> - **Cantidad** de pizzas (entre 1 y 20).
> - Botón **Calcular**: muestra el desglose. Con **3 o más pizzas hay un 10 % de descuento** (el envío no se descuenta).
> - Botón **Confirmar**: guarda el pedido en la BD, suma al usuario **1 punto por cada euro entero** y abre el resumen.
>
> **Pantalla 3: Resumen (`ResumenActivity`)**
> - Muestra el ticket del pedido, los puntos acumulados y el historial de pedidos del usuario con el total gastado.
> - **Nuevo pedido**: vuelve al formulario. **Cancelar pedido**: pide confirmación, borra el pedido y resta los puntos. **Cerrar sesión**: vuelve al login sin poder volver atrás.

---

### 10.2 🗺️ Requisito → técnica → dónde está en esta guía

| Requisito del enunciado | Técnica que lo resuelve | Bloque |
|---|---|---|
| Registro / login | `insert` + `rawQuery` con `?` | Bloque 5 |
| Contraseña cifrada | `Base64.encodeToString(..., NO_WRAP)` | Bloque 6 |
| Lista de pizzas y su precio | `Spinner` + `ArrayAdapter` + `OnItemSelectedListener` | Bloque 3 (+ nuevo) |
| Tamaño | `RadioGroup.checkedRadioButtonId` + `when` | Bloque 3 |
| Extras | `CheckBox.isChecked` | Bloque 3 |
| Cantidad, descuento y total | `toIntOrNull()`, rangos `in 1..20`, `"%.2f".format()` | Bloque 2 |
| Pasar datos entre pantallas | `Intent` + `putExtra` / `getXxxExtra` | Bloque 4 |
| Login fuera de la pila / logout | `finish()` + `FLAG_ACTIVITY_CLEAR_TASK` | Bloque 4 (+ nuevo) |
| Sumar / restar puntos | `SELECT` + `update` con ContentValues, y `execSQL` | Bloque 5 |
| Historial de pedidos | Bucle `while (cursor.moveToNext())` | **Nuevo** |
| Confirmar antes de borrar | `AlertDialog` | **Nuevo** |

---

### 10.3 🔀 Flujo de pantallas y pila (back stack)

```
┌─────────────────────┐
│    LoginActivity    │   ← LAUNCHER: la app arranca aquí
└──────────┬──────────┘
           │  "Entrar" OK → putExtra("USUARIO") + finish()
           ▼
┌─────────────────────┐
│   PedidoActivity    │   ← "Nuevo pedido" y "Cancelar" vuelven aquí (finish)
└──────────┬──────────┘
           │  "Confirmar" OK → INSERT pedido + UPDATE puntos + putExtra (x8)
           ▼
┌─────────────────────┐
│   ResumenActivity   │
└──────────┬──────────┘
           │  "Cerrar sesión" → FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK
           ▼
     LoginActivity  (pila limpia: "Atrás" ya no vuelve a nada)
```

| Momento | Pila (abajo → arriba) | Si pulsas "Atrás"… |
|---|---|---|
| Abres la app | `Login` | Sales de la app |
| Entras | `Pedido` (el Login hizo `finish()`) | Sales de la app |
| Confirmas un pedido | `Pedido → Resumen` | Vuelves a Pedido |
| Cierras sesión | `Login` (CLEAR_TASK borró el resto) | Sales de la app |

---

### 10.4 📁 Estructura de archivos

```
app/src/main/
├── AndroidManifest.xml              ← las 3 Activities + LAUNCHER en el Login
├── java/com/tu/paquete/
│   ├── AdminSQLiteOpenHelper.kt     ← crea las tablas
│   ├── LoginActivity.kt             ← registro + login + cifrado
│   ├── PedidoActivity.kt            ← formulario + cálculos + INSERT/UPDATE
│   └── ResumenActivity.kt           ← ticket + historial + DELETE + logout
└── res/layout/
    ├── activity_login.xml
    ├── activity_pedido.xml
    └── activity_resumen.xml
```

---

### 10.5 🧭 Orden de ataque en el examen

1. **Proyecto**: *New Project → **Empty Views Activity***. ¡Ojo! "Empty Activity" a secas es Jetpack Compose y no trae XML.
2. **Helper de la BD primero** (`AdminSQLiteOpenHelper.kt`): todo lo demás depende de las tablas.
3. **Crea las Activities** con *clic derecho en el paquete → New → Activity → Empty Views Activity*. Así se crean de golpe el `.kt`, el `.xml` y la entrada en el Manifest.
4. **Manifest**: mueve el `<intent-filter>` LAUNCHER a la Activity que se abre primero (el Login).
5. **XML**: pon los `android:id` antes que nada, con prefijo de tipo (`et`, `tv`, `btn`, `sp`, `rg`, `rb`, `cb`). Así en Kotlin sabes qué es cada cosa sin mirar el XML.
6. **Kotlin pantalla a pantalla**, siempre en este orden: `findViewById` → validaciones → lógica → BD → navegación.
7. **Ejecuta después de cada pantalla**, no al final. Si algo peta con 1 pantalla nueva, sabes dónde está el fallo; con 3 a la vez, no.
8. Pasa la **tabla de pruebas** (10.11) y el **checklist final** (10.15).

---

### 10.6 🗄️ `AdminSQLiteOpenHelper.kt`

```kotlin
package com.tu.paquete   // ⚠️ Pon el paquete de TU proyecto (cópialo de la 1ª línea de MainActivity)

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Gestiona la base de datos "pizzeria".
 * En TODAS las pantallas se crea exactamente igual:
 *     AdminSQLiteOpenHelper(this, "pizzeria", null, 1)
 * ⚠️ Mismo nombre y misma versión en todas partes. Si en una pantalla escribes "pizeria",
 *    Android crea OTRA base de datos vacía y el login "no encuentra" a nadie.
 */
class AdminSQLiteOpenHelper(
    context: Context,
    name: String,                              // nombre del fichero de la BD
    factory: SQLiteDatabase.CursorFactory?,    // siempre null
    version: Int                               // empieza en 1; súbelo si cambias las tablas
) : SQLiteOpenHelper(context, name, factory, version) {

    /** Solo se ejecuta UNA vez: cuando la base de datos todavía no existe. */
    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de usuarios. "usuario" es PRIMARY KEY → no se puede repetir.
        // Gracias a eso, si registras uno repetido, insert() devuelve -1 y lo detectamos.
        db.execSQL(
            "CREATE TABLE usuarios (" +
                    "usuario TEXT PRIMARY KEY, " +
                    "contrasena TEXT NOT NULL, " +       // guardada en Base64, nunca en claro
                    "puntos INTEGER DEFAULT 0)"          // si no la rellenas al insertar, vale 0
        )

        // Tabla de pedidos. El id lo genera SQLite solo (AUTOINCREMENT).
        // La columna "usuario" indica de quién es cada pedido.
        db.execSQL(
            "CREATE TABLE pedidos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "usuario TEXT NOT NULL, " +
                    "pizza TEXT, " +
                    "tamano TEXT, " +                    // ⚠️ sin ñ ni tildes en nombres de columna
                    "extras TEXT, " +
                    "cantidad INTEGER, " +
                    "total REAL)"                        // REAL = número con decimales
        )
    }

    /** Se ejecuta cuando subes la versión (por ejemplo, de 1 a 2). */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Versión "de examen": borrar todo y crearlo de nuevo (se pierden los datos)
        db.execSQL("DROP TABLE IF EXISTS pedidos")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }
}
```

**Equivalencia de tipos SQLite ↔ Kotlin** (para el `CREATE TABLE` y para leer del cursor):

| SQLite | Kotlin | Guardar | Leer del cursor |
|---|---|---|---|
| `TEXT` | `String` | `put("col", "texto")` | `cursor.getString(i)` |
| `INTEGER` | `Int` / `Long` | `put("col", 5)` | `cursor.getInt(i)` / `cursor.getLong(i)` |
| `REAL` | `Double` | `put("col", 9.5)` | `cursor.getDouble(i)` |

> ⚠️ **TRAMPA GORDA:** el `onCreate` del helper **solo se ejecuta la primera vez que se crea la BD**. Si después cambias el `CREATE TABLE` (añades una columna o una tabla), en el móvil sigue la BD vieja → `SQLiteException: no such table / no such column`.
> **Solución:** desinstala la app del emulador (mantén pulsado el icono → Desinstalar) **o** sube la versión a `2` en **todas** las llamadas para que se ejecute `onUpgrade`.

---

### 10.7 🔐 Pantalla 1: Login

#### `activity_login.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- LinearLayout vertical: los elementos se apilan de arriba abajo.
     gravity="center" los centra en la pantalla. -->
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical"
    android:padding="24dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="24dp"
        android:text="🍕 PizzApp"
        android:textSize="32sp"
        android:textStyle="bold" />

    <EditText
        android:id="@+id/etUsuario"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Usuario"
        android:inputType="text" />

    <!-- textPassword: muestra puntitos en vez de las letras -->
    <EditText
        android:id="@+id/etContrasena"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Contraseña (mín. 4 caracteres)"
        android:inputType="textPassword" />

    <Button
        android:id="@+id/btnEntrar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="16dp"
        android:text="Entrar" />

    <Button
        android:id="@+id/btnRegistrar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Registrarse" />

</LinearLayout>
```

#### `LoginActivity.kt`
```kotlin
package com.tu.paquete

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Base64          // ⚠️ android.util.Base64, NO java.util.Base64 (no son iguales)
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    // Declaradas FUERA de onCreate para poder usarlas en las funciones de abajo.
    // lateinit = "todavía no tienen valor; se lo doy en onCreate antes de usarlas".
    private lateinit var etUsuario: EditText
    private lateinit var etContrasena: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 1. Enlazar vistas (SIEMPRE después de setContentView)
        etUsuario = findViewById(R.id.etUsuario)      // no hace falta <EditText>: ya lo dice la declaración
        etContrasena = findViewById(R.id.etContrasena)
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val btnRegistrar = findViewById<Button>(R.id.btnRegistrar)

        // 2. Cada botón llama a su función → onCreate queda corto y fácil de leer
        btnEntrar.setOnClickListener { entrar() }
        btnRegistrar.setOnClickListener { registrar() }
    }

    // ─────────────────────────────────────────────────────────────
    // 🟢 REGISTRO → INSERT
    // ─────────────────────────────────────────────────────────────
    private fun registrar() {
        val usuario = etUsuario.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        // Validación 1: campos vacíos
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Rellena usuario y contraseña", Toast.LENGTH_SHORT).show()
            return  // corta la función aquí: no llega a tocar la BD
        }
        // Validación 2: longitud mínima.
        // .error pinta un aviso rojo en el propio campo (queda más pro que un Toast)
        if (contrasena.length < 4) {
            etContrasena.error = "Mínimo 4 caracteres"
            return
        }

        val admin = AdminSQLiteOpenHelper(this, "pizzeria", null, 1)
        val bd = admin.writableDatabase                  // ESCRITURA: insert / update / delete

        val registro = ContentValues()
        registro.put("usuario", usuario)                 // put("nombre_columna", valor)
        registro.put("contrasena", cifrar(contrasena))   // 🔐 se guarda cifrada, nunca en claro
        // "puntos" no lo ponemos → la tabla le asigna su DEFAULT 0

        // insert devuelve el id de la fila nueva, o -1 si falla.
        // Aquí falla si el usuario ya existe, porque es PRIMARY KEY.
        val resultado = bd.insert("usuarios", null, registro)
        bd.close()

        if (resultado == -1L) {   // -1L (con L) porque insert devuelve un Long
            Toast.makeText(this, "El usuario '$usuario' ya existe", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "¡Registrado! Ya puedes entrar", Toast.LENGTH_SHORT).show()
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 🔵 LOGIN → SELECT
    // ─────────────────────────────────────────────────────────────
    private fun entrar() {
        val usuario = etUsuario.text.toString().trim()
        val contrasena = etContrasena.text.toString().trim()

        if (usuario.isEmpty() || contrasena.isEmpty()) {
            Toast.makeText(this, "Rellena usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        val admin = AdminSQLiteOpenHelper(this, "pizzeria", null, 1)
        val bd = admin.readableDatabase                  // LECTURA: select

        // Comparamos con la contraseña CIFRADA, porque así es como está guardada.
        // Cada ? se sustituye, en orden, por un valor del arrayOf.
        // ❌ NUNCA: "... WHERE usuario = '$usuario'" → inyección SQL y fallos con comillas
        val cursor = bd.rawQuery(
            "SELECT usuario FROM usuarios WHERE usuario = ? AND contrasena = ?",
            arrayOf(usuario, cifrar(contrasena))
        )
        val existe = cursor.moveToFirst()   // true si hay al menos una fila
        cursor.close()                      // cerrar ANTES de navegar a otra pantalla
        bd.close()

        if (existe) {
            val intentPedido = Intent(this, PedidoActivity::class.java)
            intentPedido.putExtra("USUARIO", usuario)
            startActivity(intentPedido)
            finish()   // quita el Login de la pila: desde Pedido, "Atrás" ya no vuelve aquí
        } else {
            // Mismo mensaje para ambos fallos: no le digas a nadie si el usuario existe
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            etContrasena.text.clear()
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 🔐 CIFRADO (Base64)
    // ─────────────────────────────────────────────────────────────
    /**
     * Usamos NO_WRAP porque DEFAULT añade un "\n" al final del texto cifrado.
     * Para usar César en vez de Base64: return cifradoCesar(texto)   (ver Bloque 6.3)
     *
     * 📌 Base64 NO es cifrado de verdad (cualquiera lo decodifica), pero es lo que
     *    piden las prácticas. En una app real se guarda un hash (SHA-256 + salt).
     */
    private fun cifrar(texto: String): String {
        return Base64.encodeToString(texto.toByteArray(), Base64.NO_WRAP)
    }
}
```

---

### 10.8 🍕 Pantalla 2: Pedido

#### `activity_pedido.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<!-- ScrollView: si el formulario no cabe en la pantalla, se puede desplazar.
     ⚠️ Un ScrollView solo admite UN hijo directo → todo va dentro de un LinearLayout. -->
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="16dp">

        <TextView
            android:id="@+id/tvSaludo"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="¡Hola!"
            android:textSize="20sp"
            android:textStyle="bold" />

        <!-- ───────── PIZZA ───────── -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Pizza:"
            android:textStyle="bold" />

        <Spinner
            android:id="@+id/spPizza"
            android:layout_width="match_parent"
            android:layout_height="wrap_content" />

        <TextView
            android:id="@+id/tvPrecioBase"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Precio base: -" />

        <!-- ───────── TAMAÑO ─────────
             Los RadioButton tienen que ir DENTRO del RadioGroup y tener id;
             si no, se pueden marcar varios a la vez. -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Tamaño (Pequeña +0 € · Mediana +2 € · Familiar +4 €):"
            android:textStyle="bold" />

        <RadioGroup
            android:id="@+id/rgTamano"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="horizontal">

            <RadioButton
                android:id="@+id/rbPequena"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Pequeña" />

            <!-- checked="true" → marcada por defecto al abrir la pantalla -->
            <RadioButton
                android:id="@+id/rbMediana"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:checked="true"
                android:text="Mediana" />

            <RadioButton
                android:id="@+id/rbFamiliar"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="Familiar" />
        </RadioGroup>

        <!-- ───────── EXTRAS (independientes: se pueden marcar varios) ───────── -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Extras:"
            android:textStyle="bold" />

        <CheckBox
            android:id="@+id/cbQueso"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Extra queso (+1,50 € por pizza)" />

        <CheckBox
            android:id="@+id/cbBebida"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Bebida (+2,00 € por pizza)" />

        <CheckBox
            android:id="@+id/cbDomicilio"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Envío a domicilio (+3,00 € por pedido)" />

        <!-- ───────── CANTIDAD ─────────
             inputType="number": teclado solo de dígitos (sin negativos ni decimales).
             ⚠️ Aun así puede quedar VACÍO → hay que validarlo en Kotlin. -->
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Cantidad (1-20, 10 % dto. desde 3):"
            android:textStyle="bold" />

        <EditText
            android:id="@+id/etCantidad"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="Nº de pizzas"
            android:inputType="number"
            android:text="1" />

        <!-- ───────── ACCIONES ───────── -->
        <Button
            android:id="@+id/btnCalcular"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Calcular" />

        <TextView
            android:id="@+id/tvTotal"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:text="Total: —"
            android:textSize="16sp" />

        <Button
            android:id="@+id/btnConfirmar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Confirmar pedido" />

    </LinearLayout>
</ScrollView>
```

#### `PedidoActivity.kt`
```kotlin
package com.tu.paquete

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PedidoActivity : AppCompatActivity() {

    // ── Datos fijos del enunciado ──────────────────────────────────
    // Arrays "paralelos": la pizza de la posición i cuesta precios[i].
    // Con la posición del Spinner sacamos el precio directamente, sin ningún if.
    private val pizzas = arrayOf("Margarita", "Barbacoa", "Cuatro Quesos", "Vegetal")
    private val precios = doubleArrayOf(8.0, 10.5, 11.0, 9.5)

    // ── Vistas que se usan en varias funciones (por eso van aquí arriba) ──
    private lateinit var spPizza: Spinner
    private lateinit var tvPrecioBase: TextView
    private lateinit var rgTamano: RadioGroup
    private lateinit var cbQueso: CheckBox
    private lateinit var cbBebida: CheckBox
    private lateinit var cbDomicilio: CheckBox
    private lateinit var etCantidad: EditText
    private lateinit var tvTotal: TextView

    // Usuario que ha iniciado sesión (llega del Login por el Intent)
    private var usuario = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedido)

        // 1. Recibir el dato del Login (misma clave exacta: "USUARIO")
        usuario = intent.getStringExtra("USUARIO") ?: ""

        // 2. Enlazar vistas
        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        spPizza = findViewById(R.id.spPizza)
        tvPrecioBase = findViewById(R.id.tvPrecioBase)
        rgTamano = findViewById(R.id.rgTamano)
        cbQueso = findViewById(R.id.cbQueso)
        cbBebida = findViewById(R.id.cbBebida)
        cbDomicilio = findViewById(R.id.cbDomicilio)
        etCantidad = findViewById(R.id.etCantidad)
        tvTotal = findViewById(R.id.tvTotal)
        val btnCalcular = findViewById<Button>(R.id.btnCalcular)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)

        tvSaludo.text = "¡Hola, $usuario! ¿Qué te apetece hoy?"

        // 3. Rellenar el Spinner (las 3 líneas de siempre)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pizzas)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spPizza.adapter = adapter

        // 4. Reaccionar en cuanto el usuario CAMBIA de pizza (sin pulsar ningún botón).
        //    Es una interfaz con 2 métodos → se usa "object :" y hay que sobrescribir LOS DOS.
        //    Android lo llama automáticamente una vez al arrancar (con la posición 0),
        //    así que el precio base ya aparece relleno al abrir la pantalla.
        spPizza.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // position = índice elegido (0, 1, 2...) → nos sirve para el array de precios
                tvPrecioBase.text = "Precio base: ${euros(precios[position])}"
                // ⚠️ Aquí dentro "this" es el listener, NO la Activity. Para un Toast:
                //    Toast.makeText(this@PedidoActivity, "...", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Obligatorio escribirlo aunque se quede vacío
            }
        }

        // 5. Botones
        btnCalcular.setOnClickListener { calcularTotal() }
        btnConfirmar.setOnClickListener { confirmarPedido() }
    }

    // ─────────────────────────────────────────────────────────────
    // 🧮 CÁLCULO (lo usan los DOS botones → no repetimos código)
    // ─────────────────────────────────────────────────────────────
    /**
     * Lee el formulario, valida, calcula y pinta el desglose en tvTotal.
     * Devuelve el total, o null si algo no es válido (y en ese caso ya avisa al usuario).
     */
    private fun calcularTotal(): Double? {
        // 1. Cantidad: toIntOrNull() devuelve null si está vacío o no es un número → nunca crashea
        val cantidad = etCantidad.text.toString().toIntOrNull()
        if (cantidad == null || cantidad !in 1..20) {
            etCantidad.error = "Introduce una cantidad entre 1 y 20"
            return null
        }
        // A partir de aquí Kotlin ya sabe que cantidad NO es null (smart cast)

        // 2. Tamaño: comparamos el id del RadioButton marcado con cada R.id
        val suplementoTamano = when (rgTamano.checkedRadioButtonId) {
            R.id.rbPequena -> 0.0
            R.id.rbMediana -> 2.0
            R.id.rbFamiliar -> 4.0
            else -> {   // -1 = ninguno marcado
                Toast.makeText(this, "Elige un tamaño", Toast.LENGTH_SHORT).show()
                return null
            }
        }

        // 3. Precio base según la posición elegida en el Spinner
        val precioBase = precios[spPizza.selectedItemPosition]

        // 4. Extras que se cobran POR PIZZA
        var extrasPorPizza = 0.0
        if (cbQueso.isChecked) extrasPorPizza += 1.5
        if (cbBebida.isChecked) extrasPorPizza += 2.0

        // 5. Cuentas
        val precioPorPizza = precioBase + suplementoTamano + extrasPorPizza
        val subtotal = precioPorPizza * cantidad
        val descuento = if (cantidad >= 3) subtotal * 0.10 else 0.0   // 10 % desde 3 pizzas
        val envio = if (cbDomicilio.isChecked) 3.0 else 0.0           // se cobra una vez por pedido
        val total = subtotal - descuento + envio

        // 6. Desglose en pantalla ("\n" = salto de línea)
        tvTotal.text = "Precio por pizza: ${euros(precioPorPizza)}\n" +
                "Subtotal (x$cantidad): ${euros(subtotal)}\n" +
                "Descuento: -${euros(descuento)}\n" +
                "Envío: ${euros(envio)}\n" +
                "TOTAL: ${euros(total)}"

        return total
    }

    /** Texto con los extras marcados, p. ej. "Extra queso, A domicilio". */
    private fun describirExtras(): String {
        val lista = mutableListOf<String>()
        if (cbQueso.isChecked) lista.add("Extra queso")
        if (cbBebida.isChecked) lista.add("Bebida")
        if (cbDomicilio.isChecked) lista.add("A domicilio")
        return if (lista.isEmpty()) "Sin extras" else lista.joinToString(", ")
    }

    // ─────────────────────────────────────────────────────────────
    // ✅ CONFIRMAR → INSERT pedido + SELECT/UPDATE puntos + Intent
    // ─────────────────────────────────────────────────────────────
    private fun confirmarPedido() {
        // Reutilizamos validación + cálculo.
        // Elvis + return: si calcularTotal() devuelve null, salimos de la función aquí mismo.
        val total = calcularTotal() ?: return

        val pizza = spPizza.selectedItem.toString()
        val tamano = findViewById<RadioButton>(rgTamano.checkedRadioButtonId).text.toString()
        val extrasTexto = describirExtras()
        val cantidad = etCantidad.text.toString().toInt()   // seguro: calcularTotal() ya lo validó
        val puntosGanados = total.toInt()                    // 1 punto por euro (toInt quita los decimales)

        val admin = AdminSQLiteOpenHelper(this, "pizzeria", null, 1)
        val bd = admin.writableDatabase

        // 1) INSERT del pedido. No ponemos "id": lo genera SQLite (AUTOINCREMENT)
        val pedido = ContentValues()
        pedido.put("usuario", usuario)
        pedido.put("pizza", pizza)
        pedido.put("tamano", tamano)
        pedido.put("extras", extrasTexto)
        pedido.put("cantidad", cantidad)
        pedido.put("total", total)
        val idPedido = bd.insert("pedidos", null, pedido)   // devuelve el id nuevo (Long)

        if (idPedido == -1L) {
            bd.close()   // ⚠️ cerrar también en el camino de error
            Toast.makeText(this, "Error al guardar el pedido", Toast.LENGTH_SHORT).show()
            return
        }

        // 2) UPDATE de puntos, "forma ContentValues": primero LEO los actuales y luego ESCRIBO la suma
        val cursor = bd.rawQuery("SELECT puntos FROM usuarios WHERE usuario = ?", arrayOf(usuario))
        var puntosActuales = 0
        if (cursor.moveToFirst()) {
            puntosActuales = cursor.getInt(0)   // 0 = primera columna del SELECT
        }
        cursor.close()

        val nuevosPuntos = ContentValues()
        nuevosPuntos.put("puntos", puntosActuales + puntosGanados)
        bd.update("usuarios", nuevosPuntos, "usuario = ?", arrayOf(usuario))
        bd.close()

        // 3) Ir al resumen con todos los datos.
        //    Dentro de .apply { } no hace falta repetir "intentResumen." en cada línea.
        val intentResumen = Intent(this, ResumenActivity::class.java).apply {
            putExtra("USUARIO", usuario)              // String
            putExtra("ID_PEDIDO", idPedido)           // Long   → allí getLongExtra
            putExtra("PIZZA", pizza)                  // String
            putExtra("TAMANO", tamano)                // String
            putExtra("EXTRAS", extrasTexto)           // String
            putExtra("CANTIDAD", cantidad)            // Int    → allí getIntExtra
            putExtra("TOTAL", total)                  // Double → allí getDoubleExtra
            putExtra("PUNTOS_GANADOS", puntosGanados) // Int
        }
        startActivity(intentResumen)
        // SIN finish(): queremos que Pedido siga en la pila para poder volver con "Nuevo pedido"

        limpiarFormulario()   // al volver, el formulario estará en blanco
    }

    /** Deja el formulario como recién abierto. */
    private fun limpiarFormulario() {
        spPizza.setSelection(0)          // primera pizza (también dispara onItemSelected)
        rgTamano.check(R.id.rbMediana)   // así se marca un radio desde código
        cbQueso.isChecked = false
        cbBebida.isChecked = false
        cbDomicilio.isChecked = false
        etCantidad.setText("1")
        tvTotal.text = "Total: —"
    }

    /** 12.5 → "12,50 €". Con el móvil en español, el separador decimal sale como coma. */
    private fun euros(cantidad: Double): String = "%.2f €".format(cantidad)
}
```

---

### 10.9 🧾 Pantalla 3: Resumen

#### `activity_resumen.xml`
```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:padding="20dp">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Resumen del pedido"
            android:textSize="22sp"
            android:textStyle="bold" />

        <TextView
            android:id="@+id/tvTicket"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Ticket..."
            android:textSize="16sp" />

        <TextView
            android:id="@+id/tvPuntos"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Puntos: -"
            android:textStyle="bold" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="📜 Tus pedidos:"
            android:textStyle="bold" />

        <!-- monospace: todas las letras miden lo mismo → el historial sale alineado -->
        <TextView
            android:id="@+id/tvHistorial"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="monospace"
            android:text="-" />

        <Button
            android:id="@+id/btnNuevoPedido"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="24dp"
            android:text="Nuevo pedido" />

        <Button
            android:id="@+id/btnCancelar"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Cancelar este pedido" />

        <Button
            android:id="@+id/btnCerrarSesion"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Cerrar sesión" />

    </LinearLayout>
</ScrollView>
```

#### `ResumenActivity.kt`
```kotlin
package com.tu.paquete

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog       // ⚠️ el de androidx.appcompat, no android.app
import androidx.appcompat.app.AppCompatActivity

class ResumenActivity : AppCompatActivity() {

    // Se usan en varias funciones → propiedades de la clase
    private var usuario = ""
    private var idPedido = -1L
    private var puntosGanados = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumen)

        // 1. Enlazar vistas
        val tvTicket = findViewById<TextView>(R.id.tvTicket)
        val tvPuntos = findViewById<TextView>(R.id.tvPuntos)
        val tvHistorial = findViewById<TextView>(R.id.tvHistorial)
        val btnNuevoPedido = findViewById<Button>(R.id.btnNuevoPedido)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)

        // 2. Recibir datos. ⚠️ MISMA clave y MISMO tipo que en el putExtra:
        //    ID_PEDIDO se envió como Long → getLongExtra.
        //    Si lo lees con getIntExtra, te devuelve el valor por defecto y no da ningún error.
        usuario = intent.getStringExtra("USUARIO") ?: ""
        idPedido = intent.getLongExtra("ID_PEDIDO", -1L)
        val pizza = intent.getStringExtra("PIZZA") ?: ""
        val tamano = intent.getStringExtra("TAMANO") ?: ""
        val extras = intent.getStringExtra("EXTRAS") ?: ""
        val cantidad = intent.getIntExtra("CANTIDAD", 0)
        val total = intent.getDoubleExtra("TOTAL", 0.0)
        puntosGanados = intent.getIntExtra("PUNTOS_GANADOS", 0)

        // 3. Pintar el ticket
        tvTicket.text = "🧾 Pedido nº $idPedido\n" +
                "Cliente: $usuario\n" +
                "$cantidad x $pizza ($tamano)\n" +
                "Extras: $extras\n" +
                "TOTAL: ${"%.2f".format(total)} €\n" +
                "Has ganado $puntosGanados puntos"

        // 4. Datos que salen de la BD
        tvPuntos.text = "⭐ Puntos acumulados: ${leerPuntos()}"
        tvHistorial.text = cargarHistorial()

        // 5. Botones
        btnNuevoPedido.setOnClickListener {
            finish()   // Pedido sigue debajo en la pila → volvemos a él
        }

        btnCancelar.setOnClickListener {
            // Pedir confirmación antes de borrar (detalle que suele sumar nota)
            AlertDialog.Builder(this)
                .setTitle("Cancelar pedido")
                .setMessage("¿Seguro que quieres cancelar el pedido nº $idPedido?")
                .setPositiveButton("Sí, cancelar") { _, _ -> cancelarPedido() }
                .setNegativeButton("No", null)   // null = solo cierra el diálogo
                .show()                          // ⚠️ como el Toast: sin .show() no aparece
        }

        btnCerrarSesion.setOnClickListener {
            val intentLogin = Intent(this, LoginActivity::class.java)
            // NEW_TASK + CLEAR_TASK = vacía TODA la pila y deja solo el Login
            intentLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentLogin)
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 🔵 SELECT de una sola fila y una sola columna
    // ─────────────────────────────────────────────────────────────
    private fun leerPuntos(): Int {
        val admin = AdminSQLiteOpenHelper(this, "pizzeria", null, 1)
        val bd = admin.readableDatabase
        val cursor = bd.rawQuery("SELECT puntos FROM usuarios WHERE usuario = ?", arrayOf(usuario))
        val puntos = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        bd.close()
        return puntos
    }

    // ─────────────────────────────────────────────────────────────
    // 🔵 SELECT de VARIAS filas → bucle con moveToNext()
    // ─────────────────────────────────────────────────────────────
    private fun cargarHistorial(): String {
        val admin = AdminSQLiteOpenHelper(this, "pizzeria", null, 1)
        val bd = admin.readableDatabase

        // ORDER BY id DESC → los pedidos más nuevos primero
        val cursor = bd.rawQuery(
            "SELECT id, pizza, cantidad, total FROM pedidos WHERE usuario = ? ORDER BY id DESC",
            arrayOf(usuario)
        )

        val texto = StringBuilder()   // para ir pegando líneas de forma eficiente
        var numPedidos = 0
        var gastado = 0.0

        // moveToNext() pasa a la siguiente fila y devuelve false cuando ya no quedan.
        // (Con moveToFirst() solo leerías la primera fila.)
        while (cursor.moveToNext()) {
            // El índice es la POSICIÓN de la columna en el SELECT, no en la tabla:
            // 0 = id, 1 = pizza, 2 = cantidad, 3 = total
            val id = cursor.getLong(0)
            val pizza = cursor.getString(1)
            val cant = cursor.getInt(2)
            val tot = cursor.getDouble(3)
            // Alternativa sin contar posiciones: cursor.getString(cursor.getColumnIndexOrThrow("pizza"))

            texto.append("#$id  ${cant}x $pizza  →  ${"%.2f".format(tot)} €\n")
            numPedidos++
            gastado += tot
        }
        cursor.close()
        bd.close()

        texto.append("\n$numPedidos pedido(s) · Total gastado: ${"%.2f".format(gastado)} €")
        return texto.toString()
    }

    // ─────────────────────────────────────────────────────────────
    // 🔴 DELETE del pedido + 🟡 UPDATE de puntos con execSQL
    // ─────────────────────────────────────────────────────────────
    private fun cancelarPedido() {
        val admin = AdminSQLiteOpenHelper(this, "pizzeria", null, 1)
        val bd = admin.writableDatabase

        // delete devuelve CUÁNTAS filas ha borrado.
        // Los argumentos del WHERE siempre son String → idPedido.toString()
        val borrados = bd.delete("pedidos", "id = ?", arrayOf(idPedido.toString()))

        if (borrados > 0) {
            // UPDATE, "forma execSQL": la propia SQL hace la resta, sin leer antes.
            // Muy cómodo para "columna = columna ± algo".
            bd.execSQL(
                "UPDATE usuarios SET puntos = puntos - ? WHERE usuario = ?",
                arrayOf<Any>(puntosGanados, usuario)
            )
            Toast.makeText(this, "Pedido nº $idPedido cancelado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No se encontró el pedido", Toast.LENGTH_SHORT).show()
        }
        bd.close()
        finish()   // volvemos al formulario de pedido
    }
}
```

---

### 10.10 📋 `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- El theme se llama como tu proyecto (Theme.PizzApp, Theme.Practica5...): no lo cambies -->
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.PizzApp">

        <!-- La Activity con MAIN + LAUNCHER es la que se abre al tocar el icono.
             ⚠️ Solo UNA. Si la plantilla lo puso en MainActivity, muévelo aquí.
             exported="true" es OBLIGATORIO en la que tiene intent-filter. -->
        <activity
            android:name=".LoginActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Las demás: exported="false" (solo se abren desde nuestra propia app) -->
        <activity
            android:name=".PedidoActivity"
            android:exported="false" />

        <activity
            android:name=".ResumenActivity"
            android:exported="false" />

    </application>
</manifest>
```

> 💡 Si la plantilla creó una `MainActivity` que no vas a usar, bórrala (el `.kt`, el `.xml` y su `<activity>`) o simplemente quítale el `intent-filter`.

---

### 10.11 🧪 Tabla de pruebas (pásala ENTERA antes de entregar)

| # | Pantalla | Qué haces | Resultado esperado |
|---|---|---|---|
| 1 | Login | Pulsar *Registrarse* con los campos vacíos | Toast "Rellena usuario y contraseña" |
| 2 | Login | Registrar `ana` / `12` | Aviso rojo en contraseña: "Mínimo 4 caracteres" |
| 3 | Login | Registrar `ana` / `1234` | Toast "¡Registrado! Ya puedes entrar" |
| 4 | Login | Registrar `ana` otra vez | Toast "El usuario 'ana' ya existe" |
| 5 | Login | Entrar con `ana` / `9999` | Toast "Usuario o contraseña incorrectos" |
| 6 | Login | Entrar con `ana` / `1234` | Se abre Pedido con "¡Hola, ana!" |
| 7 | Pedido | Cambiar de pizza en el Spinner | "Precio base" cambia al instante |
| 8 | Pedido | Cantidad vacía, `0` o `25` → *Calcular* | Aviso rojo en cantidad, no calcula |
| 9 | Pedido | Margarita · Pequeña · sin extras · 1 → *Calcular* | **TOTAL: 8,00 €** |
| 10 | Pedido | Barbacoa · Familiar · Extra queso · Domicilio · 3 → *Calcular* | Por pizza 16,00 · Subtotal 48,00 · Descuento -4,80 · Envío 3,00 · **TOTAL: 46,20 €** |
| 11 | Pedido | *Confirmar* el caso anterior | Se abre el Resumen: pedido nº X, "Has ganado 46 puntos" |
| 12 | Resumen | Mirar el historial | Aparece el pedido y el total gastado |
| 13 | Resumen | *Nuevo pedido* | Vuelve al formulario **limpio** |
| 14 | Resumen | *Cancelar* → "No" | No pasa nada |
| 15 | Resumen | *Cancelar* → "Sí, cancelar" | Toast "Pedido nº X cancelado" y vuelve a Pedido. En el siguiente resumen ya no aparece y los puntos han bajado |
| 16 | Resumen | *Cerrar sesión* y luego Atrás | Se sale de la app (no vuelve al Resumen) |
| 17 | Pedido | Pulsar Atrás | Se sale de la app (el Login hizo `finish()`) |
| 18 | — | Cerrar la app del todo y volver a abrirla | `ana` sigue existiendo (SQLite guarda en disco) |

> 🧮 **Cuenta del caso 10, a mano:** 10,50 (Barbacoa) + 4 (Familiar) + 1,50 (queso) = **16 €/pizza** → ×3 = **48 €** → −10 % = −4,80 → +3 de envío = **46,20 €** → 46 puntos.

---

### 10.12 🔄 Variantes que te pueden pedir (y cómo adaptar el ejemplo)

#### A. "Añade un botón para cambiar la contraseña" (UPDATE)
```kotlin
// En LoginActivity. etNueva sería otro EditText del XML.
private fun cambiarContrasena() {
    val usuario = etUsuario.text.toString().trim()
    val nueva = etNueva.text.toString().trim()
    if (usuario.isEmpty() || nueva.length < 4) {
        Toast.makeText(this, "Usuario y nueva contraseña (mín. 4)", Toast.LENGTH_SHORT).show()
        return
    }
    val bd = AdminSQLiteOpenHelper(this, "pizzeria", null, 1).writableDatabase
    val registro = ContentValues()
    registro.put("contrasena", cifrar(nueva))   // ⚠️ también cifrada
    val filas = bd.update("usuarios", registro, "usuario = ?", arrayOf(usuario))
    bd.close()
    val msg = if (filas > 0) "Contraseña cambiada" else "Ese usuario no existe"
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
```

#### B. "Darse de baja" (DELETE del usuario y de todos sus pedidos)
```kotlin
val bd = AdminSQLiteOpenHelper(this, "pizzeria", null, 1).writableDatabase
bd.delete("pedidos", "usuario = ?", arrayOf(usuario))                  // primero sus pedidos
val borrados = bd.delete("usuarios", "usuario = ?", arrayOf(usuario))  // luego el usuario
bd.close()
```

#### C. "Usa César en vez de Base64"
Copia `cifradoCesar()` del **Bloque 6.3** y cambia solo el cuerpo de `cifrar()`:
```kotlin
private fun cifrar(texto: String): String = cifradoCesar(texto)
```
> ⚠️ Los usuarios que registraste con Base64 ya no podrán entrar → desinstala la app y vuelve a registrarlos.

#### D. "Que el total se recalcule solo, sin botón Calcular"
```kotlin
// En onCreate, después de enlazar las vistas:
rgTamano.setOnCheckedChangeListener { _, _ -> calcularTotal() }
cbQueso.setOnCheckedChangeListener { _, _ -> calcularTotal() }
cbBebida.setOnCheckedChangeListener { _, _ -> calcularTotal() }
cbDomicilio.setOnCheckedChangeListener { _, _ -> calcularTotal() }
// y dentro de onItemSelected del Spinner añade también: calcularTotal()
```

#### E. "Pasa una lista entre pantallas" (como las respuestas de la Práctica 5)
```kotlin
// Enviar
val lista = arrayListOf("Extra queso", "Bebida")
intentResumen.putStringArrayListExtra("LISTA_EXTRAS", lista)

// Recibir
val listaRecibida = intent.getStringArrayListExtra("LISTA_EXTRAS") ?: arrayListOf()
```

#### F. "Cuenta los pedidos con SQL en vez de con un bucle"
```kotlin
val c = bd.rawQuery("SELECT COUNT(*), SUM(total) FROM pedidos WHERE usuario = ?", arrayOf(usuario))
if (c.moveToFirst()) {
    val numPedidos = c.getInt(0)
    val gastado = c.getDouble(1)   // si no hay filas, SUM da NULL y getDouble devuelve 0.0
}
c.close()
```

#### G. Truco pro: claves del Intent como constantes (adiós al Error #7)
```kotlin
// Al principio de ResumenActivity.kt, FUERA de la clase:
const val EXTRA_USUARIO = "USUARIO"
const val EXTRA_TOTAL = "TOTAL"

// En cualquier pantalla del mismo paquete:
putExtra(EXTRA_USUARIO, usuario)               // al enviar
intent.getStringExtra(EXTRA_USUARIO)           // al recibir
// Si escribes mal el nombre, NO compila → te enteras al momento, no en el examen
```

#### H. "Muestra el resultado en la misma pantalla" (sin segunda Activity)
Quita el `Intent` de `confirmarPedido()` y escribe el ticket en un `TextView` de la propia pantalla: `tvResultado.text = "Pedido nº $idPedido guardado..."`.

---

### 10.13 🐞 Depurar cuando algo falla

| Síntoma | Causa probable | Qué hacer |
|---|---|---|
| La app se cierra sola | Excepción | **Logcat** → busca `FATAL EXCEPTION`; la primera línea `at com.tu.paquete...` te da archivo y línea exactos |
| `no such table` / `no such column` | La BD se creó con un `CREATE TABLE` anterior | Desinstala la app del emulador o sube la versión del helper |
| Crash al abrir una pantalla | Activity no registrada **o** `findViewById` de un id que está en OTRO XML (devuelve null) | Revisa el Manifest y que cada id esté en el layout del `setContentView` de esa pantalla |
| Crash nada más arrancar en `ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main))` | La plantilla espera que la raíz del XML tenga `android:id="@+id/main"` y la cambiaste | Pon ese id en la raíz del XML o borra ese bloque de código |
| El título queda debajo de la barra de estado (Android 15+) | Edge-to-edge obligatorio | Mantén el bloque `ViewCompat...` que trae la plantilla (y el id `main`) |
| Login siempre "incorrecto" | Nombre de BD distinto en dos pantallas, o cifrado distinto al registrar y al entrar | Revisa que todo use `"pizzeria", null, 1` y la misma función `cifrar()` |
| Llega 0 / null en la segunda pantalla | Clave distinta o tipo distinto (`putExtra` Long → `getIntExtra`) | Copia y pega la clave; usa el `get...Extra` del mismo tipo |
| Error raro de tipos con `Base64` o `Text` | Alt+Enter importó la clase equivocada (`java.util.Base64`, `org.w3c.dom.Text`) | Revisa los `import` de arriba del archivo |

**Herramientas:**
- **Log**: `Log.d("PIZZAPP", "total = $total")` (con `import android.util.Log`) y en Logcat filtras por `PIZZAPP`.
- **Database Inspector**: *View → Tool Windows → App Inspection → Database Inspector*, con la app ejecutándose en el emulador. Ves las tablas y las filas en directo, e incluso puedes lanzar SQL.

---

### 10.14 🎯 Lo que el profe va a mirar (y este ejemplo cumple)

- ✅ **No crashea nunca**: números con `toIntOrNull()`, campos vacíos validados, división y rangos controlados.
- ✅ **Avisa al usuario** de cada error con un mensaje claro (`Toast` o `.error` en el campo).
- ✅ **SQL seguro**: siempre `?` + `arrayOf(...)`, nunca concatenar variables en la consulta.
- ✅ **Recursos cerrados**: cada `rawQuery` tiene su `cursor.close()` y cada BD su `bd.close()`, también en los caminos de error.
- ✅ **Contraseña cifrada** al registrar **y** al comprobar.
- ✅ **Sin código repetido**: `calcularTotal()` la usan los dos botones; cada botón llama a una función con nombre claro.
- ✅ **Navegación correcta**: `finish()` donde toca y `CLEAR_TASK` al cerrar sesión.
- ✅ **Nombres claros**: `etCantidad`, `rgTamano`, `cbQueso`... se entiende sin mirar el XML.
- ✅ **Comentado** en las partes clave, sin comentar lo obvio.

---

### 10.15 ✅ Checklist final antes de entregar

- [ ] Todas las Activities están en el Manifest y el LAUNCHER está en la correcta
- [ ] Cada `Toast` y cada `AlertDialog` acaban en `.show()`
- [ ] Todos los números se leen con `toIntOrNull()` / `toDoubleOrNull()` y se validan
- [ ] Los RadioButton tienen id y están dentro de su RadioGroup
- [ ] CheckBox con `.isChecked` (nunca `.isSelected`)
- [ ] El Spinner tiene su `adapter` asignado
- [ ] Claves del Intent idénticas, y el mismo tipo en `put` y en `get`
- [ ] SQL con `?`, nunca con `'$variable'`
- [ ] `cursor.close()` y `bd.close()` en todos los caminos (también en el `else` y en los `return` de error)
- [ ] Mismo nombre y versión de BD en todas las pantallas
- [ ] Probado tras **desinstalar** la app (BD creada desde cero)
- [ ] Sin imports en gris ni código muerto (*Code → Optimize Imports* o `Ctrl+Alt+O` / `⌃⌥O`)
- [ ] Pasada la tabla de pruebas 10.11 entera
