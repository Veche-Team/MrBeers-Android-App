package com.example.neverpidor.util.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.EmptyListModelBinding
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel

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