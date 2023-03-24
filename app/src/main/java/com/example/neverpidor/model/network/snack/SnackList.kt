package com.example.neverpidor.model.network.snack

import com.example.neverpidor.model.network.beer.Data
@kotlinx.serialization.Serializable
data class SnackList(
    val `data`: List<Data> = listOf()
)