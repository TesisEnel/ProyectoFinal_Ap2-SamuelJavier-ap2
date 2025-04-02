package edu.ucne.proyectofinal_ap2.presentation.clientes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.ucne.proyectofinal_ap2.R

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
                },
                backgroundColor = MaterialTheme.colors.primarySurface
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(Color(0xFFFAFAFA)),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.dinero),
                contentDescription = "Imagen decorativa",
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                backgroundColor = MaterialTheme.colors.surface
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Aquí podrás ver la cuenta bancaria para realizar los pagos de tus pedidos.",
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Para completar tu pedido, realiza el pago a la siguiente cuenta bancaria:",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Banco: Banreservas", fontWeight = FontWeight.Bold)
                    Text("Cuenta: 001-123456789", fontWeight = FontWeight.Bold)
                    Text("Nombre: EasySign", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Una vez recibido el pago, tu pedido será procesado por un administrador.",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        "Ojo, una vez pasado 7 Dias y no se realiza el pago su pedido pasara a ser rechazado.",
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Normal,
                        color = Color.Red
                    )
                }
            }
        }
    }
}
