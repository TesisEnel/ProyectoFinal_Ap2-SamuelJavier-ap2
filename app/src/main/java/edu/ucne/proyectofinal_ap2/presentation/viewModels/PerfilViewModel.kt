package edu.ucne.proyectofinal_ap2.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.ucne.proyectofinal_ap2.data.entities.Pedido

class PerfilViewModel : ViewModel() {
    var fotoPerfil = mutableStateOf<Uri?>(null)

    fun actualizarPerfil(nuevaContrasena: String, nuevaImagenUri: Uri?, context: Context) {
        val user = Firebase.auth.currentUser

        if (user == null) {
            Toast.makeText(context, "No se ha iniciado sesión", Toast.LENGTH_SHORT).show()
            return
        }

        if (nuevaContrasena.isNotBlank()) {
            user.updatePassword(nuevaContrasena)
                .addOnSuccessListener {
                    Toast.makeText(context, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show()
                }
        }


        if (nuevaImagenUri != null) {
            val ref = Firebase.storage.reference.child("perfiles/${user.uid}.jpg")
            ref.putFile(nuevaImagenUri)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { url ->
                        Firebase.firestore.collection("usuarios").document(user.uid)
                            .update("fotoPerfil", url.toString())
                        fotoPerfil.value = url
                        Toast.makeText(context, "Foto actualizada", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
