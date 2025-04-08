package edu.ucne.proyectofinal_ap2.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import edu.ucne.proyectofinal_ap2.R

@Composable
fun RegisterScreen(
    onRegisterClick: (
        nombre: String,
        apellido: String,
        direccion: String,
        telefono: String,
        email: String,
        password: String,
        rol: String
    ) -> Unit,
    onGoToLogin: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("cliente") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Black),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBack() },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(110.dp))
            Text(
                text = "Registrarse",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(780.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 50.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp)
            )

            OutlinedTextField(
                value = apellido,
                onValueChange = { apellido = it },
                label = { Text("Apellido") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp)
            )

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Direccion") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp)
            )

            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Telefono") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                shape = RoundedCornerShape(32.dp)
            )

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(32.dp)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contrasena") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp)
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contrasena") },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    unfocusedBorderColor = Color.Gray
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Selecciona tu Rol", fontWeight = FontWeight.SemiBold)

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = rol == "cliente",
                    onClick = { rol = "cliente" }
                )
                Text("Cliente")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = rol == "admin",
                    onClick = { rol = "admin" }
                )
                Text("Admin")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (
                        nombre.isBlank() || apellido.isBlank() || direccion.isBlank() ||
                        telefono.isBlank() || email.isBlank() || password.isBlank() ||
                        confirmPassword.isBlank() || password != confirmPassword
                    ) {
                        Toast.makeText(context, "Todos los Campos son obligatorios.", Toast.LENGTH_LONG).show()
                    } else {
                        onRegisterClick(nombre, apellido, direccion, telefono, email, password, rol)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                shape = RoundedCornerShape(32.dp)
            ) {
                Text("Registrar", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ya tienes una cuenta? Inicia Sesion",
                color = Color.Gray,
                modifier = Modifier.clickable(onClick = onGoToLogin),
                textAlign = TextAlign.Center
            )
        }
    }
}

