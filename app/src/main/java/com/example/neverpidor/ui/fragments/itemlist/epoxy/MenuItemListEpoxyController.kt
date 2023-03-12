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

    // snacks layer
    var snacks = listOf<DomainSnack>()
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

    //beer layer
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
                beerList.groupBy { it.type }.forEach { map ->
                    ItemTypeEpoxyModel(
                        map.key,
                        onTypeClick = { string ->
                            isShown = if (isShown.contains(string)) {
                                isShown - string
                            } else {
                                isShown + string
                            }
                        }
                    ).id(map.key.hashCode()).addTo(this)
                    DividerEpoxy(R.color.amber_dark).id(Random.nextDouble(100.0)).addTo(this)
                    map.value.filter { isShown.contains(it.type) }.forEach{ data ->
                        MenuItemEpoxyModel(data, onEditClick, onItemClick).id(data.UID).addTo(this)

                    }
                }
            }
            R.string.snacks -> {
                snacks.groupBy { it.type }.forEach { map ->
                    ItemTypeEpoxyModel(map.key, onTypeClick = { string ->
                        isShown = isShown + string
                    }).id(map.key.hashCode()).addTo(this)
                    DividerEpoxy(R.color.accent).id(Random.nextDouble(100.0)).addTo(this)
                    map.value.filter { isShown.contains(it.type) }.forEach {
                        MenuItemEpoxyModel(it, onEditClick, onItemClick).id(it.UID).addTo(this)
                        //      DividerEpoxy(R.color.black).id(Random.nextDouble(100.0)).addTo(this)
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



