package com.example.beers_app.presentation.fragments.itemlist.epoxy.models

import com.example.beers.R
import com.example.beers.databinding.ModelItemTypeBinding
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel

data class ItemTypeEpoxyModel(
    val type: String,
) :
    ViewBindingKotlinModel<ModelItemTypeBinding>(R.layout.model_item_type) {
    override fun ModelItemTypeBinding.bind() {
        typeText.text = type
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}
