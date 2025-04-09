package edu.ucne.proyectofinal_ap2.presentation.clientes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.ucne.proyectofinal_ap2.data.entities.Pedido
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CarritoScreen(
    navController: NavHostController,
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pedidos by pedidoViewModel.pedidos.collectAsState()
    val carrito = pedidos.filter { it.estado == "carrito" }

    var showDialog by remember { mutableStateOf(false) }
    var pedidoAEliminar by remember { mutableStateOf<Pedido?>(null) }
    var eliminando by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Mi Carrito", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                    }
                },
                actions = {
                    if (carrito.isNotEmpty()) {
                        IconButton(onClick = {
                            carrito.forEach { pedido ->
                                pedidoViewModel.cambiarEstadoPedido(pedido.id, "pendiente", "Confirmado desde carrito")
                            }
                            Toast.makeText(context, "Pedidos confirmados", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.Done, contentDescription = "Confirmar todo", tint = Color.White)
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (carrito.isEmpty()) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.h6)
            } else {
                LazyColumn {
                    items(carrito) { pedido ->
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
                                Text("Cantidad: ${pedido.cantidad}")
                                Text("Estado: ${pedido.estado}")

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = {
                                            pedidoViewModel.cambiarEstadoPedido(
                                                id = pedido.id,
                                                nuevoEstado = "pendiente",
                                                mensaje = "Pedido confirmado desde carrito"
                                            )
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
                                    ) {
                                        Text("Confirmar")
                                    }
                                    Button(
                                        onClick = {
                                            navController.navigate(Screen.EditarPedidoCarritoScreen.createRoute(pedido.id))
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3))
                                    ) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = {
                                            pedidoAEliminar = pedido
                                            showDialog = true
                                        },
                                        modifier = Modifier.weight(1f),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF44336))
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (showDialog && pedidoAEliminar != null) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("¿Eliminar pedido?") },
                    text = {
                        if (eliminando) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(strokeWidth = 2.dp)
                                Text("Eliminando pedido...")
                            }
                        } else {
                            Text("¿Estás seguro de que quieres eliminar este pedido del carrito? Esta acción no se puede deshacer.")
                        }
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                eliminando = true
                                Firebase.firestore.collection("pedidos")
                                    .document(pedidoAEliminar!!.id)
                                    .delete()
                                    .addOnSuccessListener {
                                        eliminando = false
                                        showDialog = false
                                        pedidoAEliminar = null
                                        Toast.makeText(context, "Pedido eliminado", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        eliminando = false
                                        showDialog = false
                                        Toast.makeText(context, "Error al eliminar pedido", Toast.LENGTH_SHORT).show()
                                    }
                            },
                            enabled = !eliminando
                        ) {
                            Text("Eliminar", color = Color.Red)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                            pedidoAEliminar = null
                        }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}
