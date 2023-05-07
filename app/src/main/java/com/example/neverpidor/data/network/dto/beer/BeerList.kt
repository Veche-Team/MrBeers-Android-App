package com.example.neverpidor.data.network.dto.beer

// get response for all beverages
data class BeerList(
    val `data`: List<BeerData> = listOf()
)

