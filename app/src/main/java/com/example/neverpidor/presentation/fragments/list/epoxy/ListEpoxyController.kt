package com.example.neverpidor.presentation.fragments.list.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.presentation.fragments.list.epoxy.models.PreviewEpoxyModel

class ListEpoxyController(private val onClick: (MenuCategory) -> Unit) : EpoxyController() {

    var list: List<MenuCategory> = listOf()

    override fun buildModels() {
        list.forEach {
            PreviewEpoxyModel(it, onClick).id(it.id).addTo(this)
        }
    }
}