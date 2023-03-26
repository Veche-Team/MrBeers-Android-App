package com.example.neverpidor.ui.fragments.itemlist.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelMenuItemBinding
import com.example.neverpidor.model.domain.DomainItem
import com.example.neverpidor.ui.epoxy.ViewBindingKotlinModel

data class MenuItemEpoxyModel(
    val domainItem: DomainItem,
    val onEditClick: (String) -> Unit,
    val onItemClick: (DomainItem) -> Unit,
    val onFavClick: (DomainItem) -> Unit
) :
    ViewBindingKotlinModel<ModelMenuItemBinding>(R.layout.model_menu_item) {
    override fun ModelMenuItemBinding.bind() {
        nameText.text = domainItem.name
        price.text = root.context.getString(R.string.price, domainItem.price.toString())

        editImage.setOnClickListener {
            onEditClick(domainItem.UID)
        }
        domainItem.image?.let {
            shapeableImageView.setImageResource(it)
        }
        root.setOnClickListener {
            onItemClick(domainItem)
        }
        if (domainItem.isFaved) {
            favImage.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else favImage.setImageResource(R.drawable.ic_baseline_favorite_border_24)

        favImage.setOnClickListener {
            onFavClick(domainItem)
        }
    }
}