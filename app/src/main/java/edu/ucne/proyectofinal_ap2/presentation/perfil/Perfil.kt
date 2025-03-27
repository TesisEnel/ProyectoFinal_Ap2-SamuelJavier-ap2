package edu.ucne.proyectofinal_ap2.presentation.perfil

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.proyectofinal_ap2.R


@Composable
fun PerfilScreen(
    userName: String = "Usuario",
    onChangePassword: (String) -> Unit = {},
    onChangePhoto: () -> Unit = {}
) {
    var cambiarContrasena by remember { mutableStateOf(false) }
    var nuevaContrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Perfil", fontSize = 24.sp, modifier = Modifier.padding(bottom = 24.dp))

        // Foto de perfil
        Box(
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .background(Color.LightGray),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.perfil), // tu imagen de perfil por defecto
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
            IconButton(onClick = { onChangePhoto() }) {
                Text(
                    text = "📷",
                    fontSize = 24.sp
                )
            }
        }

        Text(userName, fontSize = 20.sp, modifier = Modifier.padding(top = 16.dp))

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = { cambiarContrasena = !cambiarContrasena }) {
            Icon(Icons.Default.Edit, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cambiar contraseña")
        }

        if (cambiarContrasena) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = nuevaContrasena,
                onValueChange = { nuevaContrasena = it },
                label = { Text("Nueva contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Text(
                        text = if (passwordVisible) "👁️" else "🚫",
                        modifier = Modifier
                            .clickable { passwordVisible = !passwordVisible }
                            .padding(8.dp),
                        fontSize = 18.sp
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    onChangePassword(nuevaContrasena)
                    nuevaContrasena = ""
                    cambiarContrasena = false
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar contraseña")
            }
        }
    }
}