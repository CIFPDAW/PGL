package dev.danoglez.prctica5

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

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

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
                
                if(resultado != -1L){
                     Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                } else {
                     Toast.makeText(this, "Error: El usuario ya existe", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, rellena usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }

        btnLogin.setOnClickListener {
            val admin = DatabaseHelper(this, "CuestionarioBD", null, 1)
            val bd = admin.readableDatabase
            
            val usuario = etUsername.text.toString()
            val contra = etPassword.text.toString()

            if (usuario.isNotEmpty() && contra.isNotEmpty()) {
                val fila = bd.rawQuery("select id, nota_maxima from usuarios where username='${usuario}' and password='${contra}'", null)
                
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
                Toast.makeText(this, "Por favor, rellena usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
