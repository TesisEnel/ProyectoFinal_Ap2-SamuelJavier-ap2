package edu.ucne.proyectofinal_ap2.presentation.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

@Composable
fun AdminMenuScreen(
    onVerPedidosClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onOpcionesLetrero: () -> Unit,
    onOpcionesMateriales: () -> Unit,
    onVerUsuariosClick: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val showDialog = remember { mutableStateOf(false) }

    val pedidoViewModel: PedidoViewModel = hiltViewModel()
    val showMenu = remember { mutableStateOf(false) }
    val pedidos = pedidoViewModel.pedidos.collectAsState().value
    val notificaciones = pedidoViewModel.notificacionesAdmin

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContentAdmin(
                onClose = { scope.launch { drawerState.close() } },
                onOptionClick = { opcion ->
                    when (opcion) {
                        DrawerAdminOption.VerPedidos -> onVerPedidosClick()
                        DrawerAdminOption.Letreros -> onOpcionesLetrero()
                        DrawerAdminOption.Materiales -> onOpcionesMateriales()
                        DrawerAdminOption.Usuarios -> onVerUsuariosClick()
                        DrawerAdminOption.Perfil -> onPerfilClick()
                        DrawerAdminOption.CerrarSesion -> {
                            showDialog.value = true
                        }
                    }
                }
            )
        },
        content = {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = Color.Black,
                        contentColor = Color.White,
                        elevation = 0.dp
                    ) {
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Inicio",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }

                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            IconButton(onClick = {
                                showMenu.value = !showMenu.value
                                if (showMenu.value) {
                                    pedidoViewModel.marcarNotificacionesAdminComoVistas()
                                    pedidoViewModel.resetNotificacionesAdminLocal()
                                }
                            }) {
                                Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones", tint = Color.White)
                            }

                            if (notificaciones > 0) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-4).dp, y = 4.dp)
                                        .size(16.dp)
                                        .background(Color.Red, shape = RoundedCornerShape(50)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = notificaciones.toString(),
                                        color = Color.White,
                                        fontSize = 10.sp
                                    )
                                }
                            }

                            DropdownMenu(
                                expanded = showMenu.value,
                                onDismissRequest = { showMenu.value = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(300.dp)
                            ) {
                                val pendientes = pedidos.filter { it.estado == "pendiente" && !it.vistoAdmin }

                                if (pendientes.isEmpty()) {
                                    DropdownMenuItem(onClick = {}) {
                                        Text("Sin nuevos pedidos.")
                                    }
                                } else {
                                    pendientes.forEachIndexed { index, pedido ->
                                        val fechaTexto = pedido.fecha?.let {
                                            val date = Date(it)
                                            java.text.SimpleDateFormat("dd/MM/yyyy", Locale("es")).format(date)
                                        } ?: "Fecha desconocida"

                                        DropdownMenuItem(onClick = { }) {
                                            Column {
                                                Text("📛 ${pedido.nombrePedido}", fontWeight = FontWeight.Bold)
                                                Text("👤 ${pedido.nombre} ${pedido.apellido}")
                                                Text("📅 $fechaTexto", fontSize = 12.sp, color = Color.Gray)
                                            }
                                        }

                                        if (index < pendientes.lastIndex) {
                                            Divider(color = Color.LightGray, thickness = 1.dp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                content = { padding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black)
                            .padding(padding)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                                .background(Color.White)
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) {
                                item { AdminCard("Ver Pedidos", R.drawable.pedido, onVerPedidosClick) }
                                item { AdminCard("Letreros", R.drawable.letrero, onOpcionesLetrero) }
                                item { AdminCard("Materiales", R.drawable.material, onOpcionesMateriales) }
                                item { AdminCard("Usuarios", R.drawable.usuarios, onVerUsuariosClick) }
                                item { AdminCard("Perfil", R.drawable.perfil, onPerfilClick) }
                            }
                        }
                    }
                }
            )

            if (showDialog.value) {
                AlertDialog(
                    onDismissRequest = { showDialog.value = false },
                    title = { Text("Cerrar sesión") },
                    text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog.value = false
                            onCerrarSesion()
                        },
                            colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog.value = false },
                            colors = ButtonDefaults.buttonColors(Color.Red)) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    )
}



@Composable
fun AdminCard(titulo: String, imagenRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        elevation = 6.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = imagenRes),
                contentDescription = titulo,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
@Composable
fun DrawerContentAdmin(
    onClose: () -> Unit,
    onOptionClick: (DrawerAdminOption) -> Unit
) {
    val opciones = listOf(
        DrawerAdminOption.VerPedidos,
        DrawerAdminOption.Letreros,
        DrawerAdminOption.Materiales,
        DrawerAdminOption.Usuarios,
        DrawerAdminOption.Perfil,
        DrawerAdminOption.CerrarSesion
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        opciones.forEach { opcion ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp)
                    .clickable {
                        onOptionClick(opcion)
                        onClose()
                    }
            ) {
                Image(
                    painter = painterResource(id = opcion.icon),
                    contentDescription = opcion.label,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(14.dp))
                Text(
                    text = opcion.label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
sealed class DrawerAdminOption(val label: String, val icon: Int) {
    object VerPedidos : DrawerAdminOption("Ver Pedidos", R.drawable.pedido)
    object Letreros : DrawerAdminOption("Letreros", R.drawable.letrero)
    object Materiales : DrawerAdminOption("Materiales", R.drawable.material)
    object Usuarios : DrawerAdminOption("Usuarios", R.drawable.usuarios)
    object Perfil : DrawerAdminOption("Perfil", R.drawable.perfil)
    object CerrarSesion : DrawerAdminOption("Cerrar sesión", R.drawable.logout)
}
