package edu.ucne.proyectofinal_ap2.presentation.menu

import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.*
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(
    userName: String = "Usuario",
    isAdmin: Boolean = false,
    onVerPedidos: () -> Unit,
    onCrearPedido: () -> Unit,
    onCerrarSesion: () -> Unit,
    onPerfilClick: () -> Unit,
    onMisPedidosClick: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Inicio") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Bienvenido, $userName!",
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 32.dp)
                )


                if (isAdmin) {
                    Text(
                        text = "👑 Modo administrador activado",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = onVerPedidos,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Ver pedidos")
                    }
                } else {
                    Button(
                        onClick = onCrearPedido,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Crear nuevo pedido")
                    }
                }
            }
        }
    }
}
