package com.example.neverpidor.model.beer

import com.example.neverpidor.model.Request

// body for post request
data class BeerRequest(
    val alcPercentage: Double,
    val description: String,
    val name: String,
    val price: Double,
    val type: String,
    val volume: Double
): Request()