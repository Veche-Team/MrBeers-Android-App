package com.example.neverpidor.presentation.fragments.itemlist.epoxy.models

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.neverpidor.R
import com.example.neverpidor.data.cart.InCartItem
import com.example.neverpidor.databinding.ModelMenuItemBinding
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel
import com.example.neverpidor.util.format

data class MenuItemEpoxyModel(
    val domainItem: DomainItem,
    val isLiked: Boolean,
    val onEditClick: (String) -> Unit,
    val onItemClick: (DomainItem) -> Unit,
    val onFavClick: (String) -> Unit,
    val inCartState: InCartItem,
    val onPlusClick: (InCartItem) -> Unit,
    val onMinusClick: (InCartItem) -> Unit,
    val onAddToCartClick: (String) -> Unit
) :
    ViewBindingKotlinModel<ModelMenuItemBinding>(R.layout.model_menu_item) {
    override fun ModelMenuItemBinding.bind() {

        nameText.text = domainItem.name
        price.text = root.context.getString(R.string.price, domainItem.price.format(2))

        editImage.setOnClickListener {
            onEditClick(domainItem.UID)
        }
        domainItem.image.let {
            shapeableImageView.setImageResource(it)
        }
        root.setOnClickListener {
            onItemClick(domainItem)
        }
        if (isLiked) {
            favImage.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else favImage.setImageResource(R.drawable.ic_baseline_favorite_border_24)

        favImage.setOnClickListener {
            onFavClick(domainItem.UID)
        }
        if (inCartState.quantity > 0) {
            cartImage.isGone = true
            cartQuantityText.isVisible = true
            addQuantityButton.isVisible = true
            removeQuantityButton.isVisible = true
            cartQuantityText.text = inCartState.quantity.toString()
        } else {
            cartImage.isVisible = true
            cartQuantityText.isGone = true
            addQuantityButton.isGone = true
            removeQuantityButton.isGone = true
        }
        addQuantityButton.setOnClickListener {
            onPlusClick(inCartState)
        }
        removeQuantityButton.setOnClickListener {
            onMinusClick(inCartState)
        }
        cartImage.setOnClickListener {
            onAddToCartClick(domainItem.UID)
        }
    }
}