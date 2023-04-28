package com.example.neverpidor.presentation.fragments.addbeer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.AddBeerFragmentBinding
import com.example.neverpidor.presentation.MainActivity
import com.example.neverpidor.presentation.fragments.addbeer.util.AddUpdateMode
import com.example.neverpidor.presentation.fragments.addbeer.util.AddUpdateState
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
        binding.lottie.playAnimation()
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                setFields()
                addTextChangedListeners()
                observeState()
                observeResponse()
            withContext(Dispatchers.IO) {
                args.itemId?.let {
                    viewModel.getMenuItemById(it)
                }
            }
        }

        binding.saveButton.setOnClickListener {
            viewModel.onButtonClick()
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
            volumeEt.addTextChangedListener { viewModel.onVolumeTextChanged(it.toString()) }
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
                binding.volumeEt.setText(it.volume.toString())
            }
        }
    }

    private fun handleErrorsAndButtonState(state: AddUpdateState) {
        binding.nameLayout.error = state.addUpdateErrorFields.titleError
        binding.descriptionTextLayout.error = state.addUpdateErrorFields.descriptionError
        binding.typeTextLayout.error = state.addUpdateErrorFields.typeError
        binding.priceTextLayout.error = state.addUpdateErrorFields.priceError
        binding.alcTextLayout.error = state.addUpdateErrorFields.alcPercentageError
        binding.volumeTextLayout.error = state.addUpdateErrorFields.volumeError
        binding.saveButton.isEnabled = state.isButtonEnabled
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                category = viewModel.getCategory()
                viewModel.state.collectLatest {
                    val mode = it.mode
                    handleErrorsAndButtonState(it)
                    val actionBar = (activity as MainActivity).supportActionBar
                    if (mode == AddUpdateMode.UPDATE) {

                        binding.apply {
                            saveButton.text = getString(R.string.update)

                            if (category == MenuCategory.BeerCategory) {
                                actionBar?.title =
                                    getString(R.string.changing_item, it.mainItem.name)
                            } else {
                                actionBar?.title =
                                    getString(R.string.changing_item, it.mainItem.name)
                                binding.volumeTextLayout.isGone = true
                                binding.alcTextLayout.isGone = true
                            }
                        }
                    } else {
                        if (category == MenuCategory.SnackCategory) {
                            actionBar?.title = getString(R.string.add_snack)
                            binding.volumeTextLayout.isGone = true
                            binding.alcTextLayout.isGone = true
                        } else {
                            actionBar?.title = getString(R.string.add_beer)
                        }
                    }
                }
            }
        }
    }
}