package com.example.beers_app.presentation.fragments.menu_categories_list.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.beers_app.data.common.MenuCategory
import com.example.beers_app.presentation.fragments.menu_categories_list.epoxy.models.PreviewEpoxyModel

class MenuCategoriesListEpoxyController(private val onClick: (MenuCategory) -> Unit) : EpoxyController() {

    private val list: List<MenuCategory> = listOf(MenuCategory.BeerCategory, MenuCategory.SnackCategory)

    override fun buildModels() {
        list.forEach {
            PreviewEpoxyModel(it, onClick).id(it.id).addTo(this)
        }
    }
}