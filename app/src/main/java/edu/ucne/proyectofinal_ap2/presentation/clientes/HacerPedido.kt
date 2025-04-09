package edu.ucne.proyectofinal_ap2.presentation.pedidos

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    var medidaSeleccionada by remember { mutableStateOf("") }
    var alto by remember { mutableStateOf("") }
    var ancho by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf(0.0) }
    var cantidadTexto by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }
    var nombrePedido by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imagenUri = it
    }

    val medidasConDescripcion = mapOf(
        "Pequeño" to Pair("20", "20"),
        "Mediano" to Pair("40", "40"),
        "Grande" to Pair("60", "60"),
        "Personalizado" to Pair("", "")
    )

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.Black),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(90.dp))
                Text("Crear Pedido", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 50.dp))
                    .background(Color.White)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Selecciona un material", fontWeight = FontWeight.Bold)

                var expanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = materialSeleccionado?.nombre ?: "",
                        onValueChange = {},
                        label = { Text("Material") },
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        colors = textFieldColors(),
                        shape = RoundedCornerShape(32.dp),
                        modifier = Modifier.fillMaxWidth()
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
                                    medidaSeleccionada, alto, ancho, material.nombre,
                                    cantidadTexto.toIntOrNull() ?: 1
                                )
                            }) {
                                Text(material.nombre)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombrePedido,
                    onValueChange = { nombrePedido = it },
                    label = { Text("Nombre del pedido") },
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
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
                                    medida, alto, ancho,
                                    materialSeleccionado?.nombre ?: "",
                                    cantidadTexto.toIntOrNull() ?: 1
                                )
                            }
                        )
                        Text(if (medida != "Personalizado") "$medida (${medAlto}cm x ${medAncho}cm)" else medida)
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
                                    medidaSeleccionada, alto, ancho,
                                    materialSeleccionado?.nombre ?: "",
                                    cantidadTexto.toIntOrNull() ?: 1
                                )
                            },
                            label = { Text("Alto (cm)") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            colors = textFieldColors(),
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("x", fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        OutlinedTextField(
                            value = ancho,
                            onValueChange = {
                                ancho = it
                                precio = pedidoViewModel.calcularPrecioPedido(
                                    medidaSeleccionada, alto, ancho,
                                    materialSeleccionado?.nombre ?: "",
                                    cantidadTexto.toIntOrNull() ?: 1
                                )
                            },
                            label = { Text("Ancho (cm)") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            colors = textFieldColors(),
                            shape = RoundedCornerShape(32.dp),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = {
                        if (it.all(Char::isDigit)) {
                            cantidadTexto = it
                            precio = pedidoViewModel.calcularPrecioPedido(
                                medidaSeleccionada, alto, ancho,
                                materialSeleccionado?.nombre ?: "",
                                cantidadTexto.toIntOrNull() ?: 1
                            )
                        }
                    },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = textFieldColors(),
                    shape = RoundedCornerShape(32.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                Spacer(Modifier.height(16.dp))
                Text("Sube un logo o imagen", fontWeight = FontWeight.Bold)
                Button(onClick = { launcher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White),
                    shape = RoundedCornerShape(32.dp)) {
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

                        if (userId != null && materialSeleccionado != null && medidaSeleccionada.isNotEmpty()
                            && alto.isNotEmpty() && ancho.isNotEmpty()
                        ) {
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
                                direccion = "Dirección del usuario",
                                nombrePedido = nombrePedido,
                                descripcion = descripcion
                            )
                            Log.d("PEDIDO DEBUG", "Nombre: $nombrePedido - Descripción: $descripcion")
                            pedidoViewModel.guardarPedido(
                                pedido,
                                onSuccess = {
                                    Toast.makeText(context, "Pedido guardado con éxito", Toast.LENGTH_SHORT).show()
                                    navHostController.navigate(Screen.PagoInstruccionScreen.route)
                                },
                                onError = {
                                    Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
                                }
                            )
                            Log.d("guardarPedido", "Guardando nombrePedido: ${pedido.nombrePedido} y descripcion: ${pedido.descripcion}")

                        } else {
                            Toast.makeText(context, "Por favor completa todos los campos requeridos.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(32.dp),
                    enabled = materialSeleccionado != null && cantidadTexto.isNotEmpty() && alto.isNotEmpty() && ancho.isNotEmpty()
                ) {
                    Text("Confirmar pedido", color = Color.White)
                }

                Button(
                    onClick = {
                        val cantidadFinal = if (cantidadTexto.isBlank()) 1 else cantidadTexto.toInt()
                        val pedido = Pedido(
                            id = UUID.randomUUID().toString(),
                            material = materialSeleccionado!!.nombre,
                            medida = medidaSeleccionada,
                            alto = alto,
                            ancho = ancho,
                            logoUrl = imagenUri?.toString() ?: "",
                            precio = precio,
                            cantidad = cantidadFinal,
                            estado = "carrito",
                            fecha = System.currentTimeMillis(),
                            nombre = "Nombre",
                            apellido = "Apellido",
                            telefono = "Teléfono",
                            direccion = "Dirección",
                            nombrePedido = nombrePedido,
                            descripcion = descripcion
                        )

                        pedidoViewModel.agregarAlCarrito(
                            pedido,
                            onSuccess = {
                                Toast.makeText(context, "Agregado al carrito", Toast.LENGTH_SHORT).show()
                            },
                            onError = {
                                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text("Agregar al carrito", color = Color.White)
                }

            }
        }
    }
}


@Composable
fun textFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
    textColor = Color.Black,
    focusedBorderColor = Color.Black,
    unfocusedBorderColor = Color.Gray,
    focusedLabelColor = Color.Black,
    unfocusedLabelColor = Color.Black
)
