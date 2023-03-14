package com.example.neverpidor.model.mapper

import com.example.neverpidor.data.BeerPicturesProvider
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.network.beer.Data
import javax.inject.Inject

class BeerMapper @Inject constructor(
    private val beerPicturesProvider: BeerPicturesProvider
    ) {
    
    fun buildFrom(data: Data): DomainBeer {
        return DomainBeer(
            alcPercentage = data.alcPercentage,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            volume = data.volume,
            UID = data.UID,
            image = beerPicturesProvider.getNotRandomPicture(data.UID.filter {
                it in '0'..'9'
            }.map { it.digitToInt() }.sum())
        )
    }
}

