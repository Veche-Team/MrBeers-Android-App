package com.example.neverpidor.domain.use_cases.user_validation

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.util.InvalidNameException

class NameValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidNameException(context.getString(R.string.empty_field_exception))
        }
    }
}