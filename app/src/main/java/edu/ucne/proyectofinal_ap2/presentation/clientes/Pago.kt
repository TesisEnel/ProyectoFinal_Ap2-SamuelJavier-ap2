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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import edu.ucne.proyectofinal_ap2.R
import edu.ucne.proyectofinal_ap2.presentation.navigation.Screen

@Composable
fun PagoInstruccionScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.Black,
                contentColor = Color.White,
                elevation = 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Instrucciones de Pago",
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)

            )

            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
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
                                    text = "Gracias por tu pedido",
                                    style = MaterialTheme.typography.h6,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Para completar tu pedido, realiza el 60% del pago a la siguiente cuenta bancaria:",
                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Banco: Banreservas", fontWeight = FontWeight.Bold)
                                Text("Cuenta: 001-123456789", fontWeight = FontWeight.Bold)
                                Text("Nombre: Letreros Creativos SRL", fontWeight = FontWeight.Bold)
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
                                Spacer(modifier = Modifier.height(32.dp))
                                Button(
                                    onClick = {
                                        navController.navigate(Screen.ClienteMenuScreen.route) {
                                            popUpTo(Screen.CrearPedidoScreen.route) { inclusive = true }
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = Color.Black,
                                        contentColor = Color.White
                                ),
                                    shape = RoundedCornerShape(32.dp)
                                ){
                                    Text("Volver al menú principal")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
