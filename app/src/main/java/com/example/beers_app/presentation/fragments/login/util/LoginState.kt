package com.example.beers_app.presentation.fragments.login.util

data class LoginState(
    val inputFields: LoginInputFields = LoginInputFields(),
    val errors: LoginFieldsErrors = LoginFieldsErrors(),
    val isButtonEnabled: Boolean = false
)
