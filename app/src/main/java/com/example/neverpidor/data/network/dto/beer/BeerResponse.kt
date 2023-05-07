package com.example.neverpidor.data.network.dto.beer

// post and delete response
data class BeerResponse(
    val createdBeverage: BeerData = BeerData(),
    val msg: String = ""
)