package com.example.neverpidor.domain.use_cases.validation

import com.example.neverpidor.util.EmptyFieldException

class TypeValidationUseCase {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw EmptyFieldException("Type can't be empty")
        }
    }
}