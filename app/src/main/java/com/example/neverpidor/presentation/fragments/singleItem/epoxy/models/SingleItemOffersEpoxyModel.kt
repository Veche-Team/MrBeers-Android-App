package com.example.neverpidor.presentation.fragments.singleItem.epoxy.models

import android.graphics.Typeface
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.neverpidor.R
import com.example.neverpidor.databinding.ModelSingleItemOffersBinding
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel
import com.example.neverpidor.util.format

data class SingleItemOffersEpoxyModel(
    val domainItem: DomainItem,
    val onItemClick: (DomainItem) -> Unit,
    val isLiked: Boolean,
    val onFavClick: (DomainItem) -> Unit,
    val isUserLogged: Boolean,
    val onNoUserClick: () -> Unit,
    val inCartState: InCartItem,
    val onPlusClick: (InCartItem) -> Unit,
    val onMinusClick: (InCartItem) -> Unit,
    val onAddToCartClick: (String) -> Unit,
) :
    ViewBindingKotlinModel<ModelSingleItemOffersBinding>(R.layout.model_single_item_offers) {
    override fun ModelSingleItemOffersBinding.bind() {

        nameText.text = domainItem.name

        domainItem.image.let {
            shapeableImageView.setImageResource(it)
        }
        topPart.setOnClickListener {
            onItemClick(domainItem)
        }
        val image =
            if (isLiked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
        favImage.setImageResource(image)
        favImage.setOnClickListener {
            if (isUserLogged) {
                onFavClick(domainItem)
            } else {
                onNoUserClick()
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
                if (isUserLogged) {
                    onAddToCartClick(domainItem.UID)
                } else {
                    onNoUserClick()
                }
            }
        } else cartButton.isClickable = false
        addQuantityButton.setOnClickListener {
            onPlusClick(inCartState)
        }
        removeQuantityButton.setOnClickListener {
            onMinusClick(inCartState)
        }
        if (domainItem.salePercentage == 0.0) {
            price.text =  root.context.getString(R.string.price, domainItem.price.format(2))
            discountImage.isGone = true
        } else {
            price.typeface = Typeface.DEFAULT_BOLD
            price.text = root.context.getString(R.string.price, domainItem.discountedPrice.format(2))
            discountImage.isVisible = true
        }
    }
}