package com.example.beers_app.util.epoxy.models

import com.example.beers.R
import com.example.beers.databinding.EmptyListModelBinding
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel

data class EmptyListModel(val onToMenuClick: () -> Unit) :
    ViewBindingKotlinModel<EmptyListModelBinding>(R.layout.empty_list_model) {
    override fun EmptyListModelBinding.bind() {
        toMenuButton.setOnClickListener {
            onToMenuClick()
        }
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}