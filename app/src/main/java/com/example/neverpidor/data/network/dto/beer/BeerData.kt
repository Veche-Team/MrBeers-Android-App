package com.example.neverpidor.data.network.dto.beer

data class BeerData(
    val UID: String = "",
    val alcPercentage: Double = 0.0,
    val category: String = "",
    val createdAt: String = "",
    val description: String = "",
    val imagePath: String? = null,
    val isAvaliable: Boolean? = null,
    val name: String = "",
    val price: Double = 0.0,
    val salePercentage: Double? = null,
    val tags: String? = null,
    val type: String = "",
    val updatedAt: String = "",
    val volume: Double? = 0.0
)