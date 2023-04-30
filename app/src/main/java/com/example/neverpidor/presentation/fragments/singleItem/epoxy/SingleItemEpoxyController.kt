package com.example.neverpidor.presentation.fragments.singleItem.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.presentation.fragments.singleItem.epoxy.models.SingleItemOffersEpoxyModel

class SingleItemEpoxyController(
    val onItemClick: (DomainItem) -> Unit,
    private val onFavClick: (DomainItem) -> Unit,
    val onNoUserClick:() -> Unit
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
                onNoUserClick = onNoUserClick::invoke
            ).id(domainItem.UID).addTo(this)
        }
    }
}