package com.example.neverpidor.ui.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelLoadingDataScreenBinding
import com.example.neverpidor.ui.epoxy.ViewBindingKotlinModel

class LoadingScreenEpoxyModel :
    ViewBindingKotlinModel<ModelLoadingDataScreenBinding>(R.layout.model_loading_data_screen) {
    override fun ModelLoadingDataScreenBinding.bind() {
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}