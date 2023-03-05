package com.example.neverpidor.model.snack

import com.example.neverpidor.model.Request

data class SnackRequest(
    val description: String,
    val name: String,
    val price: Double,
    val type: String
): Request()