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
import kotlin.properties.Delegates

@AndroidEntryPoint
class FragmentSingleItem : BaseFragment() {

    private var _binding: SingleItemFragmentBinding? = null
    private val binding: SingleItemFragmentBinding
        get() = _binding!!
    private val args: FragmentSingleItemArgs by navArgs()
    private val viewModel: SingleItemViewModel by viewModels()
    private lateinit var controller: SingleItemEpoxyController
    private var item: DomainItem? = null
    private var itemId by Delegates.notNull<String>()

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

        itemId = args.itemId
        val itemType = args.itemType

        controller = SingleItemEpoxyController {
            val direction =
                FragmentSingleItemDirections.actionFragmentSingleItemSelf(it.UID, it.itemType)
            navController.navigate(direction)
        }
        binding.recyclerView.setController(controller)
        if (item == null) {
            loadingState()
        }
        if (itemType == "beer") {
            showBeer()
        } else {
           showSnacks()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showBeer() {
        viewModel.getBeerById(itemId)
        viewModel.beerLiveData.observe(viewLifecycleOwner) {
            item = it
            binding.volumeText.text = getString(R.string.volume, item!!.volume.toString())
            binding.alcoholPercentageText.text =
                getString(R.string.alcPercentage, item!!.alcPercentage.toString())
            updateUi(item!!)
        }
        viewModel.getSnackSet()
        viewModel.snackListLiveData.observe(viewLifecycleOwner) {
            controller.itemList = it
        }
    }

    private fun showSnacks() {
        viewModel.getSnackById(itemId)
        viewModel.snackLiveData.observe(viewLifecycleOwner) {
            item = it
            binding.volumeText.isGone = true
            binding.alcoholPercentageText.isGone = true
            updateUi(item!!)
        }
        viewModel.getBeerSet()
        viewModel.beerListLiveData.observe(viewLifecycleOwner) {
            controller.itemList = it
        }
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

