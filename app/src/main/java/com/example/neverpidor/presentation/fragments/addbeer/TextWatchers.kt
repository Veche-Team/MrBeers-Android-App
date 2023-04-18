/*
package com.example.neverpidor.presentation.fragments.addbeer

import android.text.Editable
import android.text.TextWatcher
import com.example.neverpidor.R
import com.example.neverpidor.databinding.AddBeerFragmentBinding
import com.example.neverpidor.util.Constants.MAX_ALC
import com.example.neverpidor.util.Constants.MAX_PRICE
import com.example.neverpidor.util.Constants.MAX_VOLUME
import com.example.neverpidor.util.Constants.MIN_PRICE
import com.example.neverpidor.util.Constants.MIN_VOLUME

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
                if (it.isEmpty()) binding.nameLayout.error =
                    binding.root.context.getString(R.string.empty_name_field)
            }
        }
    }
    private val descriptionWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) binding.descriptionTextLayout.error =
                    binding.root.context.getString(R.string.empty_description_field)
            }
        }
    }
    private val typeWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) binding.typeTextLayout.error =
                    binding.root.context.getString(R.string.empty_type_field)
            }
        }
    }

    private val priceWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.isEmpty()) {
                    binding.priceTextLayout.error =
                        binding.root.context.getString(R.string.empty_price_field)
                    return
                }
                if (it.toString().toDouble() > MAX_PRICE) {
                    binding.priceTextLayout.error =
                        binding.root.context.getString(R.string.high_price_field)
                }
                if (it.toString().toDouble() < MIN_PRICE) {
                    binding.priceTextLayout.error =
                        binding.root.context.getString(R.string.low_price_field)
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
                    binding.alcTextLayout.error =
                        binding.root.context.getString(R.string.empty_alc_field)
                    return
                }
                if (it.toString().toDouble() > MAX_ALC) {
                    binding.alcTextLayout.error =
                        binding.root.context.getString(R.string.high_alc_field)
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
                    binding.volumeTextLayout.error =
                        binding.root.context.getString(R.string.empty_volume_field)
                    return
                }
                if (it.toString().toDouble() < MIN_VOLUME) {
                    binding.volumeTextLayout.error =
                        binding.root.context.getString(R.string.low_volume_field)
                }
                if (it.toString().toDouble() > MAX_VOLUME) {
                    binding.volumeTextLayout.error =
                        binding.root.context.getString(R.string.high_volume_field)
                }
            }
        }
    }
}*/
