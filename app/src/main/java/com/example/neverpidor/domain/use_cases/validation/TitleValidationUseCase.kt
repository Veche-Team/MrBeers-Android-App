package com.example.neverpidor.domain.use_cases.validation

import com.example.neverpidor.util.EmptyFieldException

class TitleValidationUseCase {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw EmptyFieldException("Title can't be empty")
        }
    }
}