package edu.ucne.proyectofinal_ap2.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyectofinal_ap2.data.entities.Pedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PedidoViewModel @Inject constructor() : ViewModel() {
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos
    var listaPedidos by mutableStateOf<List<Pedido>>(emptyList())
        private set
    var isSaving by mutableStateOf(false)
        private set

    fun calcularPrecioPedido(medida: String, alto: String = "", ancho: String = ""): Double {
        return when (medida) {
            "Pequeño" -> 500.0
            "Mediano" -> 1000.0
            "Grande" -> 1500.0
            "Personalizado" -> {
                val h = alto.toDoubleOrNull() ?: 0.0
                val w = ancho.toDoubleOrNull() ?: 0.0
                (h * w * 0.5)
            }

            else -> 0.0
        }
    }

    fun guardarPedido(pedido: Pedido, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val pedidosCollection = db.collection("pedidos")

        val pedidoMap = mapOf(
            "id" to pedido.id,
            "material" to pedido.material,
            "medida" to pedido.medida,
            "alto" to pedido.alto,
            "ancho" to pedido.ancho,
            "logoUrl" to pedido.logoUrl,
            "precio" to pedido.precio,
            "estado" to pedido.estado,
            "fecha" to System.currentTimeMillis(),
            "nombre" to pedido.nombre,
            "apellido" to pedido.apellido,
            "telefono" to pedido.telefono,
            "direccion" to pedido.direccion
        )

        pedidosCollection.document(pedido.id)
            .set(pedidoMap)
            .addOnSuccessListener {
                onSuccess()

                // 🔔 Aquí va la notificación al admin
                enviarNotificacionAlAdmin(
                    titulo = "Nuevo pedido",
                    mensaje = "Un cliente ha realizado un nuevo pedido"
                )
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error desconocido")
            }
    }

    fun obtenerPedidos() {
        Firebase.firestore.collection("pedidos")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.e("PedidoViewModel", "Error al obtener pedidos", e)
                    return@addSnapshotListener
                }

                val lista = snapshot?.documents?.map { doc ->
                    Pedido(
                        id = doc.id,
                        material = doc.getString("material") ?: "",
                        medida = doc.getString("medida") ?: "",
                        alto = doc.getString("alto") ?: "",
                        ancho = doc.getString("ancho") ?: "",
                        logoUrl = doc.getString("logoUrl") ?: "",
                        precio = doc.getDouble("precio") ?: 0.0,
                        estado = doc.getString("estado") ?: "pendiente",
                        nombre = doc.getString("nombre") ?: "",
                        apellido = doc.getString("apellido") ?: "",
                        telefono = doc.getString("telefono") ?: "",
                        direccion = doc.getString("direccion") ?: "",
                        fecha = doc.getLong("fecha")
                    )
                } ?: emptyList()

                _pedidos.value = lista
            }
    }
    fun enviarNotificacionAlAdmin(titulo: String, mensaje: String) {
        Firebase.firestore.collection("admin_tokens")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val token = document.getString("token")
                    if (token != null) {
                        val data = mapOf(
                            "to" to token,
                            "notification" to mapOf(
                                "title" to titulo,
                                "body" to mensaje
                            )
                        )


                        val client = OkHttpClient()
                        val json = Gson().toJson(data)
                        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
                        val request = Request.Builder()
                            .url("https://fcm.googleapis.com/fcm/send")// 🔁 Cambia por tu FCM Server Key
                            .addHeader("Authorization", "key=AAAAbC123456:APA91b...")
                            .addHeader("Content-Type", "application/json")
                            .post(body)
                            .build()

                        client.newCall(request).enqueue(object : Callback {
                            override fun onFailure(call: Call, e: IOException) {
                                Log.e("Noti", "Error al enviar notificación", e)
                            }

                            override fun onResponse(call: Call, response: Response) {
                                Log.d("Noti", "Notificación enviada: ${response.body?.string()}")
                            }
                        })
                    }
                }
            }
    }

    fun cambiarEstadoPedido(id: String, nuevoEstado: String) {
        Firebase.firestore.collection("pedidos").document(id)
            .update("estado", nuevoEstado)
            .addOnSuccessListener {
                obtenerPedidos()
            }
            .addOnFailureListener {
                Log.e("PedidoViewModel", "Error al cambiar estado", it)
            }
    }

}