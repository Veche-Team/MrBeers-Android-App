package com.example.neverpidor.model.network.snack

data class CreatedSnackResponse(
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