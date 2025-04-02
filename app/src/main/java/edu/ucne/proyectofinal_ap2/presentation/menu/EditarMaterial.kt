package edu.ucne.proyectofinal_ap2.presentation.menu
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController

@Composable
fun EditarMaterialScreen(navController: NavHostController) {
    val context = LocalContext.current
    var materiales by remember { mutableStateOf<List<DocumentSnapshot>>(emptyList()) }
    var materialSeleccionado by remember { mutableStateOf<DocumentSnapshot?>(null) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    // Campos de edición
    var nombreNuevo by remember { mutableStateOf("") }
    var descripcionNueva by remember { mutableStateOf("") }
    var precioNuevo by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("materiales")
            .get()
            .addOnSuccessListener { result ->
                materiales = result.documents
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Editar Materiales") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn {
                items(materiales) { doc ->
                    val nombre = doc.getString("nombre") ?: "Sin nombre"
                    val descripcion = doc.getString("descripcion") ?: ""
                    val precio = doc.getDouble("precioCm2") ?: 0.0

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                materialSeleccionado = doc
                                nombreNuevo = nombre
                                descripcionNueva = descripcion
                                precioNuevo = precio.toString()
                                mostrarDialogo = true
                            },
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Nombre: $nombre")
                            Text("Precio cm²: RD$${"%.2f".format(precio)}")
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo && materialSeleccionado != null) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Editar Material") },
            text = {
                Column {
                    OutlinedTextField(
                        value = nombreNuevo,
                        onValueChange = { nombreNuevo = it },
                        label = { Text("Nuevo nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = descripcionNueva,
                        onValueChange = { descripcionNueva = it },
                        label = { Text("Nueva descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = precioNuevo,
                        onValueChange = { precioNuevo = it },
                        label = { Text("Nuevo precio por cm²") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val precioDouble = precioNuevo.toDoubleOrNull()
                    if (precioDouble != null) {
                        Firebase.firestore.collection("materiales").document(materialSeleccionado!!.id)
                            .update(
                                mapOf(
                                    "nombre" to nombreNuevo,
                                    "descripcion" to descripcionNueva,
                                    "precioCm2" to precioDouble
                                )
                            )
                            .addOnSuccessListener {
                                Toast.makeText(context, "Material actualizado", Toast.LENGTH_SHORT).show()
                                mostrarDialogo = false
                                materialSeleccionado = null
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(context, "Precio inválido", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = {
                    mostrarDialogo = false
                    materialSeleccionado = null
                }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
