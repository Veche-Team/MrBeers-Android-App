package com.example.neverpidor.ui.fragments.itemlist

import android.graphics.Color
import android.icu.text.Normalizer2.Mode
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.databinding.DividerBinding
import com.example.neverpidor.databinding.ModelItemTypeBinding
import com.example.neverpidor.databinding.ModelLoadingDataScreenBinding
import com.example.neverpidor.databinding.ModelMenuItemBinding
import com.example.neverpidor.model.beer.BeerList
import com.example.neverpidor.model.snack.SnackList
import com.example.neverpidor.ui.epoxy.ViewBindingKotlinModel
import kotlin.random.Random

class MenuItemListEpoxyController(val id: Int, val onEditClick: (String) -> Unit) :
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
    var beerList = BeerList()
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
            0 -> {
                beerList.data.groupBy { it.type }.forEach { map ->
                    ItemTypeEpoxyModel(map.key, onTypeClick = { string ->
                        isShown = string
                    }).id(map.key.hashCode()).addTo(this)
                    DividerEpoxy(R.color.accent).id(Random.nextDouble(100.0)).addTo(this)
                    map.value.filter { it.type == isShown }.forEach {
                        MenuItemEpoxyModel(it, id, onEditClick).id(it.UID).addTo(this)
                        DividerEpoxy(R.color.black).id(Random.nextDouble(100.0)).addTo(this)
                    }
                }
            }
            1 -> {
                snacks.data.groupBy { it.type }.forEach { map ->
                    ItemTypeEpoxyModel(map.key, onTypeClick = { string ->
                        isShown = string
                    }).id(map.key.hashCode()).addTo(this)
                    DividerEpoxy(R.color.accent).id(Random.nextDouble(100.0)).addTo(this)
                    map.value.filter { it.type == isShown }.forEach {
                        MenuItemEpoxyModel(it, id, onEditClick).id(it.UID).addTo(this)
                        DividerEpoxy(R.color.black).id(Random.nextDouble(100.0)).addTo(this)
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
    }

    data class MenuItemEpoxyModel(val data: com.example.neverpidor.model.beer.Data, val id: Int, val onEditClick: (String) -> Unit) :
        ViewBindingKotlinModel<ModelMenuItemBinding>(R.layout.model_menu_item) {
        override fun ModelMenuItemBinding.bind() {
            nameText.text = data.name
            description.isGone = true
            alcoholPercentageText.isGone = true
            volumeText.isGone = true
            price.text = "${data.price} P."
            var closed: Boolean = true
            editImage.setOnClickListener {
                onEditClick(data.UID)
            }
            root.setOnClickListener {

                if (closed) {
                    when (id) {
                        0 -> {
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
            }

        }
    }
}


data class DividerEpoxy(val color: Int) :
    ViewBindingKotlinModel<DividerBinding>(R.layout.divider) {
    override fun DividerBinding.bind() {
        divideLine.setBackgroundResource(color)
    }
}

