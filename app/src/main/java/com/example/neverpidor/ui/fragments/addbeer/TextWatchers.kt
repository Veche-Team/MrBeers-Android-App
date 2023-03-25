package com.example.neverpidor.ui.fragments.addbeer

import android.text.Editable
import android.text.TextWatcher
import com.example.neverpidor.databinding.AddBeerFragmentBinding

class TextWatchers(val binding: AddBeerFragmentBinding) {

    fun setWatchers() {
        binding.apply {
            nameEditText.addTextChangedListener(titleTextWatcher)
            descriptionEt.addTextChangedListener(descriptionWatcher)
            typeEt.addTextChangedListener(typeWatcher)
            priceEt.addTextChangedListener(priceWatcher)
            alcEt.addTextChangedListener(alcWatcher)
            volumeEt.addTextChangedListener(volumeWatcher)
        }
    }

    private val titleTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) binding.nameLayout.error = "Название не может быть пустым"
            }
        }
    }
    private val descriptionWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) binding.descriptionTextLayout.error = "Придумай хоть что-нибудь"
            }
        }
    }
    private val typeWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) binding.typeTextLayout.error = "Так не пойдет"
            }
        }
    }

    private val priceWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) {
                    binding.priceTextLayout.error = "Бесплатно только шлюхи в церкви"
                    return
                }
                if (it.toString().toDouble() > 500.0) {
                    binding.priceTextLayout.error = "Чет дорого"
                }
                if (it.toString().toDouble() < 50.0) {
                    binding.priceTextLayout.error = "Хули тут так мало?"
                }
            }
        }
    }
    private val alcWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) {
                    binding.alcTextLayout.error = "Надо бы добавить хмелю"
                    return
                }
                if (it.toString().toDouble() > 20.0) {
                    binding.alcTextLayout.error = "У нас здесь не кабак"
                }
            }
        }
    }
    private val volumeWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) {
                    binding.volumeTextLayout.error = "Наливать то куда?"
                    return
                }
                if (it.toString().toDouble() < 0.25) {
                    binding.volumeTextLayout.error = "Такими темпами не захмелеешь"
                }
                if (it.toString().toDouble() > 5.0) {
                    binding.volumeTextLayout.error = "Бочками пиво не продаем"
                }
            }
        }
    }
}