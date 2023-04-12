package com.example.neverpidor.presentation.fragments.itemlist.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelItemTypeBinding
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel

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
