package com.example.neverpidor.util.mapper

import com.example.neverpidor.data.database.entities.MenuItemEntity
import com.example.neverpidor.data.network.dto.beer.BeerData
import com.example.neverpidor.data.providers.BeerPicturesProvider
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.snack.SnackData
import com.example.neverpidor.data.network.dto.snack.SnackResponse
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.data.providers.SnackPicturesProvider
import com.example.neverpidor.domain.model.DomainItem
import javax.inject.Inject

class MenuItemMapper @Inject constructor(
    private val beerPicturesProvider: BeerPicturesProvider,
    private val snackPicturesProvider: SnackPicturesProvider
) {
    fun buildDomainFromEntity(menuItemEntity: MenuItemEntity): DomainItem {
        if (menuItemEntity.category is MenuCategory.BeerCategory) {
            return DomainItem(
                alcPercentage = menuItemEntity.alcPercentage!!,
                description = menuItemEntity.description,
                name = menuItemEntity.name,
                price = menuItemEntity.price,
                type = menuItemEntity.type,
                UID = menuItemEntity.UID,
                image = beerPicturesProvider.getNotRandomPicture(menuItemEntity.UID.filter {
                    it in '0'..'9'
                }.map { it.digitToInt() }.sum()),
                category = MenuCategory.BeerCategory,
                salePercentage = menuItemEntity.salePercentage
            )
        } else return DomainItem(
            alcPercentage = 0.0,
            description = menuItemEntity.description,
            name = menuItemEntity.name,
            price = menuItemEntity.price,
            type = menuItemEntity.type,
            UID = menuItemEntity.UID,
            image = snackPicturesProvider.getNotRandomPicture(menuItemEntity.UID.filter {
                it in '0'..'9'
            }.map { it.digitToInt() }.sum()),
            category = MenuCategory.SnackCategory,
            weight = menuItemEntity.weight
        )
    }

    fun buildBeerEntityFromNetwork(data: BeerData): MenuItemEntity {
        return MenuItemEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            alcPercentage = data.alcPercentage,
            category = MenuCategory.BeerCategory,
            salePercentage = data.salePercentage?: 0.0
        )
    }

    fun buildSnackEntityFromNetwork(data: SnackData): MenuItemEntity {
        return MenuItemEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            alcPercentage = null,
            category = MenuCategory.SnackCategory,
            weight = data.weight ?: 100.0
        )
    }

    fun buildBeerEntityFromResponse(beerResponse: BeerResponse): MenuItemEntity {
        return MenuItemEntity(
            UID = beerResponse.createdBeverage.UID,
            description = beerResponse.createdBeverage.description,
            name = beerResponse.createdBeverage.name,
            price = beerResponse.createdBeverage.price,
            type = beerResponse.createdBeverage.type,
            alcPercentage = beerResponse.createdBeverage.alcPercentage,
            category = MenuCategory.BeerCategory,
            salePercentage = beerResponse.createdBeverage.salePercentage ?: 0.0
        )
    }

    fun buildSnackEntityFromResponse(snackResponse: SnackResponse): MenuItemEntity {
        return MenuItemEntity(
            UID = snackResponse.createdSnack.UID,
            description = snackResponse.createdSnack.description,
            name = snackResponse.createdSnack.name,
            price = snackResponse.createdSnack.price,
            type = snackResponse.createdSnack.type,
            category = MenuCategory.SnackCategory,
            alcPercentage = null,
            weight = snackResponse.createdSnack.weight ?: 100.0
        )
    }
}