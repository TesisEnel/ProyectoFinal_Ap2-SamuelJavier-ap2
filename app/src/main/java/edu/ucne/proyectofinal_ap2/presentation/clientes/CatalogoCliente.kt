package edu.ucne.proyectofinal_ap2.presentation.clientes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.data.entities.Letrero
import edu.ucne.proyectofinal_ap2.data.entities.Material
import edu.ucne.proyectofinal_ap2.presentation.menu.MaterialViewModel
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen
import edu.ucne.proyectofinal_ap2.presentation.viewModels.LetreroViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeClienteCatalogo(
    navController: NavHostController,
    onLetreroClick: (Letrero) -> Unit,
    onMaterialClick: (Material) -> Unit,
    onHacerPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onPerfil: () -> Unit,
    onCatalogo: () -> Unit,
    onCerrarSesion: () -> Unit,
    onPerfilClick: () -> Unit,
    onCuentaBanco: () -> Unit,
    onCarrito: () -> Unit,
    letreroViewModel: LetreroViewModel = hiltViewModel(),
    materialViewModel: MaterialViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Letreros", "Materiales")

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        letreroViewModel.obtenerLetreros()
        materialViewModel.obtenerMateriales()
    }
            Box(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black))

                Column {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .background(Color.Black),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Atras", tint = Color.White)
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Catálogo",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(48.dp))
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        Column {
                            ScrollableTabRow(
                                selectedTabIndex = selectedTab,
                                backgroundColor = Color.White,
                                contentColor = Color.Black,
                                modifier = Modifier.fillMaxWidth(),
                                edgePadding = 95.dp,
                                indicator = { tabPositions ->
                                    TabRowDefaults.Indicator(
                                        Modifier.tabIndicatorOffset(tabPositions[selectedTab])
                                    )
                                }
                            ) {
                                tabs.forEachIndexed { index, title ->
                                    Tab(
                                        selected = selectedTab == index,
                                        onClick = { selectedTab = index },
                                        text = { Text(title) },
                                        modifier = Modifier.width(IntrinsicSize.Max)
                                    )
                                }
                            }

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                if (selectedTab == 0) {
                                    items(letreroViewModel.letreros) { letrero ->
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable { onLetreroClick(letrero) },
                                            elevation = 4.dp,
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(model = letrero.imagenUrl),
                                                    contentDescription = letrero.nombre,
                                                    modifier = Modifier
                                                        .size(120.dp)
                                                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color.Black)
                                                        .padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = letrero.nombre,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Black,
                                                        fontFamily = FontFamily.SansSerif
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    items(materialViewModel.materiales) { material ->
                                        Card(
                                            modifier = Modifier
                                                .padding(8.dp)
                                                .clickable { onMaterialClick(material) },
                                            elevation = 4.dp,
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(model = material.imagenUrl),
                                                    contentDescription = material.nombre,
                                                    modifier = Modifier
                                                        .size(120.dp)
                                                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                                )
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .background(Color.Black)
                                                        .padding(vertical = 8.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = material.nombre,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Black,
                                                        fontFamily = FontFamily.SansSerif
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }




@Composable
fun DrawerContentCatalogo(
    onClose: () -> Unit,
    onOptionClick: (DrawerOptionCatalogo) -> Unit
) {
    val opciones = listOf(
        DrawerOptionCatalogo.Home,
        DrawerOptionCatalogo.Perfil,
        DrawerOptionCatalogo.MisPedidos,
        DrawerOptionCatalogo.Carrito,
        DrawerOptionCatalogo.Catalogo,
        DrawerOptionCatalogo.CuentaBanco,
        DrawerOptionCatalogo.CerrarSesion
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

sealed class DrawerOptionCatalogo(val label: String, val icon: Int) {
    object Home : DrawerOptionCatalogo("Home", R.drawable.home)
    object Perfil : DrawerOptionCatalogo("Perfil", R.drawable.perfil)
    object MisPedidos : DrawerOptionCatalogo("Mis pedidos", R.drawable.pedido)
    object Carrito : DrawerOptionCatalogo("Carrito", R.drawable.carrito)
    object Catalogo : DrawerOptionCatalogo("Catalogo", R.drawable.catalogo)
    object CuentaBanco : DrawerOptionCatalogo("Cuenta Banco", R.drawable.cuentabanco)
    object CerrarSesion : DrawerOptionCatalogo("Cerrar sesion", R.drawable.logout)
}
