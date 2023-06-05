package com.example.beers_app.presentation.fragments.singleItem

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.beers.R
import com.example.beers.databinding.SingleItemFragmentBinding
import com.example.beers_app.data.common.MenuCategory
import com.example.beers_app.domain.model.DomainItem
import com.example.beers_app.domain.model.User
import com.example.beers_app.presentation.activity.MainActivity
import com.example.beers_app.presentation.fragments.singleItem.epoxy.SingleItemEpoxyController
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

    private var isToastShowing = false
    set(value)  {
        field = value
        if (value) {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(2500)
                field = false
            }
        }
    }

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }

    private fun setFavouriteImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                val image =
                    if (state.isMainItemLiked) R.drawable.ic_baseline_favorite_24 else R.drawable.ic_baseline_favorite_border_24
                binding.favImage.setImageResource(image)
                binding.favImage.setOnClickListener {
                    if (state.user.role is User.Role.NoUser) {
                        showNoUserToast()
                    } else {
                        viewModel.likeOrDislikeMainItem()
                    }
                }
            }
        }
    }

    private fun setCartImage() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                if (state.inCartItem.quantity > 0) {
                    binding.cartImage.isGone = true
                    binding.cartQuantityText.isVisible = true
                    binding.addQuantityButton.isVisible = true
                    binding.removeQuantityButton.isVisible = true
                    binding.cartQuantityText.text = state.inCartItem.quantity.toString()
                } else {
                    binding.cartImage.isVisible = true
                    binding.cartQuantityText.isGone = true
                    binding.addQuantityButton.isGone = true
                    binding.removeQuantityButton.isGone = true
                    binding.cartButton.setOnClickListener {
                        if (binding.cartImage.isVisible) {
                            if (state.user.role is User.Role.NoUser) {
                                showNoUserToast()
                            } else {
                                viewModel.addToCart()
                            }
                        } else binding.cartButton.isClickable = false
                    }
                }
                binding.addQuantityButton.setOnClickListener {
                    viewModel.plusItemInCart()
                }
                binding.removeQuantityButton.setOnClickListener {
                    viewModel.minusItemInCart()
                }
            }
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
            },
            onNoUserClick = {
                showNoUserToast()
            },
            onPlusClick = (viewModel::plusCartItem),
            onMinusClick = (viewModel::minusCartItem),
            onAddToCartClick = (viewModel::addItemToCart)
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
                        binding.discountedPriceText.text = getString(R.string.price, item.discountedPrice)
                        binding.alcoholPercentageText.text =
                            getString(R.string.alcPercentage, item.alcPercentage)
                        binding.weightText.isGone = true
                        if (item.salePercentage != 0) {
                            binding.discountImage.isVisible = true
                            binding.discountText.isVisible = true
                            binding.discountText.text = getString(R.string.sale_percentage, item.salePercentage.toString())
                            binding.price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                            binding.discountedPriceText.isVisible = true
                            binding.discountedPriceText.isVisible = true
                        }
                    } else {
                        binding.weightText.isVisible = true
                        binding.weightText.text = getString(R.string.weight, item.weight)
                        binding.alcoholPercentageText.isGone = true
                    }
                    updateUi(item)
                    controller?.itemList = it.itemsSet
                    controller?.likes = it.likedItems
                    controller?.isUserLogged = it.user.role != User.Role.NoUser
                    controller?.inCartState = it.inCartItemsSet
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
           viewModel.getMenuItemById(itemId)
        }
    }

    private fun loadingState() {
        binding.apply {
            progressBar.isVisible = true
            alcoholPercentageText.isGone = true
            discountedPriceText.isGone = true
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

            price.isVisible = true
            description.isVisible = true
            imageView.isVisible = true
            titleText.isVisible = true
            recyclerView.isVisible = true
            item.image.let {
                Picasso.get().load(it).into(imageView)
            }
            titleText.text = item.name
            description.text = item.description
            price.text = getString(R.string.price, item.price)
        }
    }

    private fun observeLikes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.likes.collectLatest {
                binding.itemLikes.text = it.toString()
            }
        }
    }

    private fun showNoUserToast() {
        if (!isToastShowing) {
            Toast.makeText(requireContext(), requireContext().getString(R.string.need_to_login_toast), Toast.LENGTH_SHORT).show()
            isToastShowing = true
        }
    }
}