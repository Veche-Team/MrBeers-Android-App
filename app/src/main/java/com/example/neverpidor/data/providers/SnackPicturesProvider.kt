package com.example.neverpidor.data.providers

import com.example.neverpidor.R
import javax.inject.Inject

class SnackPicturesProvider @Inject constructor() {

    private val snackPictures = listOf(
        R.drawable.a_snack1,
        R.drawable.a_snack2,
        R.drawable.a_snack3,
        R.drawable.a_snack4,
        R.drawable.a_snack5,
        R.drawable.a_snack6,
        R.drawable.a_snack7,
        R.drawable.a_snack8,
        R.drawable.a_snack9,
        R.drawable.a_snack10,
        R.drawable.a_snack11,
        R.drawable.a_snack12,
        R.drawable.a_snack13,
        R.drawable.a_snack14,
        R.drawable.a_snack15,
        R.drawable.a_snack16,
        R.drawable.a_snack17,
        R.drawable.a_snack18,
        R.drawable.a_snack19,
        R.drawable.a_snack20,
    )

    fun getNotRandomPicture(index: Int): Int {
        val size = snackPictures.size
        return snackPictures[index % size]
    }
}