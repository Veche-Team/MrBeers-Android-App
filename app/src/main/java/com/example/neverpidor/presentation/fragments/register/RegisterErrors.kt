package com.example.neverpidor.presentation.fragments.register

data class RegisterErrors(
    val numberError: String = "",
    val nameError: String = "",
    val passwordError: String = "",
    val passwordRepeatError: String = ""
)