package dev.danoglez.prctica5

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        // Creamos la tabla usuarios con id, username (único), password y nota_maxima
        db.execSQL("create table usuarios(id integer primary key autoincrement, username text unique, password text, nota_maxima integer default 0)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists usuarios")
        onCreate(db)
    }
}
