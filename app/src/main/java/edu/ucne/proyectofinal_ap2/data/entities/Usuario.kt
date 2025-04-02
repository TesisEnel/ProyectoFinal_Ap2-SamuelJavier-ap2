package edu.ucne.proyectofinal_ap2.data.entities

data class Usuario(
    val id: String = "",
    val email: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val rol: String = "",
    val fotoUrl: String = ""
)