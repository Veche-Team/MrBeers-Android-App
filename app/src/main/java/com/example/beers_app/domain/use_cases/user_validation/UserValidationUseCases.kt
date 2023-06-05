package com.example.beers_app.domain.use_cases.user_validation

class UserValidationUseCases(
    val passwordValidationUseCase: PasswordValidationUseCase,
    val phoneNumberValidationUseCase: PhoneNumberValidationUseCase,
    val nameValidationUseCase: NameValidationUseCase
)