package edu.ucne.proyectofinal_ap2.presentation.menu

import android.net.Uri
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import java.util.*

@Composable
fun AgregarMaterialScreen(
    viewModel: MaterialViewModel = hiltViewModel(),
    onMaterialAgregado: () -> Unit
) {
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imagenUri = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Agregar Nuevo Material", style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del material") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        Spacer(modifier = Modifier.height(8.dp))

        imagenUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombre.isBlank() || descripcion.isBlank() || imagenUri == null) {
                    Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.agregarMaterial(
                        nombre = nombre,
                        descripcion = descripcion,
                        imagenUri = imagenUri!!,
                        context = context,
                        onSuccess = {
                            Toast.makeText(context, "Material guardado", Toast.LENGTH_SHORT).show()
                            onMaterialAgregado()
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar material")
        }
    }
}