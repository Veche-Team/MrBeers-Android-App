package com.example.beers_app.data.network.dto.beer

// get response for all beverages
data class BeerList(
    val `data`: List<BeerData> = listOf()
)

