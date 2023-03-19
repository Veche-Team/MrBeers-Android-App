package com.example.neverpidor.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.neverpidor.R

data class MenuCategoryEntity(
    val id: Int,
    @StringRes
    val name: Int,
    @DrawableRes
    val previewImage: Int
) {
    companion object {

        fun getCategories() = listOf(
            MenuCategoryEntity(0, R.string.beer, R.drawable.beer_preview),
            MenuCategoryEntity(1, R.string.snacks, R.drawable.snacks_preview)
        )
    }
}