package com.example.neverpidor.domain.use_cases.menu_validation

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.util.Constants.MAX_WEIGHT
import com.example.neverpidor.util.Constants.MIN_WEIGHT
import com.example.neverpidor.util.InvalidWeightException

class WeightValidationUseCase(
    private val context: Context
) {
    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidWeightException(context.getString(R.string.empty_field_exception))
        }
        try {
            val weight = text.toDouble()
            if (weight > MAX_WEIGHT) {
                throw InvalidWeightException(context.getString(R.string.high_weight_field))
            }
            if (weight < MIN_WEIGHT) {
                throw InvalidWeightException(context.getString(R.string.low_weight_field))
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidWeightException(context.getString(R.string.invalid_input))
        }
    }
}