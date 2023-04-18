package com.example.neverpidor.presentation.fragments.singleItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.SingleItemFragmentBinding
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.presentation.fragments.BaseFragment
import com.example.neverpidor.presentation.fragments.singleItem.epoxy.SingleItemEpoxyController
import com.example.neverpidor.util.format
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentSingleItem : BaseFragment() {

    private var _binding: SingleItemFragmentBinding? = null
    private val binding: SingleItemFragmentBinding
        get() = _binding!!
    private val args: FragmentSingleItemArgs by navArgs()
    private val viewModel: SingleItemViewModel by viewModels()
    private lateinit var controller: SingleItemEpoxyController

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
        loadingState()
        setupEpoxyController()
        showItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setImage() {
        lifecycleScope.launch {
            viewModel.state.collect {
                val image =
                    if (it.isMainItemLiked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                binding.favImage.setImageResource(image)
            }
        }
        binding.favImage.setOnClickListener {
            viewModel.likeOrDislikeMainItem()
        }
    }

    private fun setupEpoxyController() {
        controller = SingleItemEpoxyController(
            onItemClick = {
                val direction =
                    FragmentSingleItemDirections.actionFragmentSingleItemSelf(
                        it.UID,
                        it.category.toString()
                    )
                navController.navigate(direction)
            },
            onFavClick = {
                viewModel.likeOrDislikeItemInSet(it.UID)
            }
        )
        binding.recyclerView.setController(controller)
    }

    private fun showItems() {
        val itemId = args.itemId
        viewModel.getMenuItemById(itemId)
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                val item = it.mainItem
                if (it.mainItem.category == MenuCategory.BeerCategory) {
                    binding.volumeText.text = getString(R.string.volume, item.volume.format(2))
                    binding.alcoholPercentageText.text =
                        getString(R.string.alcPercentage, item.alcPercentage.format(1))
                } else {
                    binding.volumeText.isGone = true
                    binding.alcoholPercentageText.isGone = true
                }
                updateUi(item)
                setImage()
                controller.itemList = it.itemsSet
                controller.likes = it.likedItems
            }
        }
        viewModel.getItemsSet()
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
            price.text = getString(R.string.price, item.price.format(2))
        }
    }
}