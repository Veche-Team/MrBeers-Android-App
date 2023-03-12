package com.example.neverpidor.ui.fragments.addbeer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.R
import com.example.neverpidor.databinding.AddBeerFragmentBinding
import com.example.neverpidor.model.network.beer.BeerRequest
import com.example.neverpidor.model.network.snack.SnackRequest
import com.example.neverpidor.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class AddBeerFragment : BaseFragment() {

    private var _binding: AddBeerFragmentBinding? = null
    private val binding: AddBeerFragmentBinding
        get() = _binding!!

    private var updateMode = false
    private val viewModel: AddBeerViewModel by viewModels()

    private val args: AddBeerFragmentArgs by navArgs()
    private var item by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddBeerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item = viewModel.getItem()
        binding.lottie.playAnimation()
        args.itemId?.let {
            activateUpdateMode()
        }

        if (item == R.string.snacks) {
            supportActionBar?.title = "Добавить закусь"
            binding.volumeTextLayout.isGone = true
            binding.alcTextLayout.isGone = true
            binding.saveButton.setOnClickListener {
                validateAndSave(item)
            }
            observeSnackResponse()

        } else {
            supportActionBar?.title = "Добавить пивас"
            binding.saveButton.setOnClickListener {
                validateAndSave(item)
            }
            observeBeerResponse()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun validateAndSave(id: Int) {
        val name = binding.nameEditText.text.toString()
        if (name.isBlank()) {
            binding.nameLayout.error = "Имя не может быть пустым"
            return
        }
        val description = binding.descriptionEt.text.toString()
        if (description.isBlank()) {
            binding.descriptionTextLayout.error = "Придумай хоть что-нибудь"
            return
        }
        val type = binding.typeEt.text.toString()
        if (type.isBlank()) {
            binding.typeTextLayout.error = "Так не пойдет"
            return
        }
        val priceStr = binding.priceEt.text.toString()
        if (priceStr.isEmpty()) {
            binding.priceTextLayout.error = "Бесплатно только шлюхи в церкви"
            return
        }
        val price = priceStr.toDouble()
        if (price < 50.0) {
            binding.priceTextLayout.error = "Хули тут так мало?"
            return
        }
        if (price > 500.0) {
            binding.priceTextLayout.error = "Чет дорого"
            return
        }
        if (id == R.string.beer) {
            val alcPercentageStr = binding.alcEt.text.toString()
            if (alcPercentageStr.isEmpty()) {
                binding.alcTextLayout.error = "Надо бы добавить хмелю"
                return
            }
            val alcPercentage = alcPercentageStr.toDouble()
            if (alcPercentage > 20.0) {
                binding.alcTextLayout.error = "У нас здесь не кабак"
                return
            }
            val volumeStr = binding.volumeEt.text.toString()
            if (volumeStr.isEmpty()) {
                binding.volumeTextLayout.error = "???"
                return
            }
            val volume = volumeStr.toDouble()
            if (volume < 0.25) {
                binding.volumeTextLayout.error = "Такими темпами не захмелеешь"
            }
            if (volume > 3.00) {
                binding.volumeTextLayout.error = "Бочками пиво не продаем"
            }
            val beerRequest = BeerRequest(alcPercentage, description, name, price, type, volume)
            if (updateMode) {
                viewModel.updateBeer(args.itemId!!, beerRequest)
            } else {
                viewModel.addBeer(beerRequest)
            }
        } else {
            val request = SnackRequest(description, name, price, type)
            if (updateMode) {
                viewModel.updateSnack(args.itemId!!, request)
            } else {
                viewModel.addSnack(request)
            }
        }

    }

    private fun observeBeerResponse() {
        viewModel.beerResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let { beerResponse ->
                Toast.makeText(requireContext(), beerResponse.msg, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }

        }
    }

    private fun observeSnackResponse() {
        viewModel.snackResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let { snackResponse ->
                Toast.makeText(requireContext(), snackResponse.msg, Toast.LENGTH_SHORT).show()
                navController.popBackStack()
            }
        }
    }

    private fun activateUpdateMode() {
        updateMode = true

        binding.saveButton.text = getString(R.string.update)
        if (item == R.string.beer) {
            viewModel.getBeerById(args.itemId!!)
            viewModel.beerLiveData.observe(viewLifecycleOwner) {
                supportActionBar?.title = "Изменяем ${it.name}"
                binding.nameEditText.setText(it.name)
                binding.descriptionEt.setText(it.description)
                binding.typeEt.setText(it.type)
                binding.priceEt.setText(it.price.toString())
                binding.alcEt.setText(it.alcPercentage.toString())
                binding.volumeEt.setText(it.volume.toString())
            }
        } else {
            viewModel.getSnackById(args.itemId!!)
            viewModel.snackLiveData.observe(viewLifecycleOwner) {
                supportActionBar?.title = "Изменяем ${it.name}"
                binding.nameEditText.setText(it.name)
                binding.descriptionEt.setText(it.description)
                binding.typeEt.setText(it.type)
                binding.priceEt.setText(it.price.toString())
            }
        }
    }
}