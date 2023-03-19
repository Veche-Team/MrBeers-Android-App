package com.example.neverpidor.model.mapper

import com.example.neverpidor.data.SnackPicturesProvider
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.network.beer.Data
import javax.inject.Inject

class SnackMapper @Inject constructor(
    private val snackPicturesProvider: SnackPicturesProvider
) {

    fun buildFrom(data: Data): DomainSnack {
        return DomainSnack(
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            UID = data.UID,
            image = snackPicturesProvider.getNotRandomPicture(
                data.UID.filter {
                    it in '0'..'9'
                }.map { it.digitToInt() }.sum()
            )
        )
    }
}