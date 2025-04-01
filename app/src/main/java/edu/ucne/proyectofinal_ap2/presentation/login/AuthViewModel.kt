package edu.ucne.proyectofinal_ap2.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class AuthViewModel @Inject constructor() : ViewModel() {
    private val _isAdmin = mutableStateOf(false)
    val isAdmin: State<Boolean> get() = _isAdmin

    private val _userName = mutableStateOf("")
    val userName: State<String> get() = _userName

    fun loginUser(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid ?: return@addOnSuccessListener
                Firebase.firestore.collection("users").document(userId).get()
                    .addOnSuccessListener { document ->
                        val rol = document.getString("rol") ?: "cliente"
                        val nombre = document.getString("nombre") ?: ""
                        _isAdmin.value = rol == "admin"
                        _userName.value = nombre


                        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                            Firebase.firestore.collection("admin_tokens")
                                .document(userId)
                                .set(mapOf("token" to token))
                        }

                        onSuccess()
                    }
                    .addOnFailureListener {
                        onError("No se pudo obtener el rol.")
                    }
            }
            .addOnFailureListener {
                onError(it.message ?: "Error al iniciar sesión.")
            }
    }

    fun registerUser(
        nombre: String,
        apellido: String,
        direccion: String,
        telefono: String,
        email: String,
        password: String,
        rol: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = task.result?.user?.uid ?: ""
                    val db = Firebase.firestore

                    val userData = hashMapOf(
                        "nombre" to nombre,
                        "apellido" to apellido,
                        "direccion" to direccion,
                        "telefono" to telefono,
                        "email" to email,
                        "rol" to rol
                    )

                    db.collection("users").document(userId).set(userData)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener {
                            onError(it.message ?: "Error desconocido")
                        }
                } else {
                    onError(task.exception?.message ?: "Registro fallido")
                }
            }
    }
}
