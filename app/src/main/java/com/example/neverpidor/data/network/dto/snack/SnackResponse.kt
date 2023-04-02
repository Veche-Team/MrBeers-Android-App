package com.example.neverpidor.data.network.dto.snack

data class SnackResponse(
    val createdSnack: CreatedSnack = CreatedSnack(),
    val msg: String = ""
) {
    data class CreatedSnack(
        val UID: String = "",
        val createdAt: String = "",
        val description: String = "",
        val name: String = "",
        val price: Double = 0.0,
        val type: String = "",
        val updatedAt: String = ""
    )
}