package com.example.neverpidor.presentation.fragments.singleItem.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.presentation.fragments.singleItem.epoxy.models.SingleItemOffersEpoxyModel

class SingleItemEpoxyController(
    val onItemClick: (DomainItem) -> Unit,
    private val onFavClick: (DomainItem) -> Unit
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

    override fun buildModels() {
        itemList.forEach { domainItem ->
            SingleItemOffersEpoxyModel(
                domainItem,
                onItemClick = {
                    onItemClick(it)
                },
                isLiked = likes.any { it == domainItem.UID },
                onFavClick = onFavClick
            ).id(domainItem.UID).addTo(this)
        }
    }
}