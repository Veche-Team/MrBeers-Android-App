package com.example.beers_app.domain.use_cases.cart

import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.data.database.entities.UserAndItemsInCart
import com.example.beers_app.domain.repositories.CartRepository
import com.example.beers_app.util.mapper.MenuItemMapper

class GetCartListUseCase(
    private val beerMapper: MenuItemMapper,
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(userAndItems: UserAndItemsInCart): List<InCartItem> {
        val list = mutableListOf<InCartItem>()
        userAndItems.menuItems.map { entity ->

            beerMapper.buildDomainFromEntity(entity)
        }.sortedBy { it.category.name }.forEach { item ->
            list.add(
                InCartItem(
                    item.UID,
                    item.name,
                    price = if (item.salePercentage == 0) item.price else item.discountedPrice,
                    cartRepository.getItemInCart(
                        userAndItems.user.phoneNumber,
                        item.UID
                    )!!.quantity,
                    item.image
                )
            )
        }
        return list
    }
}