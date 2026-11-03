package dev.danoglez.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.danoglez.jetpackcompose.ui.theme.JetpackComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaNombres()
        }
    }
}

    @Composable
fun mensajeprueba() {
    Column(modifier= Modifier.fillMaxSize().background(Color.Red)){
        Text(text = "Mostrando")
        Text(text = "Primer")
        Text(text = "Mensaje")
    }
}


@Composable
fun ListaNombres() {
    val nombres = listOf("Juan", "Ana", "Luis", "María", "Carlos", "Elena")
    LazyColumn {
        items(nombres) { nombre -> // pasa cada elemento (nombre) al contenido del bloque.
            Text(
                text = nombre,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}