package edu.ucne.proyectofinal_ap2.presentation.navigation

import android.net.Uri
import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {
    object LoginScreen : Screen("LoginScreen")
    object RegisterScreen : Screen("RegisterScreen")
    object HomeScreen : Screen("HomeScreen")
    object VerPedidosScreen : Screen("VerPedidosScreen")
    object MisPedidosScreen : Screen("MisPedidosScreen")
    object PerfilScreen : Screen("PerfilScreen")
    object PersonalizarPedidoScreen : Screen("PersonalizarPedidoScreen")
    object ClienteMenuScreen : Screen("ClienteMenuScreen")
    object DetalleLetreroScreen : Screen("detalleLetrero/{nombre}/{descripcion}/{imagenUrl}") {
        fun createRoute(nombre: String, descripcion: String, imagenUrl: String): String {
            return "detalleLetrero/${Uri.encode(nombre)}/${Uri.encode(descripcion)}/${Uri.encode(imagenUrl)}"
        }
    }
    object AdminMenuScreen : Screen("AdminMenuScreen")
    object HomeClienteCatalogoScreen : Screen("HomeClienteCatalogoScreen")
    object AgregarLetreroScreen : Screen("AgregarLetreroScreen")
    object OpcionesLetreroScreen : Screen("OpcionesLetreroScreen")
    object EditarLetreroScreen : Screen("EditarLetreroScreen ")
    object EliminarLetreroScreen : Screen("EliminarLetreroScreen ")
    object AgregarMaterialScreen : Screen("AgregarMaterialScreen")
    object OpcionesMaterialesScreen : Screen("OpcionesMaterialesScreen ")
    object EditarMaterialesScreen : Screen("EditarMaterialesScreen")
    object EliminarMaterialesScreen : Screen("EliminarMaterialesScreen")
    object CrearPedidoScreen : Screen("CrearPedidoScreen")
    object  PagoInstruccionScreen : Screen(" PagoInstruccionScreen")
    object  ListaPedidosAdminScreen: Screen("ListaPedidosAdminScreen")
    object  CuentaBancoScreen: Screen("CuentaBancoScreen")
    object  ListaUsuariosScreen: Screen("ListaUsuariosScreen")



}
