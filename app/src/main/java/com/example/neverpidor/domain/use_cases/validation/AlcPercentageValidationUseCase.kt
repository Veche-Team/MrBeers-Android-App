package com.example.neverpidor.domain.use_cases.validation

import com.example.neverpidor.util.InvalidAlcPercentageException

class AlcPercentageValidationUseCase {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidAlcPercentageException("Alc percentage can't be empty")
        } else try {
            val alc = text.toDouble()
            if (alc > 20) {
                throw InvalidAlcPercentageException("Too much alcohol for a beer")
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidAlcPercentageException("Invalid input")
        }
    }
}