package edu.ucne.proyectofinal_ap2.presentation.navigation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.proyectofinal_ap2.data.entities.Pedido
import edu.ucne.proyectofinal_ap2.presentation.login.AuthViewModel
import edu.ucne.proyectofinal_ap2.presentation.login.LoginScreen
import edu.ucne.proyectofinal_ap2.presentation.login.RegisterScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.HomeScreen
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen.HomeScreen
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen.PerfilScreen
import edu.ucne.proyectofinal_ap2.presentation.pedidos.MisPedidosScreen
import edu.ucne.proyectofinal_ap2.presentation.pedidos.VerPedidosScreen
import edu.ucne.proyectofinal_ap2.presentation.perfil.PerfilScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(navHostController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navHostController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) { backStackEntry ->
            val authViewModel: AuthViewModel = hiltViewModel(backStackEntry)

            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.loginUser(
                        email = email,
                        password = password,
                        onSuccess = {
                            navHostController.navigate(Screen.HomeScreen.route)
                        },
                        onError = { mensaje ->
                            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                onGoToRegister = {
                    navHostController.navigate(Screen.RegisterScreen.route)
                }
            )
        }

        composable(Screen.HomeScreen.route) {
            // Obtener el mismo ViewModel que se creó en LoginScreen
            val parentEntry = remember(navHostController) {
                navHostController.getBackStackEntry(Screen.LoginScreen.route)
            }
            val authViewModel: AuthViewModel = hiltViewModel(parentEntry)

            val isAdmin by authViewModel.isAdmin
            val userName by authViewModel.userName

            LaunchedEffect(Unit) {
                println("¿Es admin dentro del HomeScreen? $isAdmin")
                println("Nombre: $userName")
            }

            HomeScreen(
                userName = userName,
                isAdmin = isAdmin,
                onVerPedidos = { navHostController.navigate(Screen.VerPedidosScreen.route) },
                onCrearPedido = { navHostController.navigate(Screen.LoginScreen.route) },
                onCerrarSesion = { navHostController.navigate(Screen.LoginScreen.route) },
                onPerfilClick = { navHostController.navigate(Screen.PerfilScreen.route) },
                onMisPedidosClick = { navHostController.navigate(Screen.MisPedidosScreen.route) }
            )
        }

        composable(Screen.RegisterScreen.route) {
            val authViewModel: AuthViewModel = hiltViewModel()

            RegisterScreen(
                onRegisterClick = { name, email, password, rol ->
                    authViewModel.registerUser(
                        name = name,
                        email = email,
                        password = password,
                        rol = rol,
                        onSuccess = {
                            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_LONG).show()
                            navHostController.navigate(Screen.LoginScreen.route)
                        },
                        onError = { mensaje ->
                            Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                onGoToLogin = {
                    navHostController.popBackStack()
                }
            )
        }

        composable(Screen.VerPedidosScreen.route) {
            VerPedidosScreen(
                onBackClick = { navHostController.popBackStack() }
            )
        }

        composable(Screen.MisPedidosScreen.route) {
            MisPedidosScreen(
                onBackClick = { navHostController.popBackStack() }
            )
        }

        composable(Screen.PerfilScreen.route) {
            PerfilScreen(
                userName = "Samy",
                onChangePassword = { navHostController.navigate(Screen.HomeScreen.route) },
                onChangePhoto = { navHostController.navigate(Screen.HomeScreen.route) }
            )
        }
    }
}
