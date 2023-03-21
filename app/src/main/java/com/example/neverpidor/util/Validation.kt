package com.example.neverpidor.util

data class ValidationModel(
    val title: String,
    val description: String,
    val type: String,
    val price: String,
    val alc: String? = null,
    val volume: String? = null
)

sealed class TextFieldValidationResult {
    object Success : TextFieldValidationResult()
    data class Failure(val errors: Map<String, Int>): TextFieldValidationResult()
}