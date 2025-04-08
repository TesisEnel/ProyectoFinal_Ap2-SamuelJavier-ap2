package edu.ucne.proyectofinal_ap2.presentation.clientes
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.presentation.menu.OpcionCard
import kotlinx.coroutines.launch

@Composable
fun ClienteMenuScreen(
    onHacerPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onPerfil: () -> Unit,
    onCatalogo: () -> Unit,
    onCerrarSesion: () -> Unit,
    onPerfilClick: () -> Unit,
    onCuentaBanco: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent { scope.launch { drawerState.close() } }
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )

                Column {
                    Spacer(modifier = Modifier.height(100.dp))


                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                            .background(Color.White)
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            item { OpcionCard("Catalogo", R.drawable.catalogo) { onCatalogo() } }
                            item { OpcionCard("Mis Pedidos", R.drawable.pedido) { onMisPedidos() } }
                            item { OpcionCard("Cuenta Banco", R.drawable.cuentabanco) { onCuentaBanco() } }
                        }
                    }
                }

                TopAppBar(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    elevation = 0.dp,
                    modifier = Modifier.height(56.dp)
                ) {
                    IconButton(onClick = { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                    }
                    Spacer(Modifier.weight(1f, true))
                    IconButton(onClick = {  }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = Color.White)
                    }
                }

                BottomAppBar(
                    backgroundColor = Color.Black,
                    contentColor = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(70.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                ) {
                    IconButton(onClick = { onHacerPedido() }) {
                        Icon(Icons.Filled.Home, contentDescription = "Home", tint = Color.White)
                    }
                    Spacer(Modifier.weight(1f, true))
                    IconButton(onClick = { onHacerPedido() }) {
                        Icon(Icons.Filled.List, contentDescription = "Make Order", tint = Color.White)
                    }
                    Spacer(Modifier.weight(1f, true))
                    IconButton(onClick = { onHacerPedido() }) {
                        Icon(Icons.Filled.Person, contentDescription = "Make Order", tint = Color.White)
                    }
                    Spacer(Modifier.weight(1f, true))
                    IconButton(onClick = { onCerrarSesion() }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Log Out", tint = Color.White)
                    }
                }
            }
        }
    )
}

@Composable
fun DrawerContent(onClose: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Home", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.h6)
        Text("Profile", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.h6)
        Text("Settings", modifier = Modifier.padding(8.dp), style = MaterialTheme.typography.h6)
    }
}
