package edu.ucne.proyectofinal_ap2.presentation.menu

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.google.firebase.firestore.DocumentSnapshot

@Composable
fun EditarLetreroScreen(
    navController: NavController
) {
    val context = LocalContext.current
    var letreros by remember { mutableStateOf<List<DocumentSnapshot>>(emptyList()) }
    var selectedLetrero by remember { mutableStateOf<DocumentSnapshot?>(null) }
    var nuevoNombre by remember { mutableStateOf("") }
    var nuevaDescripcion by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("letreros").get()
            .addOnSuccessListener { result ->
                letreros = result.documents
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Editar Letrero") },
            contentColor = Color.White,
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            },
            backgroundColor = Color.Black
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            letreros.forEach { doc ->
                val id = doc.id
                val nombre = doc.getString("nombre") ?: "Sin nombre"

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            selectedLetrero = doc
                            nuevoNombre = nombre
                            nuevaDescripcion = doc.getString("descripcion") ?: ""
                            mostrarDialogo = true
                        },
                    elevation = 4.dp
                ) {
                    Text(nombre, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }

    if (mostrarDialogo && selectedLetrero != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Editar Letrero") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nuevoNombre,
                        onValueChange = { nuevoNombre = it },
                        label = { Text("Nuevo nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = nuevaDescripcion,
                        onValueChange = { nuevaDescripcion = it },
                        label = { Text("Nueva descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val id = selectedLetrero!!.id
                    Firebase.firestore.collection("letreros").document(id)
                        .update(
                            mapOf(
                                "nombre" to nuevoNombre,
                                "descripcion" to nuevaDescripcion
                            )
                        )
                        .addOnSuccessListener {
                            Toast.makeText(context, "Letrero actualizado", Toast.LENGTH_SHORT).show()
                            mostrarDialogo = false
                            selectedLetrero = null
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                },
                    colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    mostrarDialogo = false
                    selectedLetrero = null
                },
                    colors = ButtonDefaults.buttonColors(Color.Red)) {
                    Text("Cancelar")
                }
            }
        )
    }
}
