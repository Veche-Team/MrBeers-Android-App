package com.example.neverpidor.presentation.fragments.addbeer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.AddBeerFragmentBinding
import com.example.neverpidor.presentation.fragments.BaseFragment
import com.example.neverpidor.util.ValidationModel
import com.example.neverpidor.util.disableErrorMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBeerFragment : BaseFragment() {

    private var _binding: AddBeerFragmentBinding? = null
    private val binding: AddBeerFragmentBinding
        get() = _binding!!

    private var updateMode = false
    private val viewModel: AddBeerViewModel by viewModels()

    private val args: AddBeerFragmentArgs by navArgs()
    private lateinit var category: MenuCategory

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

        category = viewModel.getItem()
        binding.lottie.playAnimation()
        args.itemId?.let {
            activateUpdateMode()
        }
        if (category == MenuCategory.SnackCategory) {
            supportActionBar?.title = getString(R.string.add_snack)
            binding.volumeTextLayout.isGone = true
            binding.alcTextLayout.isGone = true
        } else {
            supportActionBar?.title = getString(R.string.add_beer)
        }
        addTextChangedListeners()

        binding.saveButton.setOnClickListener {
            if (category == MenuCategory.BeerCategory) {
                val fields = listOf(
                    binding.nameLayout to binding.nameEditText,
                    binding.descriptionTextLayout to binding.descriptionEt,
                    binding.typeTextLayout to binding.typeEt,
                    binding.priceTextLayout to binding.priceEt,
                    binding.alcTextLayout to binding.alcEt,
                    binding.volumeTextLayout to binding.volumeEt
                )
                if (fields.any { it.first.error != null } || fields.any { it.second.text.isNullOrEmpty() }) {
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else {
                    onSaveButton()
                }
                observeBeerResponse()
            } else {
                val fields = listOf(
                    binding.nameLayout to binding.nameEditText,
                    binding.descriptionTextLayout to binding.descriptionEt,
                    binding.typeTextLayout to binding.typeEt,
                    binding.priceTextLayout to binding.priceEt
                )
                if (fields.any { it.first.error != null } || fields.any { it.second.text.isNullOrEmpty() }) {
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                } else {
                    onSaveButton()
                }
                observeSnackResponse()
            }
        }

        val watchers = TextWatchers(binding)
        watchers.setWatchers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onSaveButton() {
        if (category == MenuCategory.BeerCategory) {

            val model = ValidationModel(
                binding.nameEditText.text.toString(),
                binding.descriptionEt.text.toString(),
                binding.typeEt.text.toString(),
                binding.priceEt.text.toString(),
                binding.alcEt.text.toString(),
                binding.volumeEt.text.toString()

            )
            if (updateMode) viewModel.handleInput(model, args.itemId) else viewModel.handleInput(
                model
            )
        } else {
            val model = ValidationModel(
                binding.nameEditText.text.toString(),
                binding.descriptionEt.text.toString(),
                binding.typeEt.text.toString(),
                binding.priceEt.text.toString()
            )
            if (updateMode) viewModel.handleInput(model, args.itemId) else viewModel.handleInput(
                model
            )
        }
    }

    private fun observeBeerResponse() {
        viewModel.beerResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let { beerResponse ->
                Toast.makeText(requireContext(), beerResponse.msg, Toast.LENGTH_SHORT).show()
                if (beerResponse.msg != getString(R.string.check_connection)) navController.popBackStack()
            }
        }
    }

    private fun observeSnackResponse() {
        viewModel.snackResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let { snackResponse ->
                Toast.makeText(requireContext(), snackResponse.msg, Toast.LENGTH_SHORT).show()
                if (snackResponse.msg != getString(R.string.check_connection)) navController.popBackStack()
            }
        }
    }

    private fun activateUpdateMode() {
        updateMode = true

        binding.apply {
            saveButton.text = getString(R.string.update)
            if (category == MenuCategory.BeerCategory) {
                viewModel.getMenuItemById(args.itemId!!)
                viewModel.menuItemLiveData.observe(viewLifecycleOwner) {
                    supportActionBar?.title = getString(R.string.changing_item, it.name)
                    nameEditText.setText(it.name)
                    descriptionEt.setText(it.description)
                    typeEt.setText(it.type)
                    priceEt.setText(it.price.toString())
                    alcEt.setText(it.alcPercentage.toString())
                    volumeEt.setText(it.volume.toString())
                }
            } else {
                viewModel.getMenuItemById(args.itemId!!)
                viewModel.menuItemLiveData.observe(viewLifecycleOwner) {
                    supportActionBar?.title = getString(R.string.changing_item, it.name)
                    nameEditText.setText(it.name)
                    descriptionEt.setText(it.description)
                    typeEt.setText(it.type)
                    priceEt.setText(it.price.toString())
                }
            }
        }
    }

    private fun addTextChangedListeners() {
        binding.apply {
            nameEditText.addTextChangedListener { nameLayout.disableErrorMessage() }
            descriptionEt.addTextChangedListener { descriptionTextLayout.disableErrorMessage() }
            typeEt.addTextChangedListener { typeTextLayout.disableErrorMessage() }
            priceEt.addTextChangedListener { priceTextLayout.disableErrorMessage() }
            if (category == MenuCategory.BeerCategory) {
                alcEt.addTextChangedListener { alcTextLayout.disableErrorMessage() }
                volumeEt.addTextChangedListener { volumeTextLayout.disableErrorMessage() }
            }
        }
    }
}