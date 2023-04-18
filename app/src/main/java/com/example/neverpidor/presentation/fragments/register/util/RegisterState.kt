package com.example.neverpidor.presentation.fragments.register.util

data class RegisterState(
    val inputFields: RegisterInputFields = RegisterInputFields(),
    val errors: RegisterErrors = RegisterErrors(),
    val isButtonEnabled: Boolean = false
)
