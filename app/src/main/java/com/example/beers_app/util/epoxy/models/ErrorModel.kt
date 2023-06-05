package com.example.beers_app.util.epoxy.models

import com.example.beers.R
import com.example.beers.databinding.ModelErrorBinding
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel

data class ErrorModel(val onRetry: () -> Unit) :
    ViewBindingKotlinModel<ModelErrorBinding>(R.layout.model_error) {
    override fun ModelErrorBinding.bind() {
        retryButton.setOnClickListener {
            onRetry()
        }
    }
    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}