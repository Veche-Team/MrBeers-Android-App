package com.example.neverpidor.model.mapper

import com.example.neverpidor.data.providers.BeerPicturesProvider
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.entities.BeerEntity
import com.example.neverpidor.model.network.beer.BeerResponse
import com.example.neverpidor.model.network.beer.Data
import javax.inject.Inject

class BeerMapper @Inject constructor(
    private val beerPicturesProvider: BeerPicturesProvider
    ) {

    fun buildDomainFromEntity(beerEntity: BeerEntity): DomainBeer {
        return DomainBeer(
            alcPercentage = beerEntity.alcPercentage,
            description = beerEntity.description,
            name = beerEntity.name,
            price = beerEntity.price,
            type = beerEntity.type,
            volume = beerEntity.volume,
            UID = beerEntity.UID,
            isInCart = beerEntity.isInCart,
            isFaved = beerEntity.isFaved,
            image = beerPicturesProvider.getNotRandomPicture(beerEntity.UID.filter {
                it in '0'..'9'
            }.map { it.digitToInt() }.sum())
        )
    }
    fun buildEntityFromNetwork(data: Data): BeerEntity {
        return BeerEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            volume = data.volume,
            alcPercentage = data.alcPercentage
        )
    }
    fun buildEntityFromResponse(beerResponse: BeerResponse): BeerEntity {
        return BeerEntity(
            UID = beerResponse.createdBeverage.UID,
            description = beerResponse.createdBeverage.description,
            name = beerResponse.createdBeverage.name,
            price = beerResponse.createdBeverage.price,
            type = beerResponse.createdBeverage.type,
            alcPercentage = beerResponse.createdBeverage.alcPercentage,
            volume = beerResponse.createdBeverage.volume
        )
    }
}

