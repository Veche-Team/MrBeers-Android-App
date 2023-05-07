package com.example.neverpidor.data.network.dto.snack

data class SnackResponse(
    val createdSnack: SnackData = SnackData(),
    val msg: String = ""
)