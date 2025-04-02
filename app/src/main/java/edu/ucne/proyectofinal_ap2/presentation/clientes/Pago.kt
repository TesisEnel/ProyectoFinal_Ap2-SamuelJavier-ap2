package edu.ucne.proyectofinal_ap2.presentation.clientes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen

@Composable
fun PagoInstruccionScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Instrucciones de Pago") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Gracias por tu pedido",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Para completar tu pedido, realiza el pago a la siguiente cuenta bancaria:")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Banco: Banreservas")
            Text("Cuenta: 001-123456789")
            Text("Nombre: Letreros Creativos SRL")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Una vez recibido el pago, tu pedido será procesado por un administrador.")
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    navController.navigate(Screen.ClienteMenuScreen.route) {
                        popUpTo(Screen.CrearPedidoScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver al menú principal")
            }
        }
    }
}
