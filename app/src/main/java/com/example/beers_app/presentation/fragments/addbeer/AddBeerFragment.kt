package com.example.beers_app.presentation.fragments.addbeer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.beers.R
import com.example.beers.databinding.AddBeerFragmentBinding
import com.example.beers_app.data.common.MenuCategory
import com.example.beers_app.presentation.activity.MainActivity
import com.example.beers_app.presentation.fragments.addbeer.util.AddUpdateMode
import com.example.beers_app.presentation.fragments.addbeer.util.AddUpdateState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class AddBeerFragment : Fragment() {

    private var _binding: AddBeerFragmentBinding? = null
    private val binding: AddBeerFragmentBinding
        get() = _binding!!

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
        val category = MenuCategory.toMenuCategory(args.category)
        binding.lottie.playAnimation()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            launch {
                setFields()
                addTextChangedListeners()
                observeState(category)
                observeResponse()
            }.join()
            withContext(Dispatchers.IO) {
                args.itemId?.let {
                    viewModel.getMenuItemById(it)
                } ?: kotlin.run {
                    viewModel.setCategory(category)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.response.collect { response ->
                    Toast.makeText(requireContext(), response, Toast.LENGTH_SHORT).show()
                    if (response != getString(R.string.check_connection)) findNavController().popBackStack()
                }
            }
        }
    }

    private fun addTextChangedListeners() {
        binding.apply {
            nameEditText.addTextChangedListener { viewModel.onTitleTextChanged(it.toString()) }
            descriptionEt.addTextChangedListener { viewModel.onDescriptionTextChanged(it.toString()) }
            typeEt.addTextChangedListener { viewModel.onTypeTextChanged(it.toString()) }
            priceEt.addTextChangedListener { viewModel.onPriceTextChanged(it.toString()) }
            alcEt.addTextChangedListener { viewModel.onAlcPercentageTextChanged(it.toString()) }
            saleEt.addTextChangedListener { viewModel.onSalePercentageTextChanged(it.toString()) }
            weightEt.addTextChangedListener { viewModel.onWeightTextChanged(it.toString()) }
        }
    }

    private fun setFields() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.itemState.collectLatest {
                binding.nameEditText.setText(it.name)
                binding.descriptionEt.setText(it.description)
                binding.typeEt.setText(it.type)
                binding.priceEt.setText(it.price.toString())
                binding.alcEt.setText(it.alcPercentage.toString())
                binding.saleEt.setText(it.salePercentage.toString())
                binding.weightEt.setText(it.weight.toString())
            }
        }
    }

    private fun handleErrorsAndButtonState(state: AddUpdateState) {
        binding.apply {
            nameLayout.error = state.addUpdateErrorFields.titleError
            descriptionTextLayout.error = state.addUpdateErrorFields.descriptionError
            typeTextLayout.error = state.addUpdateErrorFields.typeError
            priceTextLayout.error = state.addUpdateErrorFields.priceError
            alcTextLayout.error = state.addUpdateErrorFields.alcPercentageError
            weightTextLayout.error = state.addUpdateErrorFields.weightError
            saleTextLayout.error = state.addUpdateErrorFields.salePercentageError
            saveButton.isEnabled = state.isButtonEnabled
        }
    }

    private fun observeState(category: MenuCategory) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val mode = state.mode
                    binding.saveButton.setOnClickListener {
                        viewModel.onButtonClick(category)
                    }
                    handleErrorsAndButtonState(state)
                    val actionBar = (activity as MainActivity).supportActionBar
                    if (mode == AddUpdateMode.UPDATE) {
                        updateMode(category, actionBar, state.mainItem.name)
                    } else {
                        addMode(category, actionBar)
                    }
                }
            }
        }
    }

    private fun updateMode(category: MenuCategory, actionBar: ActionBar?, name: String) {
        binding.apply {
            saveButton.text = getString(R.string.update)
            if (category == MenuCategory.BeerCategory) {
                actionBar?.title =
                    getString(R.string.changing_item, name)
                binding.weightTextLayout.isGone = true
            } else {
                actionBar?.title =
                    getString(R.string.changing_item, name)
                binding.alcTextLayout.isGone = true
                binding.saleTextLayout.isGone = true
            }
        }
    }

    private fun addMode(category: MenuCategory, actionBar: ActionBar?) {
        if (category == MenuCategory.SnackCategory) {
            actionBar?.title = getString(R.string.add_snack)
            binding.alcTextLayout.isGone = true
            binding.saleTextLayout.isGone = true
            val constraintLayout = binding.constraintLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                R.id.priceTextLayout,
                ConstraintSet.END,
                R.id.weightTextLayout,
                ConstraintSet.START
            )
            constraintSet.applyTo(constraintLayout)
        } else {
            actionBar?.title = getString(R.string.add_beer)
            binding.weightTextLayout.isGone = true
        }
    }
}