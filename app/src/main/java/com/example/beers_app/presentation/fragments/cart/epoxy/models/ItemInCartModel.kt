package com.example.beers_app.presentation.fragments.cart.epoxy.models

import com.example.beers.R
import com.example.beers.databinding.ItemInCartModelBinding
import com.example.beers_app.domain.model.InCartItem
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel
import com.squareup.picasso.Picasso

data class ItemInCartModel(
    val inCartItem: InCartItem,
    val onAddClick: (InCartItem) -> Unit,
    val onRemoveClick: (InCartItem) -> Unit,
    val onItemClick: (String) -> Unit
) :
    ViewBindingKotlinModel<ItemInCartModelBinding>(R.layout.item_in_cart_model) {
    override fun ItemInCartModelBinding.bind() {
        Picasso.get().load(inCartItem.image).into(itemImage)
        titleText.text = inCartItem.title
        priceText.text = root.context.getString(R.string.price, inCartItem.price * inCartItem.quantity)
        itemCount.text = inCartItem.quantity.toString()
        addButton.setOnClickListener {
            onAddClick(inCartItem)
        }
        removeButton.setOnClickListener {
            onRemoveClick(inCartItem)
        }
        itemImage.setOnClickListener {
            onItemClick(inCartItem.UID)
        }
        titleText.setOnClickListener {
            onItemClick(inCartItem.UID)
        }
    }
}