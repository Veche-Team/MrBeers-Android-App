package com.example.neverpidor.domain.use_cases.menu_validation

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.util.EmptyFieldException

class DescriptionValidationUseCase(
    private val context: Context
) {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw EmptyFieldException(context.getString(R.string.empty_field_exception))
        }
    }
}