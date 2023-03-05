package com.example.neverpidor.model.beer

// post and delete response
data class BeerResponse(
    val createdBeverage: Data = Data(),
    val msg: String = ""
) {
    fun toBeer(): Beer = Beer(data = this.createdBeverage)
}