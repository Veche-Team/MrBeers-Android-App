package com.example.neverpidor.presentation.fragments.itemlist.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.util.epoxy.models.LoadingScreenEpoxyModel
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.DividerEpoxy
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.ItemTypeEpoxyModel
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.MenuItemEpoxyModel
import kotlin.random.Random

class MenuItemListEpoxyController(
    val category: MenuCategory,
    private val onEditClick: (String) -> Unit,
    private val onItemClick: (DomainItem) -> Unit,
    private val onFavClick: (DomainItem) -> Unit
) :
    EpoxyController() {

    var isLoading = true
    var searchInput = ""
        set(value) {
            field = value.lowercase()
            requestModelBuild()
        }

    var items = listOf<DomainItem>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    private var isShown = setOf<String>()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        if (isLoading) {
            LoadingScreenEpoxyModel().id("Loading").addTo(this)
            return
        }
        when (category) {
            MenuCategory.BeerCategory -> {
                items.filter {
                    it.name.lowercase().contains(searchInput) ||
                            it.type.lowercase().contains(searchInput)
                }.groupBy { it.type }
                    .forEach { map ->
                        ItemTypeEpoxyModel(
                            map.key,
                        ).id(map.key.hashCode()).addTo(this)
                        DividerEpoxy(R.color.amber_dark).id(Random.nextDouble(100.0)).addTo(this)
                        map.value.forEach { data ->
                            MenuItemEpoxyModel(data, onEditClick, onItemClick, onFavClick).id(data.UID)
                                .addTo(this)
                        }
                    }
            }
            MenuCategory.SnackCategory -> {
                items.filter {
                    it.name.lowercase().contains(searchInput) ||
                            it.type.lowercase().contains(searchInput)
                }.groupBy { it.type }
                    .forEach { map ->
                        ItemTypeEpoxyModel(
                            map.key,
                        ).id(map.key.hashCode()).addTo(this)
                        map.value.forEach {
                            MenuItemEpoxyModel(it, onEditClick, onItemClick, onFavClick).id(it.UID).addTo(this)
                        }
                    }
            }
        }
    }

    fun getShownState(): Set<String> = isShown
    fun setShownState(set: Set<String>) {
        isShown = set
    }
}



