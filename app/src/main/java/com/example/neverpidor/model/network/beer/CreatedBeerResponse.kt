package com.example.neverpidor.model.network.beer

// post and delete response
data class CreatedBeerResponse(
    val createdBeverage: Data = Data(),
    val msg: String = ""
)