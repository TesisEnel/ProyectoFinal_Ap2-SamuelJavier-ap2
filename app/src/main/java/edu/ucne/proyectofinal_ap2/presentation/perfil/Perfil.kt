package edu.ucne.proyectofinal_ap2.presentation.perfil

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PerfilViewModel

@Composable
fun PerfilScreen(viewModel: PerfilViewModel = viewModel(), navController: NavHostController) {
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
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                backgroundColor = Color.Black
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

            val painter = when {
                imagenUri != null -> rememberAsyncImagePainter(imagenUri)
                !userData?.fotoUrl.isNullOrEmpty() -> rememberAsyncImagePainter(userData?.fotoUrl)
                else -> painterResource(id = R.drawable.ic_profile_default)
            }

            Image(
                painter = painter,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Haz clic para cambiar tu foto",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            userData?.let { usuario ->
                Text("Nombre: ${usuario.nombre} ${usuario.apellido}")
                Text("Correo: ${user?.email}")
                Text("Dirección: ${usuario.telefono}")
                Text("Teléfono: ${usuario.direccion}")
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
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text("Actualizar Perfil")
            }
        }
    }
}
