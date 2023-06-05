package com.example.beers_app.presentation.fragments.singleItem.epoxy.models

import android.graphics.Typeface
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.beers.R
import com.example.beers.databinding.ModelSingleItemOffersBinding
import com.example.beers_app.domain.model.DomainItem
import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel
import com.squareup.picasso.Picasso

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
            Picasso.get().load(it).into(shapeableImageView)
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
        if (domainItem.salePercentage == 0) {
            price.text =  root.context.getString(R.string.price, domainItem.price)
            discountImage.isGone = true
        } else {
            price.typeface = Typeface.DEFAULT_BOLD
            price.text = root.context.getString(R.string.price, domainItem.discountedPrice)
            discountImage.isVisible = true
        }
    }
}