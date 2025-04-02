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
fun CuentaBancoScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cuenta Banco") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
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
                text = "Aqui podras ver la cuenta bancaria para realizra los pagos de tus pedidos.",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Para completar tu pedido, realiza el pago a la siguiente cuenta bancaria:")
            Spacer(modifier = Modifier.height(8.dp))
            Text("Banco: Banreservas")
            Text("Cuenta: 001-123456789")
            Text("Nombre: EasySign")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Una vez recibido el pago, tu pedido será procesado por un administrador.")
            Spacer(modifier = Modifier.height(32.dp))

        }
    }
}
