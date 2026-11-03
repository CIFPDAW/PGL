# Práctica 4 - Login

**Asignatura:** Programación Multimedia y Dispositivos Móviles (PGL)
**Centro:** CIFP César Manrique
**Curso / Grupo:** 2º DAM B
**Alumno:** Francisco Daniel Darias González

---

## Índice
1. [Descripción de la Actividad](#1-descripción-de-la-actividad)
2. [Layouts (Diseño de la Interfaz)](#2-layouts-diseño-de-la-interfaz)
   - [2.1 Pantalla de Login (`activity_login.xml`)](#21-pantalla-de-login-activity_loginxml)
   - [2.2 Pantalla Principal / Encriptación (`activity_main.xml`)](#22-pantalla-principal--encriptación-activity_mainxml)
   - [2.3 Pantalla de Resultado (`activity_result.xml`)](#23-pantalla-de-resultado-activity_resultxml)
3. [Código Base Kotlin (Lógica)](#3-código-base-kotlin-lógica)
   - [3.1 Base de Datos (`DatabaseHelper.kt`)](#31-base-de-datos-databasehelperkt)
   - [3.2 Lógica del Login (`LoginActivity.kt`)](#32-lógica-del-login-loginactivitykt)
   - [3.3 Lógica de Encriptación (`MainActivity.kt`)](#33-lógica-de-encriptación-mainactivitykt)
   - [3.4 Lógica de Resultado (`ResultActivity.kt`)](#34-lógica-de-resultado-resultactivitykt)
4. [Visualización](#4-visualización)

---

## 1. Descripción de la Actividad
Esta práctica tiene como objetivo ampliar la aplicación de la Práctica 3 (una herramienta para encriptar y desencriptar textos) mediante la implementación de un sistema de autenticación de usuarios.

Para ello, la aplicación ahora inicia en una nueva actividad de Login. Desde esta pantalla inicial, el usuario puede:
- Iniciar sesión validando sus credenciales contra una base de datos local (SQLite).
- Registrar una cuenta nueva, validando que la contraseña cumpla con requisitos de seguridad (mínimo 8 caracteres, números y al menos una letra mayúscula).
- Modificar la contraseña de una cuenta existente.
- Borrar de forma definitiva una cuenta.

Solo si el inicio de sesión o el registro son exitosos, el usuario es redirigido a la pantalla donde podrá encriptar o desencriptar textos.

---

## 2. Layouts (Diseño de la Interfaz)

### 2.1 Pantalla de Login (`activity_login.xml`)

Esta pantalla recoge las credenciales del usuario y ofrece todos los botones de acción.

#### 2.1.1 Contenedor Principal
```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="32dp"
    android:gravity="center"
    android:id="@+id/main">
```
- **`LinearLayout` con `orientation="vertical"`**: Agrupa los elementos uno debajo del otro en forma de columna.
- **`gravity="center"`**: Fuerza a que todo el contenido (campos de texto y botones) se posicione en el centro de la pantalla.
- **`padding="32dp"`**: Aplica un margen interior generoso para que los elementos no se peguen a los bordes del dispositivo.

#### 2.1.2 Campos de Texto (Usuario y Contraseña)
```xml
    <EditText
        android:id="@+id/etUsername"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/username"
        android:inputType="text"
        android:layout_marginBottom="16dp"/>

    <EditText
        android:id="@+id/etPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/password"
        android:inputType="textPassword"
        android:layout_marginBottom="24dp"/>
```
- **`android:id="@+id/etUsername"` / `etPassword`**: Identificadores únicos para recuperar los valores en Kotlin.
- **`android:hint`**: Muestra un texto atenuado ("NOMBRE DE USUARIO", "CONTRASEÑA") cuando el campo está vacío.
- **`android:inputType="textPassword"`**: Fundamental para la seguridad; oculta los caracteres que el usuario introduce reemplazándolos por puntos o asteriscos.
- **`layout_marginBottom`**: Añade una separación inferior para que no colisionen con los elementos de abajo.

#### 2.1.3 Botones de la Aplicación
```xml
    <Button
        android:id="@+id/btnLogin"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/login"/>

    <Button
        android:id="@+id/btnSignIn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/sign_in"/>

    <!-- Se repite la misma estructura para btnModify y btnDelete -->
```
- **`layout_width="match_parent"`**: Hace que todos los botones se expandan a lo ancho del contenedor, manteniendo una simetría visual.
- **`android:text`**: Extrae el nombre del botón desde el archivo `strings.xml` para facilitar traducciones.

---

### 2.2 Pantalla Principal / Encriptación (`activity_main.xml`)

#### 2.2.1 Entrada de texto a procesar
```xml
    <EditText
        android:id="@+id/etInput"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/text_to_process"
        android:inputType="textMultiLine"
        android:minLines="3"
        android:layout_marginBottom="16dp"/>
```
- **`android:inputType="textMultiLine"`**: Permite al usuario pulsar "Intro" en el teclado y escribir párrafos grandes.
- **`android:minLines="3"`**: Mantiene la caja de texto visualmente con un tamaño de 3 líneas incluso si está vacía, indicando al usuario que es un área para texto largo.

#### 2.2.2 Menú Desplegable (Spinner)
```xml
    <Spinner
        android:id="@+id/spinnerMethod"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="24dp"/>
```
- **`Spinner`**: Este control nativo muestra un menú desplegable. No lleva texto definido en el XML porque sus opciones se inyectan dinámicamente mediante un `Adapter` desde el código Kotlin.

#### 2.2.3 Botones de Acción
```xml
    <Button
        android:id="@+id/btnEncrypt"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/encrypt"/>

    <Button
        android:id="@+id/btnDecrypt"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/decrypt"/>
```
- Botones básicos estructurados de igual forma para activar la lógica de cifrado o descifrado.

---

### 2.3 Pantalla de Resultado (`activity_result.xml`)

#### 2.3.1 Textos de Resultados
```xml
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/result"
        android:textSize="20sp"
        android:textStyle="bold"
        android:layout_marginBottom="16dp"/>

    <TextView
        android:id="@+id/tvResult"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:layout_marginBottom="32dp"
        android:textAlignment="center"/>
```
- El primer `TextView` es simplemente decorativo ("Resultado:") y usa `textStyle="bold"` para actuar como título.
- El segundo `TextView` (`tvResult`) será el contenedor dinámico de la respuesta.
- **`textAlignment="center"`**: Fuerza a que el texto resultante aparezca perfectamente centrado.

#### 2.3.2 Botón de Regreso
```xml
    <Button
        android:id="@+id/btnBack"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="@string/back"/>
```
- Botón final asignado a la acción de cerrar la actividad y volver atrás.

---

## 3. Código Base Kotlin (Lógica)

### 3.1 Base de Datos (`DatabaseHelper.kt`)

Esta clase es un puente entre nuestra aplicación y el motor de SQLite del teléfono.

#### 3.1.1 Configuración inicial y constantes
```kotlin
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "practica4.db"
        const val DATABASE_VERSION = 1
        const val TABLE_USERS = "USUARIOS"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
    }
```
- **`SQLiteOpenHelper`**: Al heredar de esta clase, Android asume el trabajo pesado de abrir y guardar el archivo de la base de datos.
- **`companion object`**: Permite definir constantes estáticas, lo que evita errores tipográficos al llamar al nombre de la tabla o columnas repetidas veces en el resto del proyecto.

#### 3.1.2 Creación y actualización
```kotlin
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_USERNAME TEXT PRIMARY KEY,"
                + "$COLUMN_PASSWORD TEXT"
                + ")")
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }
}
```
- **`onCreate()`**: Lanza la sentencia `CREATE TABLE` la primera vez que se inicia la app. Definimos `username` como `PRIMARY KEY` (Clave Primaria), haciendo imposible registrar dos usuarios con el mismo nombre a nivel de base de datos.
- **`onUpgrade()`**: Si en el futuro subimos el `DATABASE_VERSION`, este método destruirá la tabla antigua y la volverá a crear limpia.

---

### 3.2 Lógica del Login (`LoginActivity.kt`)

#### 3.2.1 Inicialización y vinculación de Vistas
```kotlin
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        // ... (se hace lo mismo con el resto de botones)
```
- Instanciamos el `DatabaseHelper` pasándole `this` (el contexto de la actividad).
- Enlazamos los componentes de la interfaz XML mediante su ID utilizando `findViewById`.

#### 3.2.2 Evento: Botón LOGIN (Iniciar Sesión)
```kotlin
        btnLogin.setOnClickListener {
            val user = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USERNAME}=? AND ${DatabaseHelper.COLUMN_PASSWORD}=?",
                arrayOf(user, pass)
            )

            if (cursor.moveToFirst()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No se le ha podido autenticar", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }
```
- **`.trim()`**: Elimina espacios en blanco accidentales que el usuario haya puesto al principio o al final de las credenciales.
- **`db.rawQuery(...)`**: Ejecuta una sentencia SQL pura. Utilizamos las incógnitas `?` para inyectar los arrays de manera segura y evitar inyecciones SQL.
- **`cursor.moveToFirst()`**: El `Cursor` es el puntero a los resultados. Si `moveToFirst()` devuelve `true`, significa que encontró al menos 1 coincidencia en la BD, por tanto, las credenciales son correctas y se lanza un `Intent` para ir a la `MainActivity`.

#### 3.2.3 Evento: Botón SIGN IN (Registro)
```kotlin
        btnSignIn.setOnClickListener {
            // ... (validación de campos vacíos)

            if (!isValidPassword(pass)) {
                Toast.makeText(this, "La contraseña debe tener mínimo 8 caracteres, letras, números y una mayúscula.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USERNAME}=?", arrayOf(user))

            if (cursor.moveToFirst()) {
                Toast.makeText(this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show()
            } else {
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_USERNAME, user)
                    put(DatabaseHelper.COLUMN_PASSWORD, pass)
                }
                val newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values)
                if (newRowId != -1L) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            cursor.close()
        }
```
- Verifica si la contraseña pasa la prueba de robustez mediante `isValidPassword()`.
- Hace una consulta SQL para comprobar si el usuario ya existe en la base de datos (para no machacarlo).
- **`ContentValues`**: Es un mapa clave-valor donde introducimos el nombre de las columnas y el dato a guardar.
- **`db.insert(...)`**: Inserta la nueva fila en la base de datos. Si el proceso es exitoso y no devuelve `-1`, se abre la aplicación directamente.

#### 3.2.4 Evento: Botón MODIFICAR CONTRASEÑA
```kotlin
        btnModify.setOnClickListener {
            // ... (validaciones de campos vacíos y contraseña robusta)

            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USERNAME}=?", arrayOf(user))

            if (cursor.moveToFirst()) {
                val oldPass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
                if (oldPass == pass) {
                    Toast.makeText(this, "La contraseña debe ser diferente a la actual", Toast.LENGTH_SHORT).show()
                } else {
                    val values = ContentValues().apply { put(DatabaseHelper.COLUMN_PASSWORD, pass) }
                    val selection = "${DatabaseHelper.COLUMN_USERNAME} LIKE ?"
                    val selectionArgs = arrayOf(user)
                    db.update(DatabaseHelper.TABLE_USERS, values, selection, selectionArgs)
                    Toast.makeText(this, "Contraseña modificada correctamente", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }
```
- **`cursor.getString(...)`**: Recupera el valor de la contraseña vieja que está actualmente registrada en la base de datos para verificar que el usuario no ponga la misma.
- **`db.update(...)`**: Si todo está correcto, invoca el comando de actualización pasando los `ContentValues` (nueva contraseña) y una cláusula `WHERE` definida en `selection`.

#### 3.2.5 Evento: Botón BORRAR CUENTA
```kotlin
        btnDelete.setOnClickListener {
            // ... (validaciones de campos vacíos)
            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USERNAME}=? AND ${DatabaseHelper.COLUMN_PASSWORD}=?", arrayOf(user, pass))

            if (cursor.moveToFirst()) {
                val selection = "${DatabaseHelper.COLUMN_USERNAME} LIKE ?"
                val selectionArgs = arrayOf(user)
                db.delete(DatabaseHelper.TABLE_USERS, selection, selectionArgs)
                Toast.makeText(this, "Cuenta borrada con éxito", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }
```
- Es importante que primero verifique con `rawQuery` que tanto el usuario como la contraseña **coinciden** antes de borrar.
- **`db.delete(...)`**: Método interno de Android para ejecutar el borrado de una fila concreta en SQL.

#### 3.2.6 Función Auxiliar de validación de contraseña
```kotlin
    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        var hasUpper = false
        var hasDigit = false
        for (char in password) {
            if (char.isUpperCase()) hasUpper = true
            if (char.isDigit()) hasDigit = true
        }
        return hasUpper && hasDigit
    }
```
- Si la longitud es menor de 8, devuelve automáticamente `false`.
- Recorre en un bucle for cada letra de la contraseña; si detecta una mayúscula activa la variable `hasUpper` y si detecta un número activa `hasDigit`. Ambas deben ser `true` al final.

---

### 3.3 Lógica de Encriptación (`MainActivity.kt`)

#### 3.3.1 Rellenado del Spinner
```kotlin
        val methods = arrayOf(
            getString(R.string.method_caesar),
            getString(R.string.method_reverse),
            getString(R.string.method_base64)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, methods)
        spinnerMethod.adapter = adapter
```
- Se crea un array con el texto de las 3 opciones de encriptación.
- Se vincula el array con un `ArrayAdapter` para darle formato visual, y este a su vez se asocia a la vista del Spinner mediante `spinnerMethod.adapter = adapter`.

#### 3.3.2 Eventos para Procesar y Enviar Datos
```kotlin
        btnEncrypt.setOnClickListener {
            val input = etInput.text.toString()
            if (input.isEmpty()) return@setOnClickListener

            val method = spinnerMethod.selectedItemPosition
            val result = processText(input, method, true)

            val intent = Intent(this, ResultActivity::class.java).apply {
                putExtra("RESULT_TEXT", result)
            }
            startActivity(intent)
        }
```
- **`selectedItemPosition`**: Devuelve `0, 1 o 2` dependiendo del método que el usuario seleccionó en el desplegable.
- **`processText()`**: Envía el texto a una función que calcula el resultado pasándole `true` si es encriptación y `false` en caso de que lo active el botón `btnDecrypt`.
- **`putExtra("RESULT_TEXT", result)`**: Incrusta un mensaje (como si fuera un paquete) con nombre `RESULT_TEXT` dentro del Intent para enviarlo a la otra actividad.

#### 3.3.3 Función Switch para procesar
```kotlin
    private fun processText(input: String, method: Int, encrypt: Boolean): String {
        return when (method) {
            0 -> caesarCipher(input, if (encrypt) 3 else -3)
            1 -> input.reversed()
            2 -> if (encrypt) {
                Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)
            } else {
                try {
                    String(Base64.decode(input, Base64.DEFAULT))
                } catch (_: Exception) { "Error al decodificar Base64" }
            }
            else -> input
        }
    }
```
- Actúa como un semáforo (mediante la estructura `when` equivalente a un `switch`).
- **`input.reversed()`**: El método de invertir el texto utiliza la función nativa que da la vuelta a un string.
- **`Base64`**: Encripta transformando a bytes y desencripta leyendo esos bytes. Tiene un `try-catch` para que la app no colapse si se intenta desencriptar algo que no sea Base64.

---

### 3.4 Lógica de Resultado (`ResultActivity.kt`)

#### 3.4.1 Recepción de datos y Retroceso
```kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val tvResult = findViewById<TextView>(R.id.tvResult)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Recepción del texto extra del intent
        val resultText = intent.getStringExtra("RESULT_TEXT")
        tvResult.text = resultText

        // Volver atrás
        btnBack.setOnClickListener {
            finish()
        }
    }
```
- **`intent.getStringExtra("RESULT_TEXT")`**: Captura el String que le adjuntamos al Intent en la actividad anterior e inyecta ese valor en el cuadro de texto.
- **`finish()`**: Este método del botón destruye la `ResultActivity` actual de la pila de memoria, provocando que Android vuelva a la pantalla que estaba debajo (la pantalla de encriptar).

---

## 4. Visualización

*(Aquí se deben insertar capturas de pantalla de la aplicación en ejecución mostrando la funcionalidad del Login y los Toast informativos, así como la herramienta de encriptar y la vista de resultados).*

