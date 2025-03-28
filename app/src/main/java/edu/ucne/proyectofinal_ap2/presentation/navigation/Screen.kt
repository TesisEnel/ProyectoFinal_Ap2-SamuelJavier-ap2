package edu.ucne.proyectofinal_ap2.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {
    object LoginScreen : Screen("LoginScreen")
    object RegisterScreen : Screen("RegisterScreen")
    object HomeScreen : Screen("HomeScreen")
    object VerPedidosScreen : Screen("VerPedidosScreen")
    object MisPedidosScreen : Screen("MisPedidosScreen")
    object PerfilScreen : Screen("PerfilScreen")
    object PersonalizarPedidoScreen : Screen("PersonalizarPedidoScreen")
    object MaterialesScreen : Screen("MaterialesScreen")
    object DetalleLetreroScreen : Screen("DetalleLetreroScreen")
    object AdminMenuScreen : Screen("AdminMenuScreen")
    object AgregarLetreroScreen : Screen("AgregarLetreroScreen")
    object OpcionesLetreroScreen : Screen("OpcionesLetreroScreen")
}
