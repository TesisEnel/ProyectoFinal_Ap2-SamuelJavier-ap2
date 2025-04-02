package edu.ucne.proyectofinal_ap2.presentation.perfil

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PerfilViewModel
import edu.ucne.proyectofinal_ap2.R

@Composable
fun PerfilScreen(viewModel: PerfilViewModel = viewModel(),  navController: NavHostController) {
    val context = LocalContext.current
    val user = Firebase.auth.currentUser
    val userData by viewModel.userData.collectAsState()

    var nuevaContrasena by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        imagenUri = it
    }

    LaunchedEffect(Unit) {
        viewModel.cargarDatosUsuario()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberAsyncImagePainter(imagenUri ?: userData?.fotoUrl),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            userData?.let { usuario ->
                Text("Nombre: ${usuario.nombre} ${usuario.apellido}")
                Text("Correo: ${user?.email}")
                Text("Teléfono: ${usuario.telefono}")
                Text("Dirección: ${usuario.direccion}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                label = { Text("Nueva contraseña") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    viewModel.actualizarPerfil(
                        nuevaContrasena,
                        imagenUri,
                        context
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar Perfil")
            }
        }
    }
}