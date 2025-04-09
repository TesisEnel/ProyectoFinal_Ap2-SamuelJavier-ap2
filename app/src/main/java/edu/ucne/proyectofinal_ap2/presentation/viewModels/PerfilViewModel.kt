package edu.ucne.proyectofinal_ap2.presentation.viewModels

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.ucne.proyectofinal_ap2.data.entities.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class PerfilViewModel : ViewModel() {

    private val _userData = MutableStateFlow<Usuario?>(null)
    val userData: StateFlow<Usuario?> = _userData

    fun cargarDatosUsuario() {
        val uid = Firebase.auth.currentUser?.uid ?: return
        Firebase.firestore.collection("users").document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("PerfilViewModel", "Error al obtener usuario", error)
                    return@addSnapshotListener
                }
                val usuario = snapshot?.toObject(Usuario::class.java)
                _userData.value = usuario
                println("DATOS DE USUARIO: $usuario")
            }
    }

    fun actualizarPerfil(nuevaContrasena: String, imagenUri: Uri?, context: Context) {
        val user = Firebase.auth.currentUser ?: return
        val uid = user.uid

        if (nuevaContrasena.isNotBlank()) {
            user.updatePassword(nuevaContrasena)
                .addOnSuccessListener {
                    Toast.makeText(context, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        if (imagenUri != null) {
            val storageRef = Firebase.storage.reference.child("fotos_perfil/$uid.jpg")
            storageRef.putFile(imagenUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        Firebase.firestore.collection("users").document(uid)
                            .update("fotoUrl", uri.toString())
                        Toast.makeText(context, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}



