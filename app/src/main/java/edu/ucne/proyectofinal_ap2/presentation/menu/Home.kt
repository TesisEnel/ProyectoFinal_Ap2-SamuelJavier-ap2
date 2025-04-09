package edu.ucne.proyectofinal_ap2.presentation.menu

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalContext
import edu.ucne.proyectofinal_ap2.data.entities.Material
import edu.ucne.proyectofinal_ap2.presentation.admin.AdminMenuScreen
import edu.ucne.proyectofinal_ap2.presentation.clientes.ClienteMenuScreen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    userName: String,
    isAdmin: Boolean,
    onVerPedidos: () -> Unit,
    onCrearPedido: () -> Unit,
    onPerfilClick: () -> Unit,
    onMisPedidosClick: () -> Unit,
    onIrAPersonalizar: (Int) -> Unit,
    onMaterialClick: (Material) -> Unit,
    onOpcionesLetrero: () -> Unit,
    onOpcionesMateriales: () -> Unit,
    onMisPedidos: () -> Unit,
    onHacerPedidos: () -> Unit,
    onCatalogo: () -> Unit,
    onVerUsuariosClick: () -> Unit,
    onCerrarSesion: () -> Unit,
    onCuentaBanco: () -> Unit,
    onCarrito: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val activity = LocalContext.current as? Activity
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Text(
                    "Hola, $userName",
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.h6.fontSize
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text("Perfil", modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable {
                        scope.launch { drawerState.close() }
                        onPerfilClick()
                    })

                Text("Mis Pedidos", modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable {
                        scope.launch { drawerState.close() }
                        onMisPedidosClick()
                    })

                Text("Cerrar sesión", modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable {
                        scope.launch { drawerState.close() }
                        onCerrarSesion()
                    })
            }
        }
    ) {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isAdmin) {
                    AdminMenuScreen(
                        onVerPedidosClick = onVerPedidos,
                        onOpcionesLetrero = onOpcionesLetrero,
                        onOpcionesMateriales = onOpcionesMateriales,
                        onVerUsuariosClick = onVerUsuariosClick,
                        onCerrarSesion = onCerrarSesion,
                        onPerfilClick = onPerfilClick
                    )
                } else {
                    ClienteMenuScreen(
                        onHacerPedido = onHacerPedidos,
                        onMisPedidos = onMisPedidos,
                        onPerfil = {},
                        onCatalogo = onCatalogo,
                        onCerrarSesion = onCerrarSesion,
                        onPerfilClick = onPerfilClick,
                        onCuentaBanco = onCuentaBanco,
                        onCarrito = onCarrito
                    )
                }
            }
        }
    }
    BackHandler { activity?.finish() }
}

