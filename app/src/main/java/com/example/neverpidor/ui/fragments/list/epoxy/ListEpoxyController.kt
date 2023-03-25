package com.example.neverpidor.ui.fragments.list.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.data.Category
import com.example.neverpidor.data.MenuCategoryEntity
import com.example.neverpidor.ui.fragments.list.epoxy.models.PreviewEpoxyModel

class ListEpoxyController(private val onClick: (Category) -> Unit) : EpoxyController() {

    var list: List<MenuCategoryEntity> = listOf()

    override fun buildModels() {
        list.forEach {
            PreviewEpoxyModel(it, onClick).id(it.id).addTo(this)
        }
    }
}