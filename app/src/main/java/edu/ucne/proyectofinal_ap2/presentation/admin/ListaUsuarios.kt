package edu.ucne.proyectofinal_ap2.presentation.admin

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import edu.ucne.proyectofinal_ap2.data.entities.Usuario
import edu.ucne.proyectofinal_ap2.presentation.viewModels.UsuarioViewModel


@Composable
fun ListaUsuariosScreen(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel = hiltViewModel()
) {
    val usuarios = usuarioViewModel.usuarios

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Usuarios Registrados") },
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                backgroundColor = Color.Black
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (usuarios.isEmpty()) {
                Text("No hay usuarios registrados.")
            } else {
                LazyColumn {
                    items(usuarios) { usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        model = usuario.fotoUrl.ifEmpty { "https://cdn-icons-png.flaticon.com/512/149/149071.png" }
                                    ),
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text("Nombre: ${usuario.nombre} ${usuario.apellido}")
                                    Text("Correo: ${usuario.email}")
                                    Text("Dirección: ${usuario.telefono}")
                                    Text("Teléfono: ${usuario.direccion}")
                                    Text("Rol: ${usuario.rol}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
