package edu.ucne.proyectofinal_ap2.presentation.clientes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MisPedidosScreen(
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
        pedidoViewModel.resetNotificacionesCliente()
    }

    val pedidos by pedidoViewModel.pedidos.collectAsState(initial = emptyList())
    var estadoSeleccionado by remember { mutableStateOf("pendiente") }

    val estados = listOf("pendiente", "aceptado", "rechazado", "listo")

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Mis Pedidos",
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(estados) { estado ->
                            Button(
                                onClick = { estadoSeleccionado = estado },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = if (estadoSeleccionado == estado)
                                        Color.Gray else MaterialTheme.colors.surface
                                )
                            ) {
                                Text(estado.replaceFirstChar { it.uppercaseChar() })
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn {
                        items(pedidos.filter { it.estado == estadoSeleccionado }) { pedido ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                elevation = 4.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Material: ${pedido.material}")
                                    Text("Medida: ${pedido.medida} (${pedido.alto}cm x ${pedido.ancho}cm)")
                                    Text("Precio: RD$${pedido.precio}")
                                    Text("Estado: ${pedido.estado}")
                                    Text("Mensaje: ${pedido.mensaje}")
                                    pedido.fecha?.let {
                                        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it))
                                        Text("Fecha del pedido: $fecha")
                                    } ?: Text("Fecha: No disponible")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
