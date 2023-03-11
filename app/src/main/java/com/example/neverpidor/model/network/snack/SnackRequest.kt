package com.example.neverpidor.model.network.snack

import com.example.neverpidor.model.network.Request

data class SnackRequest(
    val description: String,
    val name: String,
    val price: Double,
    val type: String
): Request()