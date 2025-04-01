package edu.ucne.proyectofinal_ap2.presentation.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ListaPedidosAdminScreen(
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
    }

    val pedidos by pedidoViewModel.pedidos.collectAsState(initial = emptyList())
    var estadoSeleccionado by remember { mutableStateOf("pendiente") }

    val pedidosFiltrados = pedidos.filter { it.estado == estadoSeleccionado }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            listOf("pendiente", "aceptado", "rechazado").forEach { estado ->
                Button(
                    onClick = { estadoSeleccionado = estado },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (estadoSeleccionado == estado)
                            MaterialTheme.colors.primary
                        else MaterialTheme.colors.surface
                    )
                ) {
                    Text(estado.replaceFirstChar { it.uppercaseChar() })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (pedidosFiltrados.isEmpty()) {
            Text("No hay pedidos $estadoSeleccionado.")
        } else {
            pedidosFiltrados.forEach { pedido ->
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
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Cliente:")
                        Text("- Nombre: ${pedido.nombre} ${pedido.apellido}")
                        Text("- Teléfono: ${pedido.telefono}")
                        Text("- Dirección: ${pedido.direccion}")


                        pedido.fecha?.let {
                            val fecha = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it))
                            Text("Fecha del pedido: $fecha")
                        } ?: Text("Fecha: No disponible")

                        if (estadoSeleccionado == "pendiente") {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Button(
                                    onClick = {
                                        pedidoViewModel.cambiarEstadoPedido(pedido.id, "aceptado")
                                    }
                                ) {
                                    Text("Aceptar")
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Button(
                                    onClick = {
                                        pedidoViewModel.cambiarEstadoPedido(pedido.id, "rechazado")
                                    },
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.error)
                                ) {
                                    Text("Rechazar")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}