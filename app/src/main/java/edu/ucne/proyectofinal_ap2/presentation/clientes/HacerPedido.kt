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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.ucne.proyectofinal_ap2.data.entities.Pedido
import edu.ucne.proyectofinal_ap2.presentation.menu.MaterialViewModel
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import java.util.*

@Composable
fun CrearPedidoScreen(
    navHostController: NavHostController,
    materialViewModel: MaterialViewModel = hiltViewModel(),
    pedidoViewModel: PedidoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val materiales = materialViewModel.materiales

    var materialSeleccionado by remember { mutableStateOf<edu.ucne.proyectofinal_ap2.data.entities.Material?>(null) }
    var medidaSeleccionada by remember { mutableStateOf("Pequeño") }
    var alto by remember { mutableStateOf("") }
    var ancho by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf(0.0) }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imagenUri = it
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Selecciona un material", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        var expanded by remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            OutlinedTextField(
                value = materialSeleccionado?.nombre ?: "",
                onValueChange = {},
                label = { Text("Material") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                materiales.forEach { material ->
                    DropdownMenuItem(onClick = {
                        materialSeleccionado = material
                        expanded = false
                    }) {
                        Text(text = material.nombre)
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Selecciona el tamaño", fontWeight = FontWeight.Bold)

        val opciones = listOf("Pequeño", "Mediano", "Grande", "Personalizado")
        opciones.forEach { medida ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = medidaSeleccionada == medida,
                    onClick = {
                        medidaSeleccionada = medida
                        precio = pedidoViewModel.calcularPrecioPedido(medida, alto, ancho)
                    }
                )
                Text(medida)
            }
        }

        if (medidaSeleccionada == "Personalizado") {
            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = alto,
                    onValueChange = {
                        alto = it
                        precio = pedidoViewModel.calcularPrecioPedido(medidaSeleccionada, alto, ancho)
                    },
                    label = { Text("Alto (cm)") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Text("x", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = ancho,
                    onValueChange = {
                        ancho = it
                        precio = pedidoViewModel.calcularPrecioPedido(medidaSeleccionada, alto, ancho)
                    },
                    label = { Text("Ancho (cm)") },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text("Sube un logo o imagen opcional", fontWeight = FontWeight.Bold)

        Button(onClick = { launcher.launch("image/*") }) {
            Text("Seleccionar imagen")
        }

        imagenUri?.let {
            Spacer(Modifier.height(8.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }

        Spacer(Modifier.height(24.dp))
        Text("Precio estimado: RD$ $precio", fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button

                Firebase.firestore.collection("usuarios")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { doc ->
                        val nombre = doc.getString("nombre") ?: ""
                        val apellido = doc.getString("apellido") ?: ""
                        val direccion = doc.getString("direccion") ?: ""
                        val telefono = doc.getString("telefono") ?: ""

                        val pedido = Pedido(
                            id = UUID.randomUUID().toString(),
                            nombre = nombre,
                            apellido = apellido,
                            direccion = direccion,
                            telefono = telefono,
                            material = materialSeleccionado?.nombre ?: "",
                            medida = medidaSeleccionada,
                            alto = alto,
                            ancho = ancho,
                            logoUrl = imagenUri?.toString() ?: "",
                            precio = precio,
                            estado = "pendiente",
                            fecha = System.currentTimeMillis()
                        )

                        pedidoViewModel.guardarPedido(pedido,
                            onSuccess = {
                                navHostController.navigate(Screen.PagoInstruccionScreen.route)
                            },
                            onError = {
                                Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
            },
            enabled = materialSeleccionado != null
        ) {
            Text("Confirmar pedido")
        }
    }
}
