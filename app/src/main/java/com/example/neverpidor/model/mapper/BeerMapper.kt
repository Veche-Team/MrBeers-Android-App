package com.example.neverpidor.model.mapper

import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.network.beer.Beer
import com.example.neverpidor.model.network.beer.Data

class BeerMapper {
    
    fun buildFrom(data: Data): DomainBeer {
        return DomainBeer(
            alcPercentage = data.alcPercentage,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            volume = data.volume,
            UID = data.UID
        )
    }
}