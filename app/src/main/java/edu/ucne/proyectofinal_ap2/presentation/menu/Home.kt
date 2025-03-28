package edu.ucne.proyectofinal_ap2.presentation.menu

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
import androidx.compose.ui.graphics.Color
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.data.entities.Letrero
import edu.ucne.proyectofinal_ap2.data.entities.Material
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    userName: String = "Usuario",
    isAdmin: Boolean = false,
    onVerPedidos: () -> Unit,
    onCrearPedido: () -> Unit,
    onCerrarSesion: () -> Unit,
    onPerfilClick: () -> Unit,
    onMisPedidosClick: () -> Unit,
    onIrAPersonalizar: (Int) -> Unit,
    onMaterialClick: (Material) -> Unit,
    onOpcionesLetrero: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val letrerosDisponibles = listOf(
        Letrero(1, "Letrero Luminoso", R.drawable.letrero1),
        Letrero(2, "Letrero de Acrílico", R.drawable.letrero1)
    )
    val materialesDisponibles = listOf(
        Material(1, "Vinilo", "Material resistente al agua", R.drawable.letrero1),
        Material(2, "Acrílico", "Ideal para letreros luminosos", R.drawable.letrero1)
    )

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
                    .padding(8.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "¡Bienvenido, $userName!",
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                if (isAdmin) {
                    AdminMenuScreen(
                        onVerPedidosClick = onVerPedidos,
                        onOpcionesLetrero= onOpcionesLetrero,
                        onAgregarMaterialClick = {  }
                    )
                } else {
                    HomeClienteCatalogo(
                        letreros = letrerosDisponibles,
                        materiales = materialesDisponibles,
                        onLetreroClick = { letrero -> onIrAPersonalizar(letrero.id) },
                        onMaterialClick = onMaterialClick
                    )
                }
            }
        }
    }
}