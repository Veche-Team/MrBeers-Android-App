package com.example.neverpidor.presentation.fragments.singleItem.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.presentation.fragments.singleItem.epoxy.models.SingleItemOffersEpoxyModel

class SingleItemEpoxyController(
    private val onItemClick: (DomainItem) -> Unit,
    private val onFavClick: (DomainItem) -> Unit,
    private val onNoUserClick:() -> Unit,
    private val onPlusClick: (InCartItem) -> Unit,
    private val onMinusClick: (InCartItem) -> Unit,
    private val onAddToCartClick: (String) -> Unit,
) : EpoxyController() {

    var itemList = setOf<DomainItem>()
        set(value) {
            field = value
            requestModelBuild()
        }

    var likes = listOf<String>()
        set(value) {
            field = value
            requestModelBuild()
        }

    var inCartState = listOf<InCartItem>()

    var isUserLogged = false

    override fun buildModels() {
        itemList.forEach { domainItem ->
            SingleItemOffersEpoxyModel(
                domainItem,
                onItemClick = {
                    onItemClick(it)
                },
                isLiked = likes.any { it == domainItem.UID },
                onFavClick = onFavClick,
                isUserLogged = isUserLogged,
                onNoUserClick = onNoUserClick::invoke,
                onPlusClick = onPlusClick::invoke,
                onMinusClick = onMinusClick::invoke,
                onAddToCartClick = onAddToCartClick::invoke,
                inCartState = inCartState.find { it.UID == domainItem.UID }
                    ?: InCartItem()
            ).id(domainItem.UID).addTo(this)
        }
    }
}