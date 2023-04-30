package com.example.neverpidor.domain.use_cases.validation

import com.example.neverpidor.util.InvalidPriceException

class PriceValidationUseCase {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
          throw InvalidPriceException("Price can't be empty")
        } else try {
            val price = text.toDouble()
            if (price > 500.0) {
                throw InvalidPriceException("Price is too high")
            } else if (price < 50.0) {
                throw InvalidPriceException("Price is too low")
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidPriceException("Invalid input")
        }
    }
}