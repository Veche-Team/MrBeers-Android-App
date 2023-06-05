package com.example.beers_app.data.network.dto.snack

data class SnackData(
    val UID: String = "",
    val createdAt: String = "",
    val description: String = "",
    val imagePath: String? = "",
    val name: String = "",
    val price: Double = 0.0,
    val tags: String? = "",
    val type: String = "",
    val updatedAt: String = "",
    val weight: Double? = 0.0
)