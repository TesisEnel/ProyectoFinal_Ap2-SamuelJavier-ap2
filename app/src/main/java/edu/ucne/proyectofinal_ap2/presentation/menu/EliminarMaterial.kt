package edu.ucne.proyectofinal_ap2.presentation.menu

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

@Composable
fun EliminarMaterialScreen(navController: NavHostController) {
    val context = LocalContext.current
    var materiales by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var showDialog by remember { mutableStateOf(false) }
    var materialToDelete by remember { mutableStateOf<Pair<String, String>?>(null) }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("materiales")
            .get()
            .addOnSuccessListener { result ->
                materiales = result.documents.mapNotNull { doc ->
                    val nombre = doc.getString("nombre") ?: return@mapNotNull null
                    doc.id to nombre
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Eliminar Materiales") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            }
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            LazyColumn {
                items(materiales) { (id, nombre) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                materialToDelete = id to nombre
                                showDialog = true
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
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmación") },
            text = { Text("¿Estás seguro de querer eliminar el material '${materialToDelete?.second}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        materialToDelete?.let { (id, _) ->
                            Firebase.firestore.collection("materiales").document(id)
                                .delete()
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Material eliminado", Toast.LENGTH_SHORT).show()
                                    materiales = materiales.filterNot { it.first == id }
                                    showDialog = false
                                }
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
