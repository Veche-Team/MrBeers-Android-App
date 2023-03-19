package com.example.neverpidor.ui.fragments.singleItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.R
import com.example.neverpidor.databinding.SingleItemFragmentBinding
import com.example.neverpidor.model.domain.DomainItem
import com.example.neverpidor.ui.fragments.BaseFragment
import com.example.neverpidor.ui.fragments.singleItem.epoxy.SingleItemEpoxyController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSingleItem : BaseFragment() {

    private var _binding: SingleItemFragmentBinding? = null
    private val binding: SingleItemFragmentBinding
        get() = _binding!!
    private val args: FragmentSingleItemArgs by navArgs()
    private val viewModel: SingleItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SingleItemFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemId = args.itemId
        val itemType = args.itemType
        var item: DomainItem? = null
        val controller = SingleItemEpoxyController {
            val direction =
                FragmentSingleItemDirections.actionFragmentSingleItemSelf(it.UID, it.itemType)
            navController.navigate(direction)
        }
        if (item == null) {
            loadingState()
        }
        if (itemType == "beer") {
            viewModel.getBeerById(itemId)
            viewModel.beerLiveData.observe(viewLifecycleOwner) {
                item = it
                binding.volumeText.text = getString(R.string.volume, item!!.volume.toString())
                binding.alcoholPercentageText.text =
                    getString(R.string.alcPercentage, item!!.alcPercentage.toString())
                updateUi(item!!)
            }
            binding.recyclerView.setController(controller)
            viewModel.getSnackSet()
            viewModel.snackListLiveData.observe(viewLifecycleOwner) {
                controller.itemList = it
            }
        } else {
            viewModel.getSnackById(itemId)
            viewModel.snackLiveData.observe(viewLifecycleOwner) {
                item = it
                binding.volumeText.isGone = true
                binding.alcoholPercentageText.isGone = true
                updateUi(item!!)
            }
            binding.recyclerView.setController(controller)
            viewModel.getBeerSet()
            viewModel.beerListLiveData.observe(viewLifecycleOwner) {
                controller.itemList = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingState() {
        binding.apply {
            progressBar.isVisible = true
            alcoholPercentageText.isGone = true
            volumeText.isGone = true
            price.isGone = true
            description.isGone = true
            imageView.isGone = true
            titleText.isGone = true
            recyclerView.isGone = true
        }
    }

    private fun updateUi(item: DomainItem) {
        supportActionBar?.title = item.name
        binding.apply {
            progressBar.isGone = true
            alcoholPercentageText.isVisible = true
            volumeText.isVisible = true
            price.isVisible = true
            description.isVisible = true
            imageView.isVisible = true
            titleText.isVisible = true
            recyclerView.isVisible = true
            item.image?.let {
                imageView.setImageResource(it)
            }
            titleText.text = item.name
            description.text = item.description
            price.text = getString(R.string.price, item.price.toString())
        }
    }
}

