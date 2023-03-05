package com.example.neverpidor.ui.fragments.addbeer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.databinding.AddBeerFragmentBinding
import com.example.neverpidor.model.beer.BeerRequest
import com.example.neverpidor.model.snack.SnackRequest
import com.example.neverpidor.ui.fragments.BaseFragment

class AddBeerFragment : BaseFragment() {

    private var _binding: AddBeerFragmentBinding? = null
    private val binding: AddBeerFragmentBinding
        get() = _binding!!

    private var updateMode = false
    private val viewModel: AddBeerViewModel by viewModels()

    private val args: AddBeerFragmentArgs by navArgs()

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

        args.itemId?.let {
            activateUpdateMode()
        }

        if (args.id == 1) {
            binding.volumeTextLayout.isGone = true
            binding.alcTextLayout.isGone = true
            binding.saveButton.setOnClickListener {
                validateAndSave(args.id)
            }
            observeSnackResponse()

        } else {
            binding.saveButton.setOnClickListener {
                validateAndSave(args.id)
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
        if (id == 0) {
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
        navController.popBackStack()
    }

    private fun observeBeerResponse() {
        viewModel.beerResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let {
                Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                Log.e("Add", "${it.msg} ${it.createdBeverage.name}")
            }

        }
    }

    private fun observeSnackResponse() {
        viewModel.snackResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let {
                Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun activateUpdateMode() {
        updateMode = true

        activity?.actionBar?.title = "Cunt"
        binding.saveButton.text = "Update"
        if (args.id == 0) {
            viewModel.getBeerById(args.itemId!!)
            viewModel.beerLiveData.observe(viewLifecycleOwner) {
                binding.nameEditText.setText(it.data.name)
                binding.descriptionEt.setText(it.data.description)
                binding.typeEt.setText(it.data.type)
                binding.priceEt.setText(it.data.price.toString())
                binding.alcEt.setText(it.data.alcPercentage.toString())
                binding.volumeEt.setText(it.data.volume.toString())
            }
        } else {
            viewModel.getSnackById(args.itemId!!)
            viewModel.snackLiveData.observe(viewLifecycleOwner) {
                binding.nameEditText.setText(it.data.name)
                binding.descriptionEt.setText(it.data.description)
                binding.typeEt.setText(it.data.type)
                binding.priceEt.setText(it.data.price.toString())
            }
        }
    }
}