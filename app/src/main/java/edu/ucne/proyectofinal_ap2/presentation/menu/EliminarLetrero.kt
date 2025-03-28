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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.storage.ktx.storage



@Composable
fun EditarLetreroScreen() {
    val context = LocalContext.current
    var letreros by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var selectedId by remember { mutableStateOf<String?>(null) }
    var nuevoNombre by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("letreros").get()
            .addOnSuccessListener { result ->
                letreros = result.documents.map {
                    it.id to (it.getString("nombre") ?: "Sin nombre")
                }
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Editar Letrero", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        letreros.forEach { (id, nombre) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        selectedId = id
                        nuevoNombre = nombre
                    },
                elevation = 4.dp
            ) {
                Text(nombre, modifier = Modifier.padding(16.dp))
            }
        }

        if (selectedId != null) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nuevoNombre,
                onValueChange = { nuevoNombre = it },
                label = { Text("Nuevo nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                Firebase.firestore.collection("letreros").document(selectedId!!).update("nombre", nuevoNombre)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Letrero actualizado", Toast.LENGTH_SHORT).show()
                        selectedId = null
                        nuevoNombre = ""
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Actualizar")
            }
        }
    }
}
