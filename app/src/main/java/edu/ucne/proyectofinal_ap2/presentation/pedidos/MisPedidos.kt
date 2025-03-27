package edu.ucne.proyectofinal_ap2.presentation.pedidos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Pedido(
    val id: Int,
    val descripcion: String,
    val estado: String,
    val fecha: String
)

@Composable
fun MisPedidosScreen(
    onBackClick: () -> Unit
) {
    val pedidos = listOf(
        Pedido(1, "Letrero para tienda con logo personalizado", "Aceptado", "24/03/2025"),
        Pedido(2, "Letrero tipo acrílico con luces", "Pendiente", "25/03/2025"),
        Pedido(3, "Letrero de vinil pequeño", "Rechazado", "26/03/2025")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis pedidos") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(pedidos) { pedido ->
                Card(
                    elevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pedido #${pedido.id}", fontSize = 18.sp)
                        Text("Descripción: ${pedido.descripcion}")
                        Text("Estado: ${pedido.estado}")
                        Text("Fecha: ${pedido.fecha}")
                    }
                }
            }
        }
    }
}