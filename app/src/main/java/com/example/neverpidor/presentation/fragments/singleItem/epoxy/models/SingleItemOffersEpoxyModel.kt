package com.example.neverpidor.presentation.fragments.singleItem.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelSingleItemOffersBinding
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.presentation.epoxy.ViewBindingKotlinModel
import com.example.neverpidor.util.format

data class SingleItemOffersEpoxyModel(
    val domainItem: DomainItem,
    val onItemClick: (DomainItem) -> Unit
) :
    ViewBindingKotlinModel<ModelSingleItemOffersBinding>(R.layout.model_single_item_offers) {
    override fun ModelSingleItemOffersBinding.bind() {

        nameText.text = domainItem.name
        price.text = root.context.getString(R.string.price, domainItem.price.format(2))

        domainItem.image?.let {
            shapeableImageView.setImageResource(it)
        }
        root.setOnClickListener {
            onItemClick(domainItem)
        }
        val image =
            if (domainItem.isFaved) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        favImage.setImageResource(image)
    }
}