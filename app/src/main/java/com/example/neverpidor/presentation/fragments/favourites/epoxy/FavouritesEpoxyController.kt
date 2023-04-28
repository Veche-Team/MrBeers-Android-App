package com.example.neverpidor.presentation.fragments.favourites.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.neverpidor.R
import com.example.neverpidor.databinding.EmptyListModelBinding
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.MenuItemListEpoxyController
import com.example.neverpidor.presentation.fragments.singleItem.epoxy.models.SingleItemOffersEpoxyModel
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel
import com.example.neverpidor.util.epoxy.models.LoadingScreenEpoxyModel

class FavouritesEpoxyController(
    val onItemClick: (DomainItem) -> Unit,
    val onFavClick: (DomainItem) -> Unit,
    val onRetry: () -> Unit,
    val onToMenuClick: () -> Unit
) : EpoxyController() {

    private var isLoading = true
    var isError = false
        set(value) {
            field = value
            isLoading = false
            requestModelBuild()
        }

    var items = listOf<DomainItem>()
        set(value) {
            if (value.isNotEmpty()) {
                field = value
                isLoading = false
                requestModelBuild()
            }
        }

    override fun buildModels() {
        if (isLoading) {
            LoadingScreenEpoxyModel().id("Loading").addTo(this)
            return
        }
        if (isError) {
            MenuItemListEpoxyController.ErrorModel {
                isLoading = true
                onRetry()
                requestModelBuild()
            }.id("error").addTo(this)
        }
        if (items.isEmpty()) {
            EmptyListModel { onToMenuClick() }.id("empty").addTo(this)
            return
        }
        items.forEach {
            SingleItemOffersEpoxyModel(
                domainItem = it,
                onItemClick = { item ->
                    onItemClick(item)
                },
                isLiked = true,
                onFavClick = { item ->
                    onFavClick(item)
                }
            ).id(it.UID).addTo(this)
        }
    }

    data class EmptyListModel(val onToMenuClick: () -> Unit) :
        ViewBindingKotlinModel<EmptyListModelBinding>(R.layout.empty_list_model) {
        override fun EmptyListModelBinding.bind() {
            toMenuButton.setOnClickListener {
                onToMenuClick()
            }
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }

    }
}