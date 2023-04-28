package com.example.neverpidor.util.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelLoadingDataScreenBinding
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel

class LoadingScreenEpoxyModel :
    ViewBindingKotlinModel<ModelLoadingDataScreenBinding>(R.layout.model_loading_data_screen) {
    override fun ModelLoadingDataScreenBinding.bind() {
        lottie.playAnimation()
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}