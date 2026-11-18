# 📄 MODELOS DE EXAMEN PRÁCTICO (VARIACIONES)
**2º DAM - Programación Multimedia y de Dispositivos Móviles (PGL)**  
*C.I.F.P. César Manrique - Gobierno de Canarias*

---

# ✈️ MODELO A: "Gestión de Vuelos de Aerolínea"

### 📋 Enunciado General:
El alumno construirá una aplicación que gestionará información sobre vuelos de una aerolínea. Los datos que se almacenarán de cada vuelo son los siguientes:
**Identificador, Destino, Compañía, Plazas Totales, Plazas Disponibles y Completo** (que se gestionará con un control RadioButton que tomará los valores `"SI"` o `"NO"`).

La aplicación mostrará inicialmente un menú con las siguientes opciones que nos llevarán a sus respectivos activities:
*Añadir vuelo - Consultar vuelo - Reservar billete - Ampliar plazas vuelo*

---

### 📝 Ejercicios y Criterios de Corrección:

#### EJERCICIO 1 (2,5 puntos) - AÑADIR VUELO:
Hacer una función en la que se mostrará un formulario que solicitará los datos de inserción. Si los datos no son correctos, o si el Identificador introducido ya existe mostrará el mensaje de error correspondiente. Se debe comprobar que el valor de plazas disponibles no contradice lo marcado en el RadioButton.
* **Puntuación:**
  - La aplicación da de alta correctamente el vuelo en la base de datos: **1 punto**
  - La aplicación muestra un mensaje de error si el Identificador a insertar ya existe: **1 punto**
  - La aplicación muestra un mensaje de error si no hay concordancia entre las plazas disponibles y lo marcado en el atributo "Completo": **0.5 puntos**

#### EJERCICIO 2 (2,5 puntos) - CONSULTAR VUELO:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador que desea consultar el usuario. Si el Identificador no es encontrado en la base de datos se mostrará el mensaje de error correspondiente. Si el Identificador existe en la base de datos, el usuario mostrará la información en etiquetas debajo del botón de CONSULTA.
* **Puntuación:**
  - La aplicación muestra los datos del vuelo consultado correctamente: **1.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador a consultar no se encuentra en la base de datos: **1 punto**

#### EJERCICIO 3 (2,5 puntos) - RESERVAR BILLETE:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador del vuelo del que desea reservar el usuario. Si el Identificador no es encontrado en la base de datos se mostrará el mensaje de error correspondiente. Si el Identificador existe en la base de datos, se restará 1 a PLAZAS DISPONIBLES y se mostrará un mensaje de confirmación al usuario. Si las Plazas Disponibles son 0 no permitirá reservar el billete y se informará de que el vuelo está completo. Si las plazas disponibles quedan igual a 0, se deberá actualizar el valor del atributo COMPLETO a "SI".
* **Puntuación:**
  - La aplicación actualiza correctamente las Plazas Disponibles del vuelo cuyo Identificador hayamos introducido: **1 punto**
  - La aplicación actualiza correctamente el atributo COMPLETO cuando las plazas disponibles quedan a 0: **0.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador del vuelo a reservar no existe o si el número de plazas disponibles actuales es igual a cero: **1 punto**

#### EJERCICIO 4 (2,5 puntos) - AMPLIAR PLAZAS VUELO:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador del vuelo del que desea ampliar su capacidad y el nuevo número de plazas totales. Si el Identificador no es encontrado en la base de datos o si el nuevo aforo total de plazas no es superior al anterior se mostrará el mensaje de error correspondiente. Si no hay ningún error, se ampliarán las plazas del vuelo por el valor dado por el usuario (sumando la diferencia a plazas disponibles). Finalmente se mostrará un mensaje de confirmación al usuario. Se deberá actualizar el atributo COMPLETO tras la ampliación en caso de que proceda.
* **Puntuación:**
  - La aplicación modifica correctamente las plazas totales del vuelo cuyo Identificador hayamos introducido: **1 punto**
  - La aplicación actualiza correctamente cuando proceda el atributo COMPLETO tras realizar la ampliación de plazas: **0.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador del vuelo a modificar no existe o si el nuevo total de plazas no es superior al antiguo: **1 punto**

---

### 💻 Código Auxiliar:
```kotlin
override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("create table vuelos(" +
        "id integer primary key, " +
        "destino text, " +
        "compania text, " +
        "plazasTotales integer, " +
        "plazasDisponibles integer, " +
        "completo text)")
}
```

### 📱 Identificadores de Vistas (IDs sugeridos):
- **Menú Principal (`activity_main.xml`):** `btnAnadir`, `btnConsultar`, `btnReservar`, `btnAmpliar`.
- **Añadir:** `etId`, `etDestino`, `etCompania`, `etTotal`, `rgCompleto` (`rbSi`, `rbNo`), `btnGuardar`.
- **Consultar:** `etId`, `btnConsultar`, `lblDatos`.
- **Reservar:** `etId`, `btnReservar`, `lblMensaje`.
- **Ampliar:** `etId`, `etNuevoTotal`, `btnAmpliar`, `lblMensaje2`.

