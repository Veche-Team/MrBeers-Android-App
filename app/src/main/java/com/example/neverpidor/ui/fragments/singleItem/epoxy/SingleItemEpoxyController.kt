package com.example.neverpidor.ui.fragments.singleItem.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.model.domain.DomainItem
import com.example.neverpidor.ui.fragments.singleItem.epoxy.models.SingleItemOffersEpoxyModel

class SingleItemEpoxyController(val onItemClick: (DomainItem) -> Unit) : EpoxyController() {

    var itemList = setOf<DomainItem>()
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
                }
            ).id(domainItem.UID).addTo(this)
        }
    }



}