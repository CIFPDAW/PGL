# Guía de Referencia: Android Studio y Kotlin

Este documento sirve como guía rápida para entender los conceptos básicos de la creación de interfaces de usuario en Android Studio y la sintaxis fundamental del lenguaje Kotlin. 

---

## 📱 PARTE 1: Interfaz de Usuario y Eventos (Android Studio)

Esta sección explica cómo construir una interfaz visual básica y cómo hacer que sus elementos interactúen con el código Kotlin.

### 1. Elementos Visuales Básicos
Los elementos visuales (Vistas) se añaden desde la paleta (`Palette`) al archivo de diseño `activity_main.xml`.
*   **EditText (Number):** Cuadros de texto para que el usuario ingrese datos. Propiedad clave: `hint` (texto de ayuda que desaparece al escribir).
*   **Button:** Botón para ejecutar acciones. Propiedad clave: `text` (el texto que muestra el botón).
*   **TextView:** Etiqueta para mostrar texto estático o resultados.

### 2. Layout y Constraints (Anclajes)
Para que los elementos no se desordenen al ejecutar la app, deben estar anclados (Constraints).
*   Se anclan arrastrando los círculos de los bordes del elemento hacia los bordes de la pantalla u otros elementos.
*   **Infer Constraints:** Botón automático (varita mágica) que deduce y aplica los anclajes necesarios.
*   **ID:** Cada elemento debe tener un `id` único (ej. `et1`, `button`, `tv1`) para poder invocarlo desde el código Kotlin.

### 3. Conectar XML con Kotlin y Capturar Clics
Existen dos formas principales de capturar el clic de un botón y ejecutar una acción:

#### Opción A: Usando `setOnClickListener` (Recomendado)
Desde el archivo `MainActivity.kt`, buscamos el elemento por su ID y le asignamos un "escuchador".

```kotlin
// 1. Enlazar el objeto visual con una variable en Kotlin
val et1 = findViewById<EditText>(R.id.et1)
val tv1 = findViewById<TextView>(R.id.tv1)
val button = findViewById<Button>(R.id.button)

// 2. Capturar el clic
button.setOnClickListener {
    // Código que se ejecuta al pulsar el botón
    val nro1 = et1.text.toString().toInt() // Capturar texto y convertir a entero
    tv1.text = "Resultado: ${nro1}" // Mostrar resultado en el TextView
}
```

#### Opción B: Usando la propiedad `onClick` del XML
Se crea una función en el código Kotlin y se enlaza directamente en las propiedades del botón en la interfaz gráfica.

```kotlin
// 1. Declarar variables para inicializar más tarde (lateinit)
lateinit var et1: EditText
lateinit var tv1: TextView

override fun onCreate(savedInstanceState: Bundle?) {
    // ...
    et1 = findViewById(R.id.et1)
    tv1 = findViewById(R.id.tv1)
}

// 2. Crear la función que recibe una Vista (View)
fun sumarNumeros(v: View?) {
    val resultado = et1.text.toString().toInt()
    tv1.text = "Resultado: $resultado"
}
```
*En el archivo XML, se busca la propiedad `onClick` del botón y se selecciona la función `sumarNumeros`.*

---

## 💻 PARTE 2: Sintaxis Básica de Kotlin

Kotlin es un lenguaje seguro, moderno y conciso. Aquí tienes las reglas y estructuras principales.

### 1. Variables (`val` vs `var`)
*   `val`: **Inmutable** (constante). Su valor no puede cambiar una vez asignado. Es la opción recomendada siempre que sea posible.
*   `var`: **Mutable**. Su valor puede cambiar durante la ejecución.

```kotlin
val firstName = "Chike" // El compilador infiere que es String
var age = 12
age = 13 // Permitido porque es var
```

### 2. Tipos de Datos y Conversión
No hay conversión implícita entre números. Debes usar funciones explícitas como `.toInt()`, `.toLong()`, `.toString()`.

*   **Números:** `Int`, `Long` (sufijo `L`), `Float` (sufijo `f`), `Double`.
*   **Booleanos:** `true` o `false`. Operadores: `||` (O), `&&` (Y), `!` (NO).
*   **Cadenas (Strings):** 
    *   Comillas dobles: `"Texto normal"`
    *   Comillas triples: `"""Texto en varias líneas"""`
    *   **Interpolación:** Usa `$` para insertar variables o `${}` para evaluar expresiones directamente en el texto. Ej: `"Hola $nombre, tienes ${edad + 1} años"`.

### 3. Arrays
Para crear conjuntos de datos.
```kotlin
// Array mixto
val mixto = arrayOf(4, 5, "Chike", false)

// Array de un tipo específico
val numeros = intArrayOf(1, 2, 3, 4, 5)

// Array con tamaño y función lambda
val pares = Array(5) { i -> i * 2 } // [0, 2, 4, 6, 8]
```

### 4. Nulabilidad (Null Safety)
Por defecto, una variable en Kotlin **no puede ser null**. Para permitir nulos, se añade `?` al tipo.
*   **Llamada segura (`?.`):** Solo ejecuta el método si la variable no es nula.
*   **Operador Elvis (`?:`):** Proporciona un valor por defecto si la variable es nula.

```kotlin
var name: String? = null // Permitido por el ?
val longitud = name?.length // Devuelve null en lugar de romper la app
val nombreFinal = name ?: "Sin nombre" // Si es null, usa "Sin nombre"
```

### 5. Bucles (Loops)
Para repetir código.
*   **while:** Comprueba primero, ejecuta después.
*   **do..while:** Ejecuta al menos una vez, luego comprueba.
*   **for:** Itera sobre rangos o arrays.

```kotlin
// Bucle for clásico
for (i in 1..5) { ... }

// Iterar array obteniendo el índice y el valor
val arr = intArrayOf(10, 20, 30)
for ((index, value) in arr.withIndex()) {
    println("Posición $index contiene $value")
}
```

### 6. Condicionales
*   **if / if..else:** Igual que en Java, pero en Kotlin puede usarse como una expresión que devuelve un valor.
```kotlin
val resultado = if (numero % 2 == 0) "Par" else "Impar"
```
*   **when:** Reemplaza al tradicional `switch`. Es mucho más potente y conciso.
```kotlin
when (numero) {
    1 -> println("Es uno")
    2, 3 -> println("Es dos o tres")
    else -> println("No es ninguno")
}
```

### 7. Funciones
Se declaran con la palabra clave `fun`. Pueden tener parámetros de entrada (obligatorio definir el tipo) y de salida (retorno).

```kotlin
// Función sin retorno
fun saludar(nombre: String) {
    println("Hola $nombre")
}

// Función con retorno (devuelve un Int)
fun sumar(a: Int, b: Int): Int {
    return a + b
}

// Función de una sola línea (retorno implícito)
fun multiplicar(a: Int, b: Int) = a * b
```
