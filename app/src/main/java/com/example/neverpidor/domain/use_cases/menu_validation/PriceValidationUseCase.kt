package com.example.neverpidor.domain.use_cases.menu_validation

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.util.Constants.MAX_PRICE
import com.example.neverpidor.util.Constants.MIN_PRICE
import com.example.neverpidor.util.InvalidPriceException

class PriceValidationUseCase(
    private val context: Context
) {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
          throw InvalidPriceException(context.getString(R.string.empty_field_exception))
        } else try {
            val price = text.toDouble()
            if (price > MAX_PRICE) {
                throw InvalidPriceException(context.getString(R.string.high_price_field))
            } else if (price < MIN_PRICE) {
                throw InvalidPriceException(context.getString(R.string.low_price_field))
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidPriceException(context.getString(R.string.invalid_input))
        }
    }
}