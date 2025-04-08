package edu.ucne.proyectofinal_ap2.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.ucne.proyectofinal_ap2.R
import androidx.activity.compose.BackHandler
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.layout.ContentScale

@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit,
    onGoToRegister: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val activity = LocalContext.current as? Activity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Colocar la imagen de forma que actúe como un acento en lugar de dominar toda la pantalla
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Background Image",
            contentScale = ContentScale.Fit, // Ajusta la imagen proporcionalmente
            modifier = Modifier
                .size(275.dp) // Ajusta el tamaño según lo necesario
                .align(Alignment.TopCenter) // Posicionar en la parte superior central
                .offset(y = 50.dp)
        )

        // Fondo blanco con esquina redondeada y contenido del login
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(min = 500.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 75.dp)
                )
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        focusedLabelColor = Color.Black,  // Label color when the TextField is focused
                        unfocusedLabelColor = Color.Black,// Borde negro cuando está enfocado
                        unfocusedBorderColor = Color.Gray  // Borde gris cuando no está enfocado
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black,
                        focusedBorderColor = Color.Black, // Borde negro cuando está enfocado
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Black,  // Label color when the TextField is focused
                        unfocusedLabelColor = Color.Black// Borde gris cuando no está enfocado
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Login button
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_LONG).show()
                        } else {
                            onLoginClick(email, password)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Text("Login", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Don't have any account? Sign Up",
                    color = Color.Gray,
                    modifier = Modifier.clickable { onGoToRegister() }
                )
            }
        }
    }
    BackHandler { activity?.finish() }
}

