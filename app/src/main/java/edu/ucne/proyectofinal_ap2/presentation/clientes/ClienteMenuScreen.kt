package edu.ucne.proyectofinal_ap2.presentation.clientes

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
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
import com.google.firebase.auth.FirebaseAuth
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.presentation.menu.OpcionCard
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

@Composable
fun ClienteMenuScreen(
    onHacerPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onPerfil: () -> Unit,
    onCatalogo: () -> Unit,
    onCerrarSesion: () -> Unit,
    onPerfilClick: () -> Unit,
    onCuentaBanco: () -> Unit,
    onCarrito: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val pedidoViewModel: PedidoViewModel = hiltViewModel()
    val notificaciones = pedidoViewModel.notificacionesCliente
    val showNotificacionesMenu = remember { mutableStateOf(false) }
    val pedidos = pedidoViewModel.pedidos.collectAsState().value
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    val showCerrarSesionDialog = remember { mutableStateOf(false) }
    val activity = LocalContext.current as? Activity

    LaunchedEffect(Unit) {
        pedidoViewModel.obtenerPedidos()
    }

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onClose = { scope.launch { drawerState.close() } },
                onOptionClick = { opcion ->
                    when (opcion) {
                        DrawerOption.Home -> {}
                        DrawerOption.Perfil -> onPerfilClick()
                        DrawerOption.MisPedidos -> onMisPedidos()
                        DrawerOption.Carrito -> onCarrito()
                        DrawerOption.Catalogo -> onCatalogo()
                        DrawerOption.CuentaBanco -> onCuentaBanco()
                        DrawerOption.CerrarSesion -> showCerrarSesionDialog.value = true
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
                                showNotificacionesMenu.value = !showNotificacionesMenu.value
                                if (showNotificacionesMenu.value) {
                                    pedidoViewModel.marcarNotificacionesComoVistas()
                                }
                            }) {
                                Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color.White)
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
                                expanded = showNotificacionesMenu.value,
                                onDismissRequest = { showNotificacionesMenu.value = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(300.dp)
                            ) {
                                val notificacionesList = pedidos.filter {
                                    it.estado in listOf("aceptado", "rechazado", "listo") && it.userId == userId
                                }

                                if (notificacionesList.isEmpty()) {
                                    DropdownMenuItem(onClick = { }) {
                                        Text("No hay notificaciones.")
                                    }
                                } else {
                                    notificacionesList.forEachIndexed { index, pedido ->
                                        val fechaTexto = pedido.fecha?.let {
                                            val date = Date(it)
                                            java.text.SimpleDateFormat("dd/MM/yyyy", Locale("es")).format(date)
                                        } ?: "Fecha desconocida"

                                        DropdownMenuItem(onClick = { }) {
                                            Column {
                                                Text("📛 ${pedido.nombrePedido}", fontWeight = FontWeight.Bold)
                                                Text("🟡 Estado: ${pedido.estado}")
                                                Text("💬 ${pedido.mensaje}")
                                                Text("📅 $fechaTexto", fontSize = 12.sp, color = Color.Gray)
                                            }
                                        }
                                        if (index < notificacionesList.lastIndex) {
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
                                item { OpcionCard("Catalogo", R.drawable.catalogo) { onCatalogo() } }
                                item { OpcionCard("Mis Pedidos", R.drawable.pedido) { onMisPedidos() } }
                                item { OpcionCard("Crear Pedido", R.drawable.editar) { onHacerPedido() } }
                                item { OpcionCard("Carrito", R.drawable.carrito) { onCarrito() } }
                                item { OpcionCard("Cuenta Banco", R.drawable.cuentabanco) { onCuentaBanco() } }
                                item { OpcionCard("Perfil", R.drawable.perfil) { onPerfilClick() } }
                            }
                        }
                    }
                }
            )
            if (showCerrarSesionDialog.value) {
                AlertDialog(
                    onDismissRequest = { showCerrarSesionDialog.value = false },
                    title = { Text("Cerrar sesión") },
                    text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showCerrarSesionDialog.value = false
                            onCerrarSesion()
                        }) {
                            Text("Sí")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showCerrarSesionDialog.value = false }) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    )
    BackHandler { activity?.finish() }
}



@Composable
fun DrawerContent(
    onClose: () -> Unit,
    onOptionClick: (DrawerOption) -> Unit
) {
    val opciones = listOf(
        DrawerOption.Home,
        DrawerOption.Perfil,
        DrawerOption.MisPedidos,
        DrawerOption.Carrito,
        DrawerOption.Catalogo,
        DrawerOption.CuentaBanco,
        DrawerOption.CerrarSesion
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

sealed class DrawerOption(val label: String, val icon: Int) {
    object Home : DrawerOption("Home", R.drawable.home)
    object Perfil : DrawerOption("Perfil", R.drawable.perfil)
    object MisPedidos : DrawerOption("Mis pedidos", R.drawable.pedido)
    object Carrito : DrawerOption("Carrito", R.drawable.carrito)
    object Catalogo : DrawerOption("Catalogo", R.drawable.catalogo)
    object CuentaBanco : DrawerOption("Cuenta Banco", R.drawable.cuentabanco)
    object CerrarSesion : DrawerOption("Cerrar sesion", R.drawable.logout)
}