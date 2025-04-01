package edu.ucne.proyectofinal_ap2.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyectofinal_ap2.data.entities.Letrero
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LetreroViewModel @Inject constructor() : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var letreros by mutableStateOf<List<Letrero>>(emptyList())
        private set

    fun agregarLetrero(
        nombre: String,
        descripcion: String,
        imagenUri: Uri,
        context: Context,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        isLoading = true

        val imageName = "letreros/${UUID.randomUUID()}.jpg"
        val storageRef = Firebase.storage.reference.child(imageName)

        Log.d("AgregarLetrero", "Subiendo imagen: $imagenUri → $imageName")

        storageRef.putFile(imagenUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    val letrero = hashMapOf(
                        "nombre" to nombre,
                        "descripcion" to descripcion,
                        "imagenUrl" to uri.toString()
                    )

                    Firebase.firestore.collection("letreros")
                        .add(letrero)
                        .addOnSuccessListener {
                            isLoading = false
                            onSuccess()
                            obtenerLetreros()
                        }
                        .addOnFailureListener {
                            isLoading = false
                            onError("Error al guardar en Firestore")
                        }
                }
            }
            .addOnFailureListener {
                isLoading = false
                Log.e("AgregarLetrero", "Error al subir imagen: ${it.message}")
                onError("Error al subir imagen: ${it.message}")
            }
    }

    fun obtenerLetreros() {
        Firebase.firestore.collection("letreros")
            .get()
            .addOnSuccessListener { result ->
                letreros = result.documents.mapNotNull { doc ->
                    val nombre = doc.getString("nombre") ?: return@mapNotNull null
                    val descripcion = doc.getString("descripcion") ?: ""
                    val imagenUrl = doc.getString("imagenUrl") ?: ""
                    Letrero(nombre = nombre, descripcion = descripcion, imagenUrl = imagenUrl)
                }
            }
            .addOnFailureListener {
                Log.e("LetreroViewModel", "Error al obtener letreros: ${it.message}")
            }
    }
}