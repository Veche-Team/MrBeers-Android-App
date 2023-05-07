package com.example.neverpidor.presentation.fragments.cart.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.databinding.ItemInCartModelBinding
import com.example.neverpidor.domain.model.InCartItem
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel
import com.example.neverpidor.util.format

data class ItemInCartModel(
    val inCartItem: InCartItem,
    val onAddClick: (InCartItem) -> Unit,
    val onRemoveClick: (InCartItem) -> Unit,
    val onItemClick: (String) -> Unit
) :
    ViewBindingKotlinModel<ItemInCartModelBinding>(R.layout.item_in_cart_model) {
    override fun ItemInCartModelBinding.bind() {
        itemImage.setImageResource(inCartItem.image)
        titleText.text = inCartItem.title
        priceText.text = root.context.getString(R.string.price, inCartItem.price.format(2))
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