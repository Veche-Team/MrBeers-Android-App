package com.example.neverpidor.model.network.beer

// post and delete response
data class BeerResponse(
    val createdBeverage: Data = Data(),
    val msg: String = ""
)