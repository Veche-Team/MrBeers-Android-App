package com.example.neverpidor.ui.fragments.singleItem.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelSingleItemOffersBinding
import com.example.neverpidor.model.domain.DomainItem
import com.example.neverpidor.ui.epoxy.ViewBindingKotlinModel


data class SingleItemOffersEpoxyModel(
    val domainItem: DomainItem,
    val onItemClick: (DomainItem) -> Unit
) :
    ViewBindingKotlinModel<ModelSingleItemOffersBinding>(R.layout.model_single_item_offers) {
    override fun ModelSingleItemOffersBinding.bind() {
        nameText.text = domainItem.name
        price.text = root.context.getString(R.string.price, domainItem.price.toString())

        domainItem.image?.let {
            shapeableImageView.setImageResource(it)
        }
        root.setOnClickListener {
            onItemClick(domainItem)
        }
    }
}