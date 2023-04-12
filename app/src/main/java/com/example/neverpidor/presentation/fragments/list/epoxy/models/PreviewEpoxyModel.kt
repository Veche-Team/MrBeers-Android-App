package com.example.neverpidor.presentation.fragments.list.epoxy.models

import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.ModelPreviewBinding
import com.example.neverpidor.util.epoxy.ViewBindingKotlinModel

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