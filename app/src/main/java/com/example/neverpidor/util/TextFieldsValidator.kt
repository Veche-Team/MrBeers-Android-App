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

    fun validateFields(
        title: String,
        description: String,
        type: String,
        price: String,
        alc: String? = null,
        volume: String? = null,
        itemId: String? = null
    ): InvalidFields {
        val invalidFields = arrayListOf<Pair<String, Int>>()
        if (title.isEmpty()) invalidFields.add(INPUT_TITLE)
        if (description.isEmpty()) invalidFields.add(INPUT_DESCRIPTION)
        if (type.isEmpty()) invalidFields.add(INPUT_TYPE)
        if (price.isEmpty()) invalidFields.add(EMPTY_PRICE)
        if (price.isNotEmpty() && price.toDouble() < 50.0) invalidFields.add(LOW_PRICE)
        if (price.isNotEmpty() && price.toDouble() > 500.0) invalidFields.add(HIGH_PRICE)
        alc?.let {
            if (it.isEmpty()) invalidFields.add(EMPTY_ALC)
            if (it.isNotEmpty() && it.toDouble() > 20.0) invalidFields.add(HIGH_ALC)
        }
        volume?.let {
            if (it.isEmpty()) invalidFields.add(EMPTY_VOLUME)
            if (it.isNotEmpty() && it.toDouble() < 0.25) invalidFields.add(LOW_VOLUME)
            if (it.isNotEmpty() && it.toDouble() > 5.00) invalidFields.add(HIGH_VOLUME)
        }
        return InvalidFields(invalidFields)
    }
}