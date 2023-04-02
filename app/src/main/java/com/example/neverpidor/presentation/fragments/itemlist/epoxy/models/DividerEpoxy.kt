package com.example.neverpidor.presentation.fragments.itemlist.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.DividerBinding
import com.example.neverpidor.presentation.epoxy.ViewBindingKotlinModel

data class DividerEpoxy(val color: Int) :
    ViewBindingKotlinModel<DividerBinding>(R.layout.divider) {
    override fun DividerBinding.bind() {
        divideLine.setBackgroundResource(color)
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}