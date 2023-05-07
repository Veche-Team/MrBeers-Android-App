package com.example.neverpidor.domain.use_cases.user_validation

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.util.InvalidNumberException

class PhoneNumberValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidNumberException(context.getString(R.string.empty_field_exception))
        }
    }
}