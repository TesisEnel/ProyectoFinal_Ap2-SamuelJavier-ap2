package edu.ucne.proyectofinal_ap2.presentation.pedidos

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonalizarPedidoScreen(
    letreroId: Int,
    onPedidoConfirmado: () -> Unit
) {
    var texto by remember { mutableStateOf("") }


    val tiposLetrero = listOf("Letrero Luminoso", "Letrero de Acrílico", "Letrero de Madera", "Letrero de Metal")
    var tipoSeleccionado by remember { mutableStateOf(tiposLetrero[0]) }
    var tipoExpanded by remember { mutableStateOf(false) }

    val opcionesTamano = listOf("Pequeño", "Mediano", "Grande", "Personalizado")
    var tamanoSeleccionado by remember { mutableStateOf(opcionesTamano[0]) }
    var tamanoExpanded by remember { mutableStateOf(false) }
    var mostrarCampoPersonalizado by remember { mutableStateOf(false) }
    var tamanoPersonalizado by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Personaliza tu pedido", style = MaterialTheme.typography.h6)


        ExposedDropdownMenuBox(
            expanded = tipoExpanded,
            onExpandedChange = { tipoExpanded = !tipoExpanded }
        ) {
            OutlinedTextField(
                value = tipoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo de Letrero") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded) },
                modifier = Modifier.fillMaxWidth()

            )
            ExposedDropdownMenu(
                expanded = tipoExpanded,
                onDismissRequest = { tipoExpanded = false }
            ) {
                tiposLetrero.forEach { tipo ->
                    DropdownMenuItem(onClick = {
                        tipoSeleccionado = tipo
                        tipoExpanded = false
                    }) {
                        Text(tipo)
                    }
                }
            }
        }

        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Texto del letrero") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = tamanoExpanded,
            onExpandedChange = {
                tamanoExpanded = !tamanoExpanded
            }
        ) {
            OutlinedTextField(
                value = tamanoSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tamaño") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = tamanoExpanded) },
                modifier = Modifier.fillMaxWidth()

            )
            ExposedDropdownMenu(
                expanded = tamanoExpanded,
                onDismissRequest = { tamanoExpanded = false }
            ) {
                opcionesTamano.forEach { opcion ->
                    DropdownMenuItem(onClick = {
                        tamanoSeleccionado = opcion
                        mostrarCampoPersonalizado = opcion == "Personalizado"
                        tamanoExpanded = false
                    }) {
                        Text(opcion)
                    }
                }
            }
        }

        if (mostrarCampoPersonalizado) {
            OutlinedTextField(
                value = tamanoPersonalizado,
                onValueChange = { tamanoPersonalizado = it },
                label = { Text("Especifica el tamaño") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(onClick = {

        }) {
            Text("Subir imagen del logo")
        }

        Button(
            onClick = {
                val tamanoFinal = if (mostrarCampoPersonalizado) tamanoPersonalizado else tamanoSeleccionado
                println("Tipo: $tipoSeleccionado, Texto: $texto, Tamaño: $tamanoFinal")
                onPedidoConfirmado()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar Pedido")
        }
    }
}
