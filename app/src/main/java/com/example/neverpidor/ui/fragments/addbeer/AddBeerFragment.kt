package com.example.neverpidor.ui.fragments.addbeer

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
import com.example.neverpidor.databinding.AddBeerFragmentBinding
import com.example.neverpidor.ui.fragments.BaseFragment
import com.example.neverpidor.util.InvalidFields
import com.example.neverpidor.util.disableErrorMessage
import com.google.android.material.textfield.TextInputLayout
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
            supportActionBar?.title = getString(R.string.add_snack)
            binding.volumeTextLayout.isGone = true
            binding.alcTextLayout.isGone = true
        } else {
            supportActionBar?.title = getString(R.string.add_beer)
        }
        addTextChangedListeners()

        binding.saveButton.setOnClickListener {
            if (item == R.string.beer) {
                if (updateMode) {
                    viewModel.validateFields(
                        binding.nameEditText.text.toString(),
                        binding.descriptionEt.text.toString(),
                        binding.typeEt.text.toString(),
                        binding.priceEt.text.toString(),
                        binding.alcEt.text.toString(),
                        binding.volumeEt.text.toString(),
                        args.itemId
                    )
                } else {
                    viewModel.validateFields(
                        binding.nameEditText.text.toString(),
                        binding.descriptionEt.text.toString(),
                        binding.typeEt.text.toString(),
                        binding.priceEt.text.toString(),
                        binding.alcEt.text.toString(),
                        binding.volumeEt.text.toString()
                    )
                }
                observeBeerResponse()
            } else {
                if (updateMode) {

                    viewModel.validateFields(
                        binding.nameEditText.text.toString(),
                        binding.descriptionEt.text.toString(),
                        binding.typeEt.text.toString(),
                        binding.priceEt.text.toString(),
                        itemId = args.itemId
                    )
                } else {
                    viewModel.validateFields(
                        binding.nameEditText.text.toString(),
                        binding.descriptionEt.text.toString(),
                        binding.typeEt.text.toString(),
                        binding.priceEt.text.toString()
                    )
                }
                observeSnackResponse()
            }
            viewModel.currentLiveState.observe(viewLifecycleOwner) {
                it?.let {
                    handleErrorFields(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initValidationFields() = mapOf(
        AddBeerViewModel.INPUT_TITLE.first to binding.nameLayout,
        AddBeerViewModel.INPUT_DESCRIPTION.first to binding.descriptionTextLayout,
        AddBeerViewModel.INPUT_TYPE.first to binding.typeTextLayout,
        AddBeerViewModel.EMPTY_PRICE.first to binding.priceTextLayout,
        AddBeerViewModel.LOW_PRICE.first to binding.priceTextLayout,
        AddBeerViewModel.HIGH_PRICE.first to binding.priceTextLayout,
        AddBeerViewModel.EMPTY_ALC.first to binding.alcTextLayout,
        AddBeerViewModel.HIGH_ALC.first to binding.alcTextLayout,
        AddBeerViewModel.EMPTY_VOLUME.first to binding.volumeTextLayout,
        AddBeerViewModel.LOW_VOLUME.first to binding.volumeTextLayout,
        AddBeerViewModel.HIGH_VOLUME.first to binding.volumeTextLayout
    )

    private fun handleErrorFields(data: InvalidFields) {
        val validationFields: Map<String, TextInputLayout> = initValidationFields()
        data.fields.forEach {
            val stringErrorMessage = getString(it.second)
            validationFields[it.first]?.error = stringErrorMessage
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

        binding.apply {
            saveButton.text = getString(R.string.update)
            if (item == R.string.beer) {
                viewModel.getBeerById(args.itemId!!)
                viewModel.beerLiveData.observe(viewLifecycleOwner) {
                    supportActionBar?.title = "Изменяем ${it.name}"
                    nameEditText.setText(it.name)
                    descriptionEt.setText(it.description)
                    typeEt.setText(it.type)
                    priceEt.setText(it.price.toString())
                    alcEt.setText(it.alcPercentage.toString())
                    volumeEt.setText(it.volume.toString())
                }
            } else {
                viewModel.getSnackById(args.itemId!!)
                viewModel.snackLiveData.observe(viewLifecycleOwner) {
                    supportActionBar?.title = "Изменяем ${it.name}"
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
            if (item == R.string.beer) {
                alcEt.addTextChangedListener { alcTextLayout.disableErrorMessage() }
                volumeEt.addTextChangedListener { volumeTextLayout.disableErrorMessage() }
            }
        }
    }
}