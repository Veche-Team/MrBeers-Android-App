package com.example.neverpidor.util

import com.example.neverpidor.util.Constants.EMPTY_ALC
import com.example.neverpidor.util.Constants.EMPTY_PRICE
import com.example.neverpidor.util.Constants.EMPTY_VOLUME
import com.example.neverpidor.util.Constants.HIGH_ALC
import com.example.neverpidor.util.Constants.HIGH_PRICE
import com.example.neverpidor.util.Constants.HIGH_VOLUME
import com.example.neverpidor.util.Constants.INPUT_DESCRIPTION
import com.example.neverpidor.util.Constants.INPUT_TITLE
import com.example.neverpidor.util.Constants.INPUT_TYPE
import com.example.neverpidor.util.Constants.LOW_PRICE
import com.example.neverpidor.util.Constants.LOW_VOLUME
import javax.inject.Inject

class TextFieldsValidator @Inject constructor() {

    fun validateFields(input: ValidationModel): TextFieldValidationResult {
        var errors = mutableMapOf<String, Int>()
        if (input.title.isEmpty()) errors[INPUT_TITLE.first] = INPUT_TITLE.second

        if (input.description.isEmpty()) errors[INPUT_DESCRIPTION.first] = INPUT_DESCRIPTION.second
        if (input.type.isEmpty()) errors[INPUT_TYPE.first] = INPUT_TYPE.second
        if (input.price.isEmpty()) errors[EMPTY_PRICE.first] = EMPTY_PRICE.second
        if (input.price.isNotEmpty() && input.price.toDouble() < 50.0) errors[LOW_PRICE.first] =
            LOW_PRICE.second
        if (input.price.isNotEmpty() && input.price.toDouble() > 500.0) errors[HIGH_PRICE.first] =
            HIGH_PRICE.second
        input.alc?.let {
            if (it.isEmpty()) errors[EMPTY_ALC.first] = EMPTY_ALC.second
            if (it.isNotEmpty() && it.toDouble() > 20.0) errors[HIGH_ALC.first] = HIGH_ALC.second
        }
        input.volume?.let {
            if (it.isEmpty()) errors[EMPTY_VOLUME.first] = EMPTY_VOLUME.second
            if (it.isNotEmpty() && it.toDouble() < 0.25) errors[LOW_VOLUME.first] =
                LOW_VOLUME.second
            if (it.isNotEmpty() && it.toDouble() > 5.00) errors[HIGH_VOLUME.first] =
                HIGH_VOLUME.second
        }
        if (errors.isEmpty()) return TextFieldValidationResult.Success
        return TextFieldValidationResult.Failure(errors)
    }
}