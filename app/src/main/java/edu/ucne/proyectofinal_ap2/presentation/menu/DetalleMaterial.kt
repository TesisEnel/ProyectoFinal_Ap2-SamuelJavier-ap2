package edu.ucne.proyectofinal_ap2.presentation.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter

@Composable
fun DetalleMaterialScreen(
    nombre: String,
    descripcion: String,
    imagenUrl: String,
    precioCm2: Double,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Informacion del Material") },
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                backgroundColor = Color.Black
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = imagenUrl,
                contentDescription = nombre,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                elevation = 4.dp,
                shape = RoundedCornerShape(16.dp),
                backgroundColor = Color(0xFFF5F5F5),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    // Header tipo TopBar dentro de la Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = nombre,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = descripcion,
                            style = MaterialTheme.typography.body1.copy(lineHeight = 22.sp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Precio por cm²: RD$ ${"%.2f".format(precioCm2)}",
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }
        }
    }
}
