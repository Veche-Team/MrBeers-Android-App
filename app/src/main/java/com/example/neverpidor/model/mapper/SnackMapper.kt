package com.example.neverpidor.model.mapper

import com.example.neverpidor.data.providers.SnackPicturesProvider
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.entities.SnackEntity
import com.example.neverpidor.model.network.beer.Data
import com.example.neverpidor.model.network.snack.SnackResponse
import javax.inject.Inject

class SnackMapper @Inject constructor(
    private val snackPicturesProvider: SnackPicturesProvider
) {

    fun buildDomainFromEntity(snackEntity: SnackEntity): DomainSnack {
        return DomainSnack(
            description = snackEntity.description,
            name = snackEntity.name,
            price = snackEntity.price,
            type = snackEntity.type,
            UID = snackEntity.UID,
            isFaved = snackEntity.isFaved,
            isInCart = snackEntity.isInCart,
            image = snackPicturesProvider.getNotRandomPicture(
                snackEntity.UID.filter {
                    it in '0'..'9'
                }.map { it.digitToInt() }.sum()
            )
        )
    }
    fun buildEntityFromNetwork(data: Data): SnackEntity {
        return SnackEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
        )
    }
    fun buildEntityFromResponse(snackResponse: SnackResponse): SnackEntity {
        return SnackEntity(
            UID = snackResponse.createdSnack.UID,
            description = snackResponse.createdSnack.description,
            name = snackResponse.createdSnack.name,
            price = snackResponse.createdSnack.price,
            type = snackResponse.createdSnack.type
        )
    }
}