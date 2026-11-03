package dev.danoglez.prctica4

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val btnModify = findViewById<Button>(R.id.btnModify)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

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
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "No se le ha podido autenticar", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        }

        btnSignIn.setOnClickListener {
            val user = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(pass)) {
                Toast.makeText(this, "La contraseña debe tener mínimo 8 caracteres, incluir letras, números y una mayúscula.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USERNAME}=?",
                arrayOf(user)
            )

            if (cursor.moveToFirst()) {
                Toast.makeText(this, "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show()
            } else {
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_USERNAME, user)
                    put(DatabaseHelper.COLUMN_PASSWORD, pass)
                }
                val newRowId = db.insert(DatabaseHelper.TABLE_USERS, null, values)
                if (newRowId != -1L) {
                    Toast.makeText(this, "Usuario creado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                }
            }
            cursor.close()
        }

        btnModify.setOnClickListener {
            val user = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidPassword(pass)) {
                Toast.makeText(this, "La nueva contraseña debe cumplir con los requisitos.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USERNAME}=?",
                arrayOf(user)
            )

            if (cursor.moveToFirst()) {
                val oldPass = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD))
                if (oldPass == pass) {
                    Toast.makeText(this, "La contraseña debe ser diferente a la actual", Toast.LENGTH_SHORT).show()
                } else {
                    val values = ContentValues().apply {
                        put(DatabaseHelper.COLUMN_PASSWORD, pass)
                    }
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

        btnDelete.setOnClickListener {
            val user = etUsername.text.toString().trim()
            val pass = etPassword.text.toString().trim()

            if (user.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.writableDatabase
            val cursor = db.rawQuery(
                "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE ${DatabaseHelper.COLUMN_USERNAME}=? AND ${DatabaseHelper.COLUMN_PASSWORD}=?",
                arrayOf(user, pass)
            )

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
    }

    private fun isValidPassword(password: String): Boolean {
        // Mínimo 8 caracteres, letras, números y al menos una mayúscula
        if (password.length < 8) return false
        var hasUpper = false
        var hasLower = false
        var hasDigit = false
        for (char in password) {
            if (char.isUpperCase()) hasUpper = true
            if (char.isLowerCase()) hasLower = true
            if (char.isDigit()) hasDigit = true
        }
        return hasUpper && (hasLower || hasUpper) && hasDigit
    }
}
