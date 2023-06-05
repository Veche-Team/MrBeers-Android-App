package com.example.beers_app.presentation.fragments.register.util

data class RegisterErrors(
    val numberError: String = "",
    val nameError: String = "",
    val passwordError: String = "",
    val passwordRepeatError: String = ""
)