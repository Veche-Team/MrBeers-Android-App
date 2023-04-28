package com.example.neverpidor.util.mapper

import com.example.neverpidor.data.database.entities.MenuItemEntity
import com.example.neverpidor.data.providers.BeerPicturesProvider
import com.example.neverpidor.data.network.dto.beer.BeerResponse
import com.example.neverpidor.data.network.dto.beer.Data
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
                volume = menuItemEntity.volume!!,
                UID = menuItemEntity.UID,
                isInCart = menuItemEntity.isInCart,
                image = beerPicturesProvider.getNotRandomPicture(menuItemEntity.UID.filter {
                    it in '0'..'9'
                }.map { it.digitToInt() }.sum()),
                category = MenuCategory.BeerCategory
            )
        } else return DomainItem(
            alcPercentage = 0.0,
            description = menuItemEntity.description,
            name = menuItemEntity.name,
            price = menuItemEntity.price,
            type = menuItemEntity.type,
            volume = 0.0,
            UID = menuItemEntity.UID,
            isInCart = menuItemEntity.isInCart,
            image = snackPicturesProvider.getNotRandomPicture(menuItemEntity.UID.filter {
                it in '0'..'9'
            }.map { it.digitToInt() }.sum()),
            category = MenuCategory.SnackCategory
        )
    }

    fun buildBeerEntityFromNetwork(data: Data): MenuItemEntity {
        return MenuItemEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            volume = data.volume,
            alcPercentage = data.alcPercentage,
            category = MenuCategory.BeerCategory
        )
    }

    fun buildSnackEntityFromNetwork(data: Data): MenuItemEntity {
        return MenuItemEntity(
            UID = data.UID,
            description = data.description,
            name = data.name,
            price = data.price,
            type = data.type,
            volume = data.volume,
            alcPercentage = data.alcPercentage,
            category = MenuCategory.SnackCategory
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
            volume = beerResponse.createdBeverage.volume,
            category = MenuCategory.BeerCategory
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
            volume = null
        )
    }
}

