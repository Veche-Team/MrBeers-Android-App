package com.example.neverpidor.model.mapper

import com.example.neverpidor.data.SnackPicturesProvider
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.model.entities.SnackEntity
import com.example.neverpidor.model.network.beer.Data
import javax.inject.Inject

class SnackMapper @Inject constructor(
    private val snackPicturesProvider: SnackPicturesProvider
) {

    fun buildDomainFromNetwork(data: Data): DomainSnack {
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
}