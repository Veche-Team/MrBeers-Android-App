package com.example.neverpidor.domain.use_cases.cart

import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.data.database.entities.UserAndItemsInCart
import com.example.neverpidor.domain.repositories.CartRepository
import com.example.neverpidor.util.mapper.MenuItemMapper

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
                    item.price,
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