package com.example.neverpidor.ui.fragments.itemlist.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.domain.DomainItem
import com.example.neverpidor.model.domain.DomainSnack
import com.example.neverpidor.ui.epoxy.models.LoadingScreenEpoxyModel
import com.example.neverpidor.ui.fragments.itemlist.epoxy.models.DividerEpoxy
import com.example.neverpidor.ui.fragments.itemlist.epoxy.models.ItemTypeEpoxyModel
import com.example.neverpidor.ui.fragments.itemlist.epoxy.models.MenuItemEpoxyModel
import kotlin.random.Random

class MenuItemListEpoxyController(
    val id: Int,
    private val onEditClick: (String) -> Unit,
    private val onItemClick: (DomainItem) -> Unit
) :
    EpoxyController() {

    var isLoading = true
    var searchInput = ""
        set(value) {
            field = value.lowercase()
            requestModelBuild()
        }

    private var isShown = setOf<String>()
        set(value) {
            field = value
            requestModelBuild()
        }

    var snacks = listOf<DomainSnack>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    var beerList = listOf<DomainBeer>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {

        if (isLoading) {
            LoadingScreenEpoxyModel().id("Loading").addTo(this)
            return
        }
        when (id) {
            R.string.beer -> {
                beerList.filter {
                    it.name.lowercase().contains(searchInput) ||
                            it.type.lowercase().contains(searchInput)
                }.groupBy { it.type }
                    .forEach { map ->
                        ItemTypeEpoxyModel(
                            map.key,
                        ).id(map.key.hashCode()).addTo(this)
                        DividerEpoxy(R.color.amber_dark).id(Random.nextDouble(100.0)).addTo(this)
                        map.value.forEach { data ->
                            MenuItemEpoxyModel(data, onEditClick, onItemClick).id(data.UID)
                                .addTo(this)
                        }
                    }
            }
            R.string.snacks -> {
                snacks.filter {
                    it.name.lowercase().contains(searchInput) ||
                            it.type.lowercase().contains(searchInput)
                }.groupBy { it.type }
                    .forEach { map ->
                        ItemTypeEpoxyModel(
                            map.key,
                        ).id(map.key.hashCode()).addTo(this)
                        map.value.forEach {
                            MenuItemEpoxyModel(it, onEditClick, onItemClick).id(it.UID).addTo(this)
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



