package com.example.beers_app.data.network.dto.snack

data class SnackResponse(
    val createdSnack: SnackData = SnackData(),
    val msg: String = ""
)