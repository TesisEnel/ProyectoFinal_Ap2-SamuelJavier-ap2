package edu.ucne.proyectofinal_ap2.presentation.login
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen
import edu.ucne.proyectofinal_ap2.presentation.viewModels.AuthViewModel

@Composable
fun SplashScreen(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {
    LaunchedEffect(true) {
        authViewModel.verificarSesionActiva(
            onUsuarioAutenticado = { esAdmin, _ ->
                navController.navigate(if (esAdmin) Screen.AdminMenuScreen.route else Screen.ClienteMenuScreen.route) {
                    popUpTo(0)
                }
            },
            onNoAutenticado = {
                navController.navigate(Screen.LoginScreen.route) {
                    popUpTo(0)
                }
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.Blue,
            strokeWidth = 4.dp
        )
    }
}
