package edu.ucne.proyectofinal_ap2.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.ucne.proyectofinal_ap2.data.entities.Letrero

@Composable
fun DetalleLetreroScreen(
    letrero: Letrero,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(letrero.nombre) },
                navigationIcon = { /* Puedes agregar botón de retroceso si gustas */ }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = letrero.imagen),
                contentDescription = letrero.nombre,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Nombre del letrero: ${letrero.nombre}",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            // Puedes agregar más información del letrero aquí si se necesita
        }
    }
}