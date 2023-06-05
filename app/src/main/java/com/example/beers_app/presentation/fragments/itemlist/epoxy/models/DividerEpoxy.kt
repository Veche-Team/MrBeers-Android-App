package com.example.beers_app.presentation.fragments.itemlist.epoxy.models

import com.example.beers.R
import com.example.beers.databinding.DividerBinding
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel

class DividerEpoxy:
    ViewBindingKotlinModel<DividerBinding>(R.layout.divider) {
    override fun DividerBinding.bind() {}

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}