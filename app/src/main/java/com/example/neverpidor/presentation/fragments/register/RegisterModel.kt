package com.example.neverpidor.presentation.fragments.register

data class RegisterModel(
    val number: String = "",
    val name: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)