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

    private val precioPorCm2Material = mapOf(
        "Vinilo" to 0.45,
        "Acrílico" to 0.60,
        "PVC" to 0.35,
        "Lona" to 0.25,
        "Madera" to 0.50
    )

    private val medidasPorDefecto = mapOf(
        "Pequeño" to (30 to 30),
        "Mediano" to (45 to 45),
        "Grande" to (60 to 60)
    )

    fun calcularPrecioPedido(
        medida: String,
        altoTexto: String = "",
        anchoTexto: String = "",
        material: String = "",
        cantidad: Int = 1
    ): Double {
        val precioCm2 = precioPorCm2Material[material] ?: 0.40

        val (alto, ancho) = when (medida) {
            "Pequeño", "Mediano", "Grande" -> medidasPorDefecto[medida] ?: (0 to 0)
            "Personalizado" -> {
                val altoCm = altoTexto.toDoubleOrNull() ?: 0.0
                val anchoCm = anchoTexto.toDoubleOrNull() ?: 0.0

                if (altoCm > 60 || anchoCm > 60) return 0.0

                (altoCm.toInt() to anchoCm.toInt())
            }
            else -> (0 to 0)
        }

        val area = alto * ancho
        return area * precioCm2 * cantidad
    }

    fun guardarPedido(pedido: Pedido, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val db = Firebase.firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            onError("Usuario no autenticado")
            return
        }
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { userDoc ->
                val nombre = userDoc.getString("nombre") ?: ""
                val apellido = userDoc.getString("apellido") ?: ""
                val telefono = userDoc.getString("telefono") ?: ""
                val direccion = userDoc.getString("direccion") ?: ""

                val pedidoMap = mapOf(
                    "id" to pedido.id,
                    "material" to pedido.material,
                    "medida" to pedido.medida,
                    "alto" to pedido.alto,
                    "ancho" to pedido.ancho,
                    "logoUrl" to pedido.logoUrl,
                    "precio" to pedido.precio,
                    "cantidad" to pedido.cantidad,
                    "estado" to pedido.estado,
                    "fecha" to System.currentTimeMillis(),
                    "userId" to userId,
                    "nombre" to nombre,
                    "apellido" to apellido,
                    "telefono" to telefono,
                    "direccion" to direccion
                )

                db.collection("pedidos").document(pedido.id)
                    .set(pedidoMap)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onError(e.message ?: "Error desconocido") }
            }
            .addOnFailureListener { e ->
                onError("No se pudieron obtener los datos del usuario: ${e.message}")
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
                        direccion = doc.getString("direccion") ?: "",
                        telefono = doc.getString("telefono") ?: "",
                        fecha = doc.getLong("fecha"),
                        cantidad = doc.getLong("cantidad")?.toInt() ?:1,
                        mensaje = doc.getString("mensaje") ?: ""
                    )
                } ?: emptyList()

                _pedidos.value = lista
            }
    }



    fun cambiarEstadoPedido(id: String, nuevoEstado: String, mensaje: String) {
        Firebase.firestore.collection("pedidos").document(id)
            .update(mapOf(
                "estado" to nuevoEstado,
                "mensaje" to mensaje
            ))
            .addOnSuccessListener {
                Log.d("PedidoViewModel", "Estado actualizado correctamente.")
                obtenerPedidos()
            }
            .addOnFailureListener {
                Log.e("PedidoViewModel", "Error al actualizar el estado del pedido", it)
            }
    }

}