---
---

# 🎮 MODELO B: "Control de Stock en Tienda Gaming"

### 📋 Enunciado General:
El alumno construirá una aplicación que gestionará información sobre productos y periféricos gaming de una tienda. Los datos que se almacenarán de cada producto son los siguientes:
**Identificador, Nombre, Marca, Stock Total, Stock Disponible y Agotado** (que se gestionará con un control RadioButton que tomará los valores `"SI"` o `"NO"`).

La aplicación mostrará inicialmente un menú con las siguientes opciones que nos llevarán a sus respectivos activities:
*Añadir producto - Consultar producto - Vender unidad - Reponer stock producto*

---

### 📝 Ejercicios y Criterios de Corrección:

#### EJERCICIO 1 (2,5 puntos) - AÑADIR PRODUCTO:
Hacer una función en la que se mostrará un formulario que solicitará los datos de inserción. Si los datos no son correctos, o si el Identificador introducido ya existe mostrará el mensaje de error correspondiente. Se debe comprobar que el valor de stock disponible no contradice lo marcado en el RadioButton.
* **Puntuación:**
  - La aplicación da de alta correctamente el producto en la base de datos: **1 punto**
  - La aplicación muestra un mensaje de error si el Identificador a insertar ya existe: **1 punto**
  - La aplicación muestra un mensaje de error si no hay concordancia entre el stock disponible y lo marcado en el atributo "Agotado": **0.5 puntos**

#### EJERCICIO 2 (2,5 puntos) - CONSULTAR PRODUCTO:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador que desea consultar el usuario. Si el Identificador no es encontrado en la base de datos se mostrará el mensaje de error correspondiente. Si el Identificador existe en la base de datos, el usuario mostrará la información en etiquetas debajo del botón de CONSULTA.
* **Puntuación:**
  - La aplicación muestra los datos del producto consultado correctamente: **1.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador a consultar no se encuentra en la base de datos: **1 punto**

#### EJERCICIO 3 (2,5 puntos) - VENDER UNIDAD:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador del producto del que desea comprar o vender una unidad. Si el Identificador no es encontrado en la base de datos se mostrará el mensaje de error correspondiente. Si el Identificador existe en la base de datos, se restará 1 a STOCK DISPONIBLE y se mostrará un mensaje de confirmación al usuario. Si el Stock Disponible es 0 no permitirá realizar la venta y se informará de que el producto está agotado. Si el stock disponible queda igual a 0, se deberá actualizar el valor del atributo AGOTADO a "SI".
* **Puntuación:**
  - La aplicación actualiza correctamente el Stock Disponible del producto cuyo Identificador hayamos introducido: **1 punto**
  - La aplicación actualiza correctamente el atributo AGOTADO cuando el stock disponible queda a 0: **0.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador del producto a vender no existe o si el número de stock disponible actual es igual a cero: **1 punto**

#### EJERCICIO 4 (2,5 puntos) - REPONER STOCK PRODUCTO:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador del producto del que desea reponer stock y la nueva cantidad total de stock. Si el Identificador no es encontrado en la base de datos o si el nuevo stock total no es superior al anterior se mostrará el mensaje de error correspondiente. Si no hay ningún error, se actualizará el stock total del producto por el valor dado por el usuario y se incrementará el stock disponible con la diferencia añadida. Finalmente se mostrará un mensaje de confirmación al usuario. Se deberá actualizar el atributo AGOTADO tras la reposición en caso de que proceda.
* **Puntuación:**
  - La aplicación modifica correctamente el stock total del producto cuyo Identificador hayamos introducido: **1 punto**
  - La aplicación actualiza correctamente cuando proceda el atributo AGOTADO tras realizar la reposición de stock: **0.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador del producto a modificar no existe o si el nuevo stock total no es superior al antiguo: **1 punto**

---

### 💻 Código Auxiliar:
```kotlin
override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("create table productos(" +
        "id integer primary key, " +
        "nombre text, " +
        "marca text, " +
        "stockTotal integer, " +
        "stockDisponible integer, " +
        "agotado text)")
}
```

### 📱 Identificadores de Vistas (IDs sugeridos):
- **Menú Principal (`activity_main.xml`):** `btnAnadir`, `btnConsultar`, `btnVender`, `btnReponer`.
- **Añadir:** `etId`, `etNombre`, `etMarca`, `etTotal`, `rgAgotado` (`rbSi`, `rbNo`), `btnGuardar`.
- **Consultar:** `etId`, `btnConsultar`, `lblDatos`.
- **Vender:** `etId`, `btnVender`, `lblMensaje`.
- **Reponer:** `etId`, `etNuevoTotal`, `btnReponer`, `lblMensaje2`.

---
---

# 🏨 MODELO C: "Reserva de Habitaciones en Cadena Hotelera"

