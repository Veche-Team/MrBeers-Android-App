package com.example.neverpidor.model.network.beer

// get response for all beverages
@kotlinx.serialization.Serializable
data class BeerList(
    val `data`: List<Data> = listOf()
)

