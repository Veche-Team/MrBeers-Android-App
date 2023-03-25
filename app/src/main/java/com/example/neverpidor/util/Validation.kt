package com.example.neverpidor.util

data class ValidationModel(
    val title: String,
    val description: String,
    val type: String,
    val price: String,
    val alc: String? = null,
    val volume: String? = null
)
