package edu.ucne.proyectofinal_ap2.presentation.admin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import edu.ucne.proyectofinal_ap2.data.entities.Pedido
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel


@Composable
fun AdminPollingListener(
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    var lastPedidoId by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            delay(10000)

            pedidoViewModel.obtenerPedidos {
                val nuevo = it.firstOrNull { pedido -> pedido.id != lastPedidoId }

                if (nuevo != null) {
                    lastPedidoId = nuevo.id
                    println("¡Nuevo pedido recibido!")
                }
            }
        }
    }
}
