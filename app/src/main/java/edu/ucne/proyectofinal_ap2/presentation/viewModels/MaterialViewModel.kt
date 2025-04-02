package edu.ucne.proyectofinal_ap2.presentation.menu

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyectofinal_ap2.data.entities.Material
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MaterialViewModel @Inject constructor() : ViewModel() {

    var materiales by mutableStateOf<List<Material>>(emptyList())
        private set

    init {
        obtenerMateriales()
    }

    fun obtenerMateriales() {
        Firebase.firestore.collection("materiales")
            .get()
            .addOnSuccessListener { result ->
                materiales = result.map { doc ->
                    Material(
                        nombre = doc.getString("nombre") ?: "",
                        descripcion = doc.getString("descripcion") ?: "",
                        imagenUrl = doc.getString("imagenUrl") ?: ""
                    )
                }
            }
            .addOnFailureListener {
                Log.e("Materiales", "Error al obtener materiales", it)
            }
    }

    fun agregarMaterial(
        nombre: String,
        descripcion: String,
        imagenUri: Uri,
        precioCm2: Double,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val storageRef = Firebase.storage.reference.child("materiales/${UUID.randomUUID()}.jpg")

        storageRef.putFile(imagenUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val material = hashMapOf(
                        "nombre" to nombre,
                        "descripcion" to descripcion,
                        "imagenUrl" to uri.toString(),
                        "precioCm2" to precioCm2 // 🔹 se guarda en Firebase
                    )

                    Firebase.firestore.collection("materiales")
                        .add(material)
                        .addOnSuccessListener {
                            obtenerMateriales()
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onError("Error al guardar el material")
                        }
                }
            }
            .addOnFailureListener {
                onError("Error al subir imagen")
            }
    }

}
