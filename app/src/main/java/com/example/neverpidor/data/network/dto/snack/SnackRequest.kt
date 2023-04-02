package com.example.neverpidor.data.network.dto.snack

data class SnackRequest(
    val description: String,
    val name: String,
    val price: Double,
    val type: String
)