### 📋 Enunciado General:
El alumno construirá una aplicación que gestionará información sobre hoteles y disponibilidad de alojamiento. Los datos que se almacenarán de cada hotel son los siguientes:
**Identificador, Nombre, Ciudad, Habitaciones Totales, Habitaciones Disponibles y Lleno** (que se gestionará con un control RadioButton que tomará los valores `"SI"` o `"NO"`).

La aplicación mostrará inicialmente un menú con las siguientes opciones que nos llevarán a sus respectivos activities:
*Añadir hotel - Consultar hotel - Reservar habitación - Ampliar capacidad hotel*

---

### 📝 Ejercicios y Criterios de Corrección:

#### EJERCICIO 1 (2,5 puntos) - AÑADIR HOTEL:
Hacer una función en la que se mostrará un formulario que solicitará los datos de inserción. Si los datos no son correctos, o si el Identificador introducido ya existe mostrará el mensaje de error correspondiente. Se debe comprobar que el valor de habitaciones disponibles no contradice lo marcado en el RadioButton.
* **Puntuación:**
  - La aplicación da de alta correctamente el hotel en la base de datos: **1 punto**
  - La aplicación muestra un mensaje de error si el Identificador a insertar ya existe: **1 punto**
  - La aplicación muestra un mensaje de error si no hay concordancia entre las habitaciones disponibles y lo marcado en el atributo "Lleno": **0.5 puntos**

#### EJERCICIO 2 (2,5 puntos) - CONSULTAR HOTEL:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador que desea consultar el usuario. Si el Identificador no es encontrado en la base de datos se mostrará el mensaje de error correspondiente. Si el Identificador existe en la base de datos, el usuario mostrará la información en etiquetas debajo del botón de CONSULTA.
* **Puntuación:**
  - La aplicación muestra los datos del hotel consultado correctamente: **1.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador a consultar no se encuentra en la base de datos: **1 punto**

#### EJERCICIO 3 (2,5 puntos) - RESERVAR HABITACIÓN:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador del hotel del que desea reservar el usuario. Si el Identificador no es encontrado en la base de datos se mostrará el mensaje de error correspondiente. Si el Identificador existe en la base de datos, se restará 1 a HABITACIONES DISPONIBLES y se mostrará un mensaje de confirmación al usuario. Si las Habitaciones Disponibles son 0 no permitirá reservar y se informará de que el hotel está al completo. Si las habitaciones disponibles quedan igual a 0, se deberá actualizar el valor del atributo LLENO a "SI".
* **Puntuación:**
  - La aplicación actualiza correctamente las Habitaciones Disponibles del hotel cuyo Identificador hayamos introducido: **1 punto**
  - La aplicación actualiza correctamente el atributo LLENO cuando las habitaciones disponibles quedan a 0: **0.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador del hotel a reservar no existe o si el número de habitaciones disponibles actuales es igual a cero: **1 punto**

#### EJERCICIO 4 (2,5 puntos) - AMPLIAR CAPACIDAD HOTEL:
Hacer una función en la que se mostrará un formulario que solicitará el Identificador del hotel del que desea ampliar su capacidad y el nuevo número total de habitaciones. Si el Identificador no es encontrado en la base de datos o si el nuevo número de habitaciones totales no es superior al anterior se mostrará el mensaje de error correspondiente. Si no hay ningún error, se ampliará el total de habitaciones por el valor dado por el usuario (sumando las habitaciones nuevas a las disponibles). Finalmente se mostrará un mensaje de confirmación al usuario. Se deberá actualizar el atributo LLENO tras la ampliación en caso de que proceda.
* **Puntuación:**
  - La aplicación modifica correctamente las habitaciones totales del hotel cuyo Identificador hayamos introducido: **1 punto**
  - La aplicación actualiza correctamente cuando proceda el atributo LLENO tras realizar la ampliación de capacidad: **0.5 puntos**
  - La aplicación muestra un mensaje de error si el Identificador del hotel a modificar no existe o si el nuevo total de habitaciones no es superior al antiguo: **1 punto**

---

### 💻 Código Auxiliar:
```kotlin
override fun onCreate(db: SQLiteDatabase) {
    db.execSQL("create table hoteles(" +
        "id integer primary key, " +
        "nombre text, " +
        "ciudad text, " +
        "habTotales integer, " +
        "habDisponibles integer, " +
        "lleno text)")
}
```

### 📱 Identificadores de Vistas (IDs sugeridos):
- **Menú Principal (`activity_main.xml`):** `btnAnadir`, `btnConsultar`, `btnReservar`, `btnAmpliar`.
- **Añadir:** `etId`, `etNombre`, `etCiudad`, `etTotal`, `rgLleno` (`rbSi`, `rbNo`), `btnGuardar`.
- **Consultar:** `etId`, `btnConsultar`, `lblDatos`.
- **Reservar:** `etId`, `btnReservar`, `lblMensaje`.
- **Ampliar:** `etId`, `etNuevoTotal`, `btnAmpliar`, `lblMensaje2`.
