package com.example.neverpidor.data

import com.example.neverpidor.R

class BeerPicturesProvider {

    private val beerPictures = listOf(
        R.drawable.a_beer1,
        R.drawable.a_beer2,
        R.drawable.a_beer3,
        R.drawable.a_beer4,
        R.drawable.a_beer5,
        R.drawable.a_beer6,
        R.drawable.a_beer7,
        R.drawable.a_beer8,
        R.drawable.a_beer9,
        R.drawable.a_beer10,
        R.drawable.a_beer11,
        R.drawable.a_beer12,
        R.drawable.a_beer13,
        R.drawable.a_beer14,
        R.drawable.a_beer15,
        R.drawable.a_beer16,
        R.drawable.a_beer17,
        R.drawable.a_beer18,
    )

    fun getRandomPicture(): Int = beerPictures.random()
    fun getNotRandomPicture(index: Int): Int {
        val size = beerPictures.size
        return beerPictures[index % size]

    }
}