package edu.ucne.proyectofinal_ap2.presentation.pedidos

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.ucne.proyectofinal_ap2.data.entities.Material
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

    var materialSeleccionado by remember { mutableStateOf<Material?>(null) }
    var medidaSeleccionada by remember { mutableStateOf("Pequeño") }
    var alto by remember { mutableStateOf("") }
    var ancho by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf(0.0) }
    var cantidadTexto by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imagenUri = it
    }

    val medidasConDescripcion = mapOf(
        "Pequeño" to Pair("20", "20"),
        "Mediano" to Pair("40", "40"),
        "Grande" to Pair("60", "60"),
        "Personalizado" to Pair("", "")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Pedido") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text("Selecciona un material", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = materialSeleccionado?.nombre ?: "",
                    onValueChange = {},
                    label = { Text("Material") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
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
                            precio = pedidoViewModel.calcularPrecioPedido(
                                medidaSeleccionada,
                                alto,
                                ancho,
                                material.nombre,
                                if (cantidadTexto.isNotEmpty()) cantidadTexto.toInt() else 1
                            )
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
                val (medAlto, medAncho) = medidasConDescripcion[medida] ?: Pair("", "")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = medidaSeleccionada == medida,
                        onClick = {
                            medidaSeleccionada = medida
                            if (medida != "Personalizado") {
                                alto = medAlto
                                ancho = medAncho
                            }
                            precio = pedidoViewModel.calcularPrecioPedido(
                                medida,
                                alto,
                                ancho,
                                materialSeleccionado?.nombre ?: "",
                                if (cantidadTexto.isNotEmpty()) cantidadTexto.toInt() else 1
                            )
                        }
                    )
                    Text(
                        text = if (medida != "Personalizado") "$medida (${medAlto}cm x ${medAncho}cm)" else medida
                    )
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
                            precio = pedidoViewModel.calcularPrecioPedido(
                                medidaSeleccionada,
                                alto,
                                ancho,
                                materialSeleccionado?.nombre ?: "",
                                if (cantidadTexto.isNotEmpty()) cantidadTexto.toInt() else 1
                            )
                        },
                        label = { Text("Alto (cm)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("x", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = ancho,
                        onValueChange = {
                            ancho = it
                            precio = pedidoViewModel.calcularPrecioPedido(
                                medidaSeleccionada,
                                alto,
                                ancho,
                                materialSeleccionado?.nombre ?: "",
                                if (cantidadTexto.isNotEmpty()) cantidadTexto.toInt() else 1
                            )
                        },
                        label = { Text("Ancho (cm)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            cantidadTexto = it
                            if (cantidadTexto.isNotEmpty()) {
                                precio = pedidoViewModel.calcularPrecioPedido(
                                    medidaSeleccionada,
                                    alto,
                                    ancho,
                                    materialSeleccionado?.nombre ?: "",
                                    cantidadTexto.toInt()
                                )
                            }
                        }
                    },
                    label = { Text("Cantidad") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )
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
            Text("Precio estimado: RD$ ${"%.2f".format(precio)}", fontWeight = FontWeight.Bold)

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val cantidadFinal = if (cantidadTexto.isBlank()) 1 else cantidadTexto.toInt()
                    val userId = FirebaseAuth.getInstance().currentUser?.uid

                    if (userId != null && materialSeleccionado != null && medidaSeleccionada.isNotEmpty() && alto.isNotEmpty() && ancho.isNotEmpty()) {
                        val pedido = Pedido(
                            id = UUID.randomUUID().toString(),
                            material = materialSeleccionado!!.nombre,
                            medida = medidaSeleccionada,
                            alto = alto,
                            ancho = ancho,
                            logoUrl = imagenUri?.toString() ?: "",
                            precio = precio,
                            cantidad = cantidadFinal,
                            estado = "pendiente",
                            fecha = System.currentTimeMillis(),
                            nombre = "Nombre del usuario",
                            apellido = "Apellido del usuario",
                            telefono = "Número de teléfono",
                            direccion = "Dirección del usuario"
                        )

                        pedidoViewModel.guardarPedido(
                            pedido,
                            onSuccess = {
                                Toast.makeText(context, "Pedido guardado con éxito", Toast.LENGTH_SHORT).show()
                                navHostController.navigate(Screen.PagoInstruccionScreen.route)
                            },
                            onError = { error ->
                                Toast.makeText(context, "Error al guardar: $error", Toast.LENGTH_SHORT).show()
                            }
                        )
                    } else {
                        Toast.makeText(context, "Por favor completa todos los campos requeridos.", Toast.LENGTH_SHORT).show()
                    }
                },
                enabled = materialSeleccionado != null && cantidadTexto.isNotEmpty() && alto.isNotEmpty() && ancho.isNotEmpty()
            ) {
                Text("Confirmar pedido")
            }
        }
    }
}
