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




@Composable
fun EditarMaterialScreen() {
    val context = LocalContext.current
    var materiales by remember { mutableStateOf<List<DocumentSnapshot>>(emptyList()) }
    var nombreNuevo by remember { mutableStateOf("") }
    var descripcionNueva by remember { mutableStateOf("") }
    var materialSeleccionado by remember { mutableStateOf<DocumentSnapshot?>(null) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("materiales")
            .get()
            .addOnSuccessListener { result ->
                materiales = result.documents
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Editar Materiales", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(materiales) { doc ->
                val nombre = doc.getString("nombre") ?: "Sin nombre"
                val descripcion = doc.getString("descripcion") ?: ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            materialSeleccionado = doc
                            nombreNuevo = nombre
                            descripcionNueva = descripcion
                        },
                    elevation = 4.dp
                ) {
                    Text(
                        text = nombre,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        materialSeleccionado?.let { doc ->
            Spacer(modifier = Modifier.height(16.dp))
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

            Button(onClick = {
                Firebase.firestore.collection("materiales").document(doc.id)
                    .update("nombre", nombreNuevo, "descripcion", descripcionNueva)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Material actualizado", Toast.LENGTH_SHORT).show()
                        materialSeleccionado = null
                    }
            }) {
                Text("Guardar cambios")
            }
        }
    }
}
