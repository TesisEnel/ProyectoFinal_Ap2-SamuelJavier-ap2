package edu.ucne.proyectofinal_ap2.presentation.navigation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import edu.ucne.proyectofinal_ap2.presentation.viewModels.AuthViewModel
import edu.ucne.proyectofinal_ap2.presentation.login.LoginScreen
import edu.ucne.proyectofinal_ap2.presentation.login.RegisterScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.HomeScreen
import edu.ucne.proyectofinal_ap2.presentation.pedidos.PersonalizarPedidoScreen
import edu.ucne.proyectofinal_ap2.presentation.pedidos.VerPedidosScreen
import edu.ucne.proyectofinal_ap2.presentation.perfil.PerfilScreen
import edu.ucne.proyectofinal_ap2.presentation.admin.ListaPedidosAdminScreen
import edu.ucne.proyectofinal_ap2.presentation.clientes.ClienteMenuScreen
import edu.ucne.proyectofinal_ap2.presentation.admin.AdminMenuScreen
import edu.ucne.proyectofinal_ap2.presentation.admin.ListaUsuariosScreen
import edu.ucne.proyectofinal_ap2.presentation.clientes.CuentaBancoScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.AgregarLetreroScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.AgregarMaterialScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.EditarLetreroScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.EditarMaterialScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.EliminarLetreroScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.EliminarMaterialScreen
import edu.ucne.proyectofinal_ap2.presentation.clientes.HomeClienteCatalogo
import edu.ucne.proyectofinal_ap2.presentation.clientes.MisPedidosScreen
import edu.ucne.proyectofinal_ap2.presentation.clientes.PagoInstruccionScreen
import edu.ucne.proyectofinal_ap2.presentation.letreros.DetalleLetreroScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.MaterialViewModel
import edu.ucne.proyectofinal_ap2.presentation.viewModels.LetreroViewModel
import edu.ucne.proyectofinal_ap2.presentation.menu.OpcionesLetreroScreen
import edu.ucne.proyectofinal_ap2.presentation.menu.OpcionesMaterialesScreen
import edu.ucne.proyectofinal_ap2.presentation.pedidos.CrearPedidoScreen
import edu.ucne.proyectofinal_ap2.presentation.viewModels.PedidoViewModel
import edu.ucne.proyectofinal_ap2.presentation.viewModels.UsuarioViewModel


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
                onVerPedidos = { navHostController.navigate(Screen.ListaPedidosAdminScreen.route) },
                onCrearPedido = { navHostController.navigate(Screen.LoginScreen.route) },
                onCerrarSesion = { navHostController.navigate(Screen.LoginScreen.route) },
                onPerfilClick = { navHostController.navigate(Screen.PerfilScreen.route) },
                onMisPedidosClick = { navHostController.navigate(Screen.MisPedidosScreen.route) },
                onIrAPersonalizar = { navHostController.navigate(Screen.DetalleLetreroScreen.route)},
                onMaterialClick  = { navHostController.navigate(Screen.PersonalizarPedidoScreen.route)},
                onOpcionesLetrero = { navHostController.navigate(Screen.OpcionesLetreroScreen.route)},
                onOpcionesMateriales = { navHostController.navigate(Screen.OpcionesMaterialesScreen.route)},
                onMisPedidos =  { navHostController.navigate(Screen.MisPedidosScreen.route)},
                onHacerPedidos =  { navHostController.navigate(Screen.CrearPedidoScreen.route)},
                onCatalogo = { navHostController.navigate(Screen.HomeClienteCatalogoScreen.route)},
                onVerUsuariosClick = { navHostController.navigate(Screen.ListaUsuariosScreen.route)},
                onCuentaBanco = { navHostController.navigate(Screen.CuentaBancoScreen.route)}
            )
        }

        composable(Screen.RegisterScreen.route) {
            val authViewModel: AuthViewModel = hiltViewModel()

            RegisterScreen(
                onRegisterClick = { nombre, apellido, telefono, direccion, email, password, rol ->
                    authViewModel.registerUser(
                        nombre = nombre,
                        apellido = apellido,
                        telefono = telefono,
                        direccion = direccion,
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

        composable(Screen.AdminMenuScreen.route) {
            AdminMenuScreen(
                onVerPedidosClick = { navHostController.popBackStack() },
                onOpcionesLetrero = { navHostController.navigate(Screen.OpcionesLetreroScreen.route)},
                onOpcionesMateriales = { navHostController.navigate(Screen.OpcionesMaterialesScreen.route)},
                onVerUsuariosClick = { navHostController.navigate(Screen.ListaUsuariosScreen.route)},
                onCerrarSesion = {
                    FirebaseAuth.getInstance().signOut()
                    navHostController.navigate("login") {
                        popUpTo("admin") { inclusive = true }
                    }
                },
                onPerfilClick =  { navHostController.navigate(Screen.PerfilScreen.route)}
            )
        }


        composable(Screen.EditarLetreroScreen.route) {
            EditarLetreroScreen(
      navController= navHostController
            )
        }
        composable(Screen.EliminarLetreroScreen.route) {
            EliminarLetreroScreen(
navController = navHostController
            )
        }

        composable(Screen.AgregarLetreroScreen.route) {
            val LetreroViewModel: LetreroViewModel = hiltViewModel()
            AgregarLetreroScreen(
                viewModel = LetreroViewModel,
                onLetreroAgregado = {
                    navHostController.popBackStack()
                },
                navController = navHostController
            )
        }

        composable(Screen.OpcionesLetreroScreen.route) {
            OpcionesLetreroScreen(
                onEditarClick = { navHostController.navigate(Screen.EditarLetreroScreen.route)},
                onAgregarClick = { navHostController.navigate(Screen.AgregarLetreroScreen.route)},
                onEliminarClick = { navHostController.navigate(Screen.EliminarLetreroScreen.route)},
                navController = navHostController
            )
        }

        composable(Screen.OpcionesMaterialesScreen.route) {
            OpcionesMaterialesScreen(
                onEditarClick = { navHostController.navigate(Screen.EditarMaterialesScreen.route)},
                onAgregarClick = { navHostController.navigate(Screen.AgregarMaterialScreen.route)},

                onEliminarClick = { navHostController.navigate(Screen.EliminarMaterialesScreen.route)},
                navController = navHostController
            )
        }

        composable(Screen.HomeClienteCatalogoScreen.route) {
            val letreroViewModel: LetreroViewModel = hiltViewModel()
            val materialViewModel: MaterialViewModel = hiltViewModel()
            HomeClienteCatalogo(

                onLetreroClick = { letrero ->
                    navHostController.navigate(
                        Screen.DetalleLetreroScreen.createRoute(
                            nombre = letrero.nombre,
                            descripcion = letrero.descripcion,
                            imagenUrl = letrero.imagenUrl
                        )
                    )
                },
                onMaterialClick = { material ->
                },
                letreroViewModel = letreroViewModel,
                materialViewModel = materialViewModel,
                navController = navHostController
            )
        }

        composable(Screen.AgregarMaterialScreen.route) {
            val MaterialViewModel: MaterialViewModel = hiltViewModel()
            AgregarMaterialScreen(
                viewModel = MaterialViewModel,
                onMaterialAgregado = {
                    navHostController.popBackStack()
                },
                navController = navHostController
            )
        }
        composable(Screen.EliminarMaterialesScreen.route) {
            EliminarMaterialScreen(
                navController = navHostController

            )
        }

        composable(Screen.EditarMaterialesScreen.route) {
            EditarMaterialScreen(
                  navController = navHostController
            )
        }

        composable(Screen.ListaUsuariosScreen.route) {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            ListaUsuariosScreen(
                navController = navHostController,
                usuarioViewModel = usuarioViewModel
            )
        }



        composable(
            route = "detalleLetrero/{nombre}/{descripcion}/{imagenUrl}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("descripcion") { type = NavType.StringType },
                navArgument("imagenUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val descripcion = backStackEntry.arguments?.getString("descripcion") ?: ""
            val imagenUrl = backStackEntry.arguments?.getString("imagenUrl") ?: ""

            DetalleLetreroScreen(
                nombre = nombre,
                descripcion = descripcion,
                imagenUrl = imagenUrl,
                onBack = { navHostController.popBackStack() }
            )
        }

        composable(Screen.PersonalizarPedidoScreen.route) {
            PersonalizarPedidoScreen(
            letreroId = 1,
                onPedidoConfirmado = {navHostController.popBackStack()}
            )
        }

        composable(Screen.CrearPedidoScreen.route) {
            val materialViewModel: MaterialViewModel = hiltViewModel()
            val pedidoViewModel: PedidoViewModel = hiltViewModel()

            CrearPedidoScreen(
                materialViewModel = materialViewModel,
                pedidoViewModel = pedidoViewModel,
                navHostController = navHostController

            )
        }

        composable(Screen.PagoInstruccionScreen.route) {
            PagoInstruccionScreen(navController = navHostController)
        }

        composable(Screen.CuentaBancoScreen.route) {
            CuentaBancoScreen(navController = navHostController)
        }

        composable(Screen.ListaPedidosAdminScreen.route) {
            val pedidoViewModel: PedidoViewModel = hiltViewModel()
            ListaPedidosAdminScreen(
                pedidoViewModel = pedidoViewModel,
                navHostController = navHostController

            )
        }



        composable(Screen.PerfilScreen.route) {

            PerfilScreen(
                navController = navHostController

            )
        }
        composable(Screen.MisPedidosScreen.route) {
            val pedidoViewModel: PedidoViewModel = hiltViewModel()
            MisPedidosScreen(
                pedidoViewModel = pedidoViewModel,
                navController = navHostController
            )
        }

        composable(Screen.ClienteMenuScreen.route) {
            ClienteMenuScreen(
                onPerfil = { navHostController.popBackStack() },
                onMisPedidos = { navHostController.navigate(Screen.MisPedidosScreen.route)},
                onHacerPedido = { navHostController.navigate(Screen.CrearPedidoScreen.route)},
                onCatalogo = { navHostController.navigate(Screen.HomeClienteCatalogoScreen.route)},
                onCerrarSesion = {
                    FirebaseAuth.getInstance().signOut()
                    navHostController.navigate("login") {
                        popUpTo("cliente") { inclusive = true }
                    }
                },
                onPerfilClick = { navHostController.navigate(Screen.PerfilScreen.route)},
                onCuentaBanco = {navHostController.navigate(Screen.CuentaBancoScreen.route)}
            )
        }

    }
}
