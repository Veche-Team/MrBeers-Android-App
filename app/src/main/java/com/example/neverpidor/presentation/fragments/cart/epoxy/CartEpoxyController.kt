package com.example.neverpidor.presentation.fragments.cart.epoxy

import android.util.Log
import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.data.cart.InCartItem
import com.example.neverpidor.databinding.ItemInCartModelBinding
import com.example.neverpidor.presentation.fragments.favourites.epoxy.FavouritesEpoxyController
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel
import com.example.neverpidor.util.epoxy.models.LoadingScreenEpoxyModel
import com.example.neverpidor.util.format

class CartEpoxyController(
    val onAddClick: (InCartItem) -> Unit,
    val onRemoveClick: (InCartItem) -> Unit,
    val onToMenuClick: () -> Unit,
    val onItemClick: (String) -> Unit
) : EpoxyController() {

    var isLoading = true

    var cartItems = listOf<InCartItem>()
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {

        if (isLoading) {
            LoadingScreenEpoxyModel().id("Loading").addTo(this)
            return
        }
        if (cartItems.isEmpty()) {
            FavouritesEpoxyController.EmptyListModel(onToMenuClick).id("empty").addTo(this)
            return
        }

        cartItems.forEach {
            Log.e("Epoxy", it.title)
            ItemInCartModel(
                it,
                onAddClick = { inCartItem ->
                    onAddClick(inCartItem)
                },
                onRemoveClick = { inCartItem ->
                   onRemoveClick(inCartItem)
                },
                onItemClick = { id ->
                    onItemClick(id)
                }
            ).id(it.UID).addTo(this)
        }

    }

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
            priceText.text = inCartItem.price.format(2)
            itemCount.text = inCartItem.quantity.toString()
            addButton.setOnClickListener {
                onAddClick(inCartItem)
            }
            removeButton.setOnClickListener {
                onRemoveClick(inCartItem)
            }
            root.setOnClickListener {
                onItemClick(inCartItem.UID)
            }
        }
    }
}