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
                    Button(
                        onClick = { estadoSeleccionado = estados[index] },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (estadoSeleccionado == estados[index])
                                MaterialTheme.colors.primary
                            else MaterialTheme.colors.surface
                        )
                    ) {
                        Text(estados[index])
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            pedidos.filter { it.estado == estadoSeleccionado }.forEach { pedido ->
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
                            TextField(
                                value = mensaje,
                                onValueChange = { mensaje = it },
                                label = { Text("Mensaje al cliente") }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            if (estadoSeleccionado == "pendiente") {
                                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                    Button(
                                        onClick = {
                                            pedidoViewModel.cambiarEstadoPedido(pedido.id, "aceptado", mensaje)
                                        }
                                    ) {
                                        Text("Aceptar")
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
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
