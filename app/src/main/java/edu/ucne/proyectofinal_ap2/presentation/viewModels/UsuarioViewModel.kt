package edu.ucne.proyectofinal_ap2.presentation.viewModels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.proyectofinal_ap2.data.entities.Usuario
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor() : ViewModel() {

    private val _usuarios = mutableStateListOf<Usuario>()
    val usuarios: List<Usuario> get() = _usuarios

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        Firebase.firestore.collection("users")
            .get()
            .addOnSuccessListener { result ->
                _usuarios.clear()
                result.forEach { doc ->
                    val usuario = Usuario(
                        id = doc.id,
                        nombre = doc.getString("nombre") ?: "",
                        apellido = doc.getString("apellido") ?: "",
                        email = doc.getString("email") ?: "",
                        direccion = doc.getString("direccion") ?: "",
                        telefono = doc.getString("telefono") ?: "",
                        rol = doc.getString("rol") ?: "cliente",
                        fotoUrl = doc.getString("fotoUrl") ?: ""
                    )
                    _usuarios.add(usuario)
                }
            }
    }
}
