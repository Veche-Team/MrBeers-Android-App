package com.example.neverpidor.presentation.fragments.itemlist.epoxy.models

import android.graphics.Typeface
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.neverpidor.R
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.databinding.ModelMenuItemBinding
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.model.User
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
    val onAddToCartClick: (String) -> Unit,
    val userRole: User.Role,
    val onNoUserClick: () -> Unit
) :
    ViewBindingKotlinModel<ModelMenuItemBinding>(R.layout.model_menu_item) {
    override fun ModelMenuItemBinding.bind() {

        when (userRole) {
            User.Role.Admin -> editImage.isVisible = true
            else -> editImage.isGone = true
        }

        nameText.text = domainItem.name
        if (domainItem.salePercentage == 0.0) {
            price.text =  root.context.getString(R.string.price, domainItem.price.format(2))
            discountImage.isGone = true
        } else {
            price.typeface = Typeface.DEFAULT_BOLD
            price.text = root.context.getString(R.string.price, domainItem.discountedPrice.format(2))
            discountImage.isVisible = true
        }

        editImage.setOnClickListener {
            onEditClick(domainItem.UID)
        }
        domainItem.image.let {
            shapeableImageView.setImageResource(it)
        }
        topPart.setOnClickListener {
            onItemClick(domainItem)
        }
        if (isLiked) {
            favImage.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else favImage.setImageResource(R.drawable.ic_baseline_favorite_border_24)

        favImage.setOnClickListener {
            if (userRole is User.Role.NoUser) {
                onNoUserClick()
            } else {
                onFavClick(domainItem.UID)
            }
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
        if (cartImage.isVisible) {
            cartButton.setOnClickListener {
                if (userRole is User.Role.NoUser) {
                    onNoUserClick()
                } else {
                    onAddToCartClick(domainItem.UID)
                }
            }
        } else cartButton.isClickable = false
        addQuantityButton.setOnClickListener {
            onPlusClick(inCartState)
        }
        removeQuantityButton.setOnClickListener {
            onMinusClick(inCartState)
        }
    }
}