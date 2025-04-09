package edu.ucne.proyectofinal_ap2.presentation.admin

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
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ListaPedidosAdminScreen(
    navHostController: NavHostController,
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
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
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Pedidos",
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
                                    Text("Cantidad: ${pedido.cantidad}")
                                    Text("Precio: RD$${pedido.precio}")
                                    Text("Estado: ${pedido.estado}")
                                    Text("Cliente: ${pedido.nombre} ${pedido.apellido}")
                                    Text("- Teléfono: ${pedido.telefono}")
                                    Text("- Dirección: ${pedido.direccion}")
                                    Text("- Mensaje: ${pedido.mensaje}")

                                    pedido.fecha?.let {
                                        val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it))
                                        Text("Fecha del pedido: $fecha")
                                    } ?: Text("Fecha: No disponible")

                                    if (estadoSeleccionado == "pendiente" || estadoSeleccionado == "aceptado") {
                                        var mensaje by remember { mutableStateOf(pedido.mensaje ?: "") }

                                        OutlinedTextField(
                                            value = mensaje,
                                            onValueChange = { mensaje = it },
                                            label = { Text("Mensaje al cliente") },
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        if (estadoSeleccionado == "pendiente") {
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Button(
                                                    onClick = {
                                                        pedidoViewModel.cambiarEstadoPedido(pedido.id, "aceptado", mensaje)
                                                    },
                                                    colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                                                ) {
                                                    Text("Aceptar")
                                                }

                                                Button(
                                                    onClick = {
                                                        pedidoViewModel.cambiarEstadoPedido(pedido.id, "rechazado", mensaje)
                                                    },
                                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.error)
                                                ) {
                                                    Text("Rechazar")
                                                }
                                            }
                                        } else if (estadoSeleccionado == "aceptado") {
                                            Button(
                                                onClick = {
                                                    pedidoViewModel.cambiarEstadoPedido(pedido.id, "listo", mensaje)
                                                },
                                                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)),
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Text("Marcar como Listo")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
