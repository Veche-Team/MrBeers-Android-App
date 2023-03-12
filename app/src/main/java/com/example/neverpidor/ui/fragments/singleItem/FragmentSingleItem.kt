package com.example.neverpidor.ui.fragments.singleItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.databinding.SingleItemFragmentBinding
import com.example.neverpidor.model.domain.DomainItem
import com.example.neverpidor.ui.fragments.BaseFragment
import com.example.neverpidor.ui.fragments.singleItem.epoxy.SingleItemEpoxyController

class FragmentSingleItem: BaseFragment() {

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
            val direction = FragmentSingleItemDirections.actionFragmentSingleItemSelf(it.UID, it.itemType)
            navController.navigate(direction)
        }
        if (item == null) {
            loadingState()
        }
        if (itemType == "beer") {
            viewModel.getBeerById(itemId)
            viewModel.beerLiveData.observe(viewLifecycleOwner) {
                item = it
                binding.volumeText.text = "${(item!!).volume} Л"
                binding.alcoholPercentageText.text = "Алк.:${(item!!).alcPercentage}"
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
        binding.progressBar.isVisible = true
        binding.alcoholPercentageText.isGone = true
        binding.volumeText.isGone = true
        binding.price.isGone = true
        binding.description.isGone = true
        binding.imageView.isGone = true
        binding.titleText.isGone = true
        binding.recyclerView.isGone = true
    }

    private fun updateUi(item: DomainItem) {
        supportActionBar?.title = item.name
        binding.progressBar.isGone = true
        binding.alcoholPercentageText.isVisible = true
        binding.volumeText.isVisible = true
        binding.price.isVisible = true
        binding.description.isVisible = true
        binding.imageView.isVisible = true
        binding.titleText.isVisible = true
        binding.recyclerView.isVisible = true
        item.image?.let {
            binding.imageView.setImageResource(it)
        }
        binding.titleText.text = item.name
        binding.description.text = item.description
        binding.price.text = "${item.price} P."
    }

}

