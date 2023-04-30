package com.example.neverpidor.domain.use_cases.validation

import com.example.neverpidor.util.InvalidVolumeException

class VolumeValidationUseCase {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidVolumeException("Volume can't be empty")
        } else try {
            val volume = text.toDouble()
            if (volume > 5) {
                throw InvalidVolumeException("Too much volume")
            } else if (volume < 0.25) {
                throw InvalidVolumeException("Not enough volume")
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidVolumeException("Invalid input")
        }
    }
}