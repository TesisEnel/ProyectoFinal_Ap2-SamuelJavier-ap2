package edu.ucne.proyectofinal_ap2.presentation.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

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

    val pedidosFiltrados = pedidos.filter { it.estado == estadoSeleccionado }

    val estados = listOf("pendiente", "aceptado", "rechazado", "listo")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(estados.size) { index ->
                    val estado = estados[index]
                    Button(
                        onClick = { estadoSeleccionado = estado },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (estadoSeleccionado == estado)
                                MaterialTheme.colors.primary
                            else MaterialTheme.colors.surface
                        )
                    ) {
                        Text(
                            text = estado.replaceFirstChar { it.uppercaseChar() }
                        )
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
                                val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it))
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

                            if (estadoSeleccionado == "aceptado") {
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = {
                                        pedidoViewModel.cambiarEstadoPedido(pedido.id, "listo")
                                    },
                                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary)
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
