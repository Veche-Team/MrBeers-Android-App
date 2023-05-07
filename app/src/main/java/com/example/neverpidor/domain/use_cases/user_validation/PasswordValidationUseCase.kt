package com.example.neverpidor.domain.use_cases.user_validation

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.util.PasswordException

class PasswordValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.length in 1..5) {
            throw PasswordException.NewPasswordException(context.getString(R.string.short_password_exception))
        }
    }
}