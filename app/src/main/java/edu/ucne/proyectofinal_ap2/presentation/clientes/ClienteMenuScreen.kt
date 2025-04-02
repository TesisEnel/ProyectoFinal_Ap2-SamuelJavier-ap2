package edu.ucne.proyectofinal_ap2.presentation.clientes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.presentation.admin.AdminCard

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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Menu") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Panel Cliente",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )




            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {

                item {
                    OpcionCard("Ver Catálogo", R.drawable.catalogo) { onCatalogo() }
                }

                item {
                    OpcionCard("Mis Pedidos", R.drawable.pedido) { onMisPedidos() }
                }

                item {
                    OpcionCard("Hacer Pedido", R.drawable.editar) { onHacerPedido() }
                }
                item {
                    OpcionCard("Cuenta Banco", R.drawable.cuentabanco) { onCuentaBanco() }
                }

                item {
                    OpcionCard("Perfil", R.drawable.perfil) { onPerfilClick() }
                }

                item {
                    OpcionCard("Cerrar Sesión", R.drawable.logout) { onCerrarSesion() }
                }
            }
        }
    }
    }



@Composable
fun OpcionCard(titulo: String, imagenRes: Int, onClick: () -> Unit) {
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
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
