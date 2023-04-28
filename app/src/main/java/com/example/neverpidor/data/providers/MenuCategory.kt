package com.example.neverpidor.data.providers

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.neverpidor.R

sealed class MenuCategory(
    val id: Int,
    @StringRes
    val name: Int,
    @DrawableRes
    val previewImage: Int
) {

    object BeerCategory: MenuCategory(
        id = 0,
        name = R.string.beer,
        previewImage = R.drawable.beer_preview
    )
    object SnackCategory: MenuCategory(
        id = 1,
        name = R.string.snacks,
        previewImage = R.drawable.snacks_preview
    )
    companion object {
        fun toMenuCategory(categoryString: String): MenuCategory {
            return when (categoryString) {
                BeerCategory.toString() -> BeerCategory
                SnackCategory.toString() -> SnackCategory
                else -> throw Exception("Unknown category")
            }
        }
    }
}
