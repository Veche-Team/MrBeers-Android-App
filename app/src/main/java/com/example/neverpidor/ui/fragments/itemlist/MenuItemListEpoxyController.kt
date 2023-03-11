package com.example.neverpidor.ui.fragments.itemlist

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.databinding.*
import com.example.neverpidor.model.domain.DomainBeer
import com.example.neverpidor.model.network.beer.BeerList
import com.example.neverpidor.model.network.beer.Data
import com.example.neverpidor.model.network.snack.SnackList
import com.example.neverpidor.ui.epoxy.ViewBindingKotlinModel
import kotlin.random.Random

class MenuItemListEpoxyController(val id: Int, private val onEditClick: (String) -> Unit) :
    EpoxyController() {

    var isLoading = true

    // snacks layer
    var snacks = SnackList()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    private var isShown = ""
        set(value) {
            if (value == field) {
                field = ""
                requestModelBuild()
            } else {
                field = value
                requestModelBuild()
            }
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
                    ItemTypeEpoxyModel(map.key, onTypeClick = { string ->
                        isShown = string
                    }).id(map.key.hashCode()).addTo(this)
                    DividerEpoxy(R.color.amber_dark).id(Random.nextDouble(100.0)).addTo(this)
                    map.value.filter { it.type == isShown }.forEachIndexed { index, data ->
                        MenuItemEpoxyModel(data , id, onEditClick).id(data.UID).addTo(this)
                     /*   if (index % 2 == 1) {
                            DividerEpoxy(R.color.amber_dark).id(Random.nextDouble(100.0)).addTo(this)
                        }*/
                    }
                }
            }
            R.string.snacks -> {
                snacks.data.groupBy { it.type }.forEach { map ->
                    ItemTypeEpoxyModel(map.key, onTypeClick = { string ->
                        isShown = string
                    }).id(map.key.hashCode()).addTo(this)
                    DividerEpoxy(R.color.accent).id(Random.nextDouble(100.0)).addTo(this)
                    map.value.filter { it.type == isShown }.forEach {
                        MenuItemEpoxyModel(it, id, onEditClick).id(it.UID).addTo(this)
                  //      DividerEpoxy(R.color.black).id(Random.nextDouble(100.0)).addTo(this)
                    }
                }
            }
        }
    }

    class LoadingScreenEpoxyModel :
        ViewBindingKotlinModel<ModelLoadingDataScreenBinding>(R.layout.model_loading_data_screen) {
        override fun ModelLoadingDataScreenBinding.bind() {

        }
    }

    data class ItemTypeEpoxyModel(
        val type: String,
        val onTypeClick: (String) -> Unit
    ) :
        ViewBindingKotlinModel<ModelItemTypeBinding>(R.layout.model_item_type) {
        override fun ModelItemTypeBinding.bind() {
            typeText.text = type
            root.setOnClickListener {
                onTypeClick(type)
            }
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }

    data class MenuItemEpoxyModel(val domainBeer: DomainBeer, val id: Int, val onEditClick: (String) -> Unit) :
        ViewBindingKotlinModel<ModelMenuItem2Binding>(R.layout.model_menu_item2) {
        override fun ModelMenuItem2Binding.bind() {
            nameText.text = domainBeer.name
            price.text = "${domainBeer.price} P."

            editImage.setOnClickListener {
                onEditClick(domainBeer.UID)
            }
          /*  description.isGone = true
            alcoholPercentageText.isGone = true
            volumeText.isGone = true

            var closed: Boolean = true

            root.setOnClickListener {

                if (closed) {
                    when (id) {
                        R.string.beer -> {
                            alcoholPercentageText.isVisible = true
                            alcoholPercentageText.text =
                                "Содержание алкоголя ${data.alcPercentage}%"
                            volumeText.isVisible = true
                            volumeText.text = "Объем: ${data.volume} Л"
                        }
                    }
                    description.isVisible = true
                    description.text = data.description

                } else {
                    description.isGone = true
                    alcoholPercentageText.isGone = true
                    volumeText.isGone = true
                }
                closed = !closed
            }*/

        }
    }
}


data class DividerEpoxy(val color: Int) :
    ViewBindingKotlinModel<DividerBinding>(R.layout.divider) {
    override fun DividerBinding.bind() {
        divideLine.setBackgroundResource(color)
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}

