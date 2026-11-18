package dev.danoglez.megarepasoexamen

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "mi_base_datos.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios (nombre TEXT PRIMARY KEY)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    fun registrarUsuario(nombre: String): Boolean {
        val db = this.writableDatabase // writableDatabase = para escribir/insertar
        val valores = ContentValues().apply {
            put("nombre", nombre) // "nombre" es la columna, nombre es el valor
        }
        val resultado = db.insert("usuarios", null, valores)
        return resultado != -1L // Si devuelve -1 es que hubo error (ej: ya existía)
    }

    fun existeUsuario(nombre: String): Boolean {
        val db = this.writableDatabase
        val resultado = db.rawQuery("SELECT * FROM usuarios WHERE nombre = ?", arrayOf(nombre))
        val existe = resultado.moveToFirst();
        resultado.close()

        return existe
    }
}
