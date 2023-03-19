package com.example.neverpidor.model.network.beer

import com.example.neverpidor.model.network.Request

// body for post request
data class BeerRequest(
    val alcPercentage: Double,
    val description: String,
    val name: String,
    val price: Double,
    val type: String,
    val volume: Double
): Request()