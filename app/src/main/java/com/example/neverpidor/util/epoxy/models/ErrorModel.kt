package com.example.neverpidor.util.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelErrorBinding
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel

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