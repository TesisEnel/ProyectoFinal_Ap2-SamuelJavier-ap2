package edu.ucne.proyectofinal_ap2.presentation.clientes

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import edu.ucne.proyectofinal_ap2.data.entities.Material
import edu.ucne.proyectofinal_ap2.presentation.viewModels.LetreroViewModel
import edu.ucne.proyectofinal_ap2.data.entities.Letrero
import edu.ucne.proyectofinal_ap2.presentation.menu.MaterialViewModel
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeClienteCatalogo(
    navController: NavHostController,
    onLetreroClick: (Letrero) -> Unit,
    onMaterialClick: (Material) -> Unit,
    letreroViewModel: LetreroViewModel = hiltViewModel(),
    materialViewModel: MaterialViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Letreros", "Materiales")

    LaunchedEffect(Unit) {
        letreroViewModel.obtenerLetreros()
        materialViewModel.obtenerMateriales()
    }

    val letreros = letreroViewModel.letreros
    val materiales = materialViewModel.materiales

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(letreros) { letrero ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onLetreroClick(letrero) }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = letrero.imagenUrl),
                                    contentDescription = letrero.nombre,
                                    modifier = Modifier.size(120.dp)
                                )
                                Text(
                                    text = letrero.nombre,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }

                1 -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(materiales) { material ->
                        Card(
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable { onMaterialClick(material) }
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = rememberAsyncImagePainter(model = material.imagenUrl),
                                    contentDescription = material.nombre,
                                    modifier = Modifier.size(120.dp)
                                )
                                Text(
                                    text = material.nombre,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

