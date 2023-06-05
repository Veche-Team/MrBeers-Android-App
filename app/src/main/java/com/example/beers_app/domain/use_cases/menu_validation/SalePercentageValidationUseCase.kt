package com.example.beers_app.domain.use_cases.menu_validation

import android.content.Context
import com.example.beers.R
import com.example.beers_app.util.Constants.MAX_SALE_PERCENTAGE
import com.example.beers_app.util.InvalidSalePercentageException

class SalePercentageValidationUseCase(
    private val context: Context
) {

    operator fun invoke(text: String) {
        if (text.isEmpty()) {
            throw InvalidSalePercentageException(context.getString(R.string.empty_field_exception))
        }
        try {
            val salePercentage = text.toDouble()
            if (salePercentage > MAX_SALE_PERCENTAGE) {
                throw InvalidSalePercentageException(context.getString(R.string.high_sale_percentage))
            }
        } catch (e: java.lang.NumberFormatException) {
            throw InvalidSalePercentageException(context.getString(R.string.invalid_input))
        }
    }
}