package com.example.beers_app.domain.use_cases.menu_validation

import android.content.Context
import com.example.beers.R
import com.example.beers_app.util.EmptyFieldException

class TypeValidationUseCase(
    private val context: Context
) {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw EmptyFieldException(context.getString(R.string.empty_field_exception))
        }
    }
}