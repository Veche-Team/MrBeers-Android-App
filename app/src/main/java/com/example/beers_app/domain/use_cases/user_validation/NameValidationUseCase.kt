package com.example.beers_app.domain.use_cases.user_validation

import android.content.Context
import com.example.beers.R
import com.example.beers_app.util.InvalidNameException

class NameValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidNameException(context.getString(R.string.empty_field_exception))
        }
    }
}