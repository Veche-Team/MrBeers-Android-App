package com.example.beers_app.presentation.fragments.menu_categories_list.epoxy.models

import com.example.beers.R
import com.example.beers.databinding.ModelPreviewBinding
import com.example.beers_app.data.common.MenuCategory
import com.example.beers_app.util.epoxy.ViewBindingKotlinModel

data class PreviewEpoxyModel(
    val menuCategoryEntity: MenuCategory,
    val onClick: (MenuCategory) -> Unit
) : ViewBindingKotlinModel<ModelPreviewBinding>(
    R.layout.model_preview
) {
    override fun ModelPreviewBinding.bind() {
        rootLayout.setBackgroundResource(menuCategoryEntity.previewImage)
        titleText.text = root.context.getString(menuCategoryEntity.name)
        rootLayout.setOnClickListener {
            onClick(menuCategoryEntity)
        }
    }
}