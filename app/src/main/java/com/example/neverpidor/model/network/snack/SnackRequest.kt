package com.example.neverpidor.model.network.snack

data class SnackRequest(
    val description: String,
    val name: String,
    val price: Double,
    val type: String
)