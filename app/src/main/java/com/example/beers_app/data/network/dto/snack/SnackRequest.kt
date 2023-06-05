package com.example.beers_app.data.network.dto.snack

data class SnackRequest(
    val description: String,
    val name: String,
    val price: Double,
    val type: String,
    val weight: Double,
    val imagePath: String?
)