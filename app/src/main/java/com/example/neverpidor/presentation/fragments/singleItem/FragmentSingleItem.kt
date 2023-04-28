package com.example.neverpidor.presentation.fragments.singleItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.SingleItemFragmentBinding
import com.example.neverpidor.domain.model.DomainItem
import com.example.neverpidor.presentation.MainActivity
import com.example.neverpidor.presentation.fragments.singleItem.epoxy.SingleItemEpoxyController
import com.example.neverpidor.util.format
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentSingleItem : Fragment() {

    private var _binding: SingleItemFragmentBinding? = null
    private val binding: SingleItemFragmentBinding
        get() = _binding!!
    private val args: FragmentSingleItemArgs by navArgs()
    private val viewModel: SingleItemViewModel by viewModels()
    private var controller: SingleItemEpoxyController? = null

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
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                loadingState()
                setupEpoxyController()
                observeLikes()
                setFavouriteImage()
                setCartImage()
            }.join()
            launch {
                showItems()
            }
        }
        binding.addQuantityButton.setOnClickListener {
            viewModel.plusItemInCart()
        }
        binding.removeQuantityButton.setOnClickListener {
            viewModel.minusItemInCart()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }

    private fun setFavouriteImage() {
        viewLifecycleOwner.lifecycleScope.launch {
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

    private fun setCartImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                if (it.inCartItem.quantity > 0) {
                    binding.cartImage.isGone = true
                    binding.cartQuantityText.isVisible = true
                    binding.addQuantityButton.isVisible = true
                    binding.removeQuantityButton.isVisible = true
                    binding.cartQuantityText.text = it.inCartItem.quantity.toString()
                } else {
                    binding.cartImage.isVisible = true
                    binding.cartQuantityText.isGone = true
                    binding.addQuantityButton.isGone = true
                    binding.removeQuantityButton.isGone = true
                }

            }
        }
        binding.cartImage.setOnClickListener {
            viewModel.addToCart()
        }
    }

    private fun setupEpoxyController() {
        controller = SingleItemEpoxyController(
            onItemClick = {
                val direction =
                    FragmentSingleItemDirections.actionFragmentSingleItemSelf(
                        it.UID
                    )
                findNavController().navigate(
                    direction
                )
            },
            onFavClick = {
                viewModel.likeOrDislikeItemInSet(it.UID)
            }
        )
        binding.recyclerView.setController(controller!!)
    }

    private fun showItems() {
        val itemId = args.itemId
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                    controller?.itemList = it.itemsSet
                    controller?.likes = it.likedItems
                }
            }
        }
        viewModel.getItemsSet()
        viewModel.getMenuItemById(itemId)
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
        (activity as MainActivity).supportActionBar?.title = item.name
        binding.apply {
            progressBar.isGone = true
            alcoholPercentageText.isVisible = true
            volumeText.isVisible = true
            price.isVisible = true
            description.isVisible = true
            imageView.isVisible = true
            titleText.isVisible = true
            recyclerView.isVisible = true
            item.image.let {
                imageView.setImageResource(it)
            }
            titleText.text = item.name
            description.text = item.description
            price.text = getString(R.string.price, item.price.format(2))
        }
    }

    private fun observeLikes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.likes.collectLatest {
                binding.itemLikes.text = it.toString()
            }
        }
    }
}