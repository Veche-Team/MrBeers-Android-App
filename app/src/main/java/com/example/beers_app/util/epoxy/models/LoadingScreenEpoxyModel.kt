package com.example.beers_app.util.epoxy.models

import com.example.beers.R
import com.example.beers.databinding.ModelLoadingDataScreenBinding
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel

class LoadingScreenEpoxyModel :
    ViewBindingKotlinModel<ModelLoadingDataScreenBinding>(R.layout.model_loading_data_screen) {
    override fun ModelLoadingDataScreenBinding.bind() {
        lottie.playAnimation()
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}