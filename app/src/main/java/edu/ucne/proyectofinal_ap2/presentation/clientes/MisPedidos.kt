package edu.ucne.proyectofinal_ap2.presentation.clientes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MisPedidosScreen(
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
    }

    val pedidos by pedidoViewModel.pedidos.collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Mis Pedidos",
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (pedidos.isEmpty()) {
            Text("Aún no has realizado ningún pedido.")
        } else {
            LazyColumn {
                items(pedidos) { pedido ->
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

                            Spacer(modifier = Modifier.height(4.dp))

                            val fechaTexto = pedido.fecha?.let {
                                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                sdf.format(Date(it))
                            } ?: "No disponible"

                            Text("Fecha: $fechaTexto")
                        }
                    }
                }
            }
        }
    }
}
