package edu.ucne.proyectofinal_ap2.presentation.menu
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.data.entities.Letrero
import edu.ucne.proyectofinal_ap2.data.entities.Material
import androidx.compose.foundation.lazy.items


@Composable
fun HomeClienteCatalogo(
    letreros: List<Letrero>,
    materiales: List<Material>,
    onLetreroClick: (Letrero) -> Unit,
    onMaterialClick: (Material) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Letreros", "Materiales")

    Column {
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
                                painter = painterResource(id = letrero.imagen),
                                contentDescription = letrero.nombre,
                                modifier = Modifier.size(120.dp)
                            )
                            Text(text = letrero.nombre)
                        }
                    }
                }
            }

            1 -> LazyColumn(
                modifier = Modifier.fillMaxSize().padding(8.dp)
            ) {
                items(materiales) { material ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onMaterialClick(material) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = material.nombre, fontWeight = FontWeight.Bold)
                            Text(text = material.descripcion)
                        }
                    }
                }
            }
        }
    }
}

