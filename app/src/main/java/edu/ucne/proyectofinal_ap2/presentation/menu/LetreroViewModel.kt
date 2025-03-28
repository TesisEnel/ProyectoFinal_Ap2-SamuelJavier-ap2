package edu.ucne.proyectofinal_ap2.presentation.menu
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LetreroViewModel @Inject constructor() : ViewModel() {

    var isLoading by mutableStateOf(false)
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

        // 🔍 LOG
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
}