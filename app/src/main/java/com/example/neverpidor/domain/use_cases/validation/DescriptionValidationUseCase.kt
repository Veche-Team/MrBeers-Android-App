package com.example.neverpidor.domain.use_cases.validation

import com.example.neverpidor.util.EmptyFieldException

class DescriptionValidationUseCase {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw EmptyFieldException("Description can't be empty")
        }
    }
}