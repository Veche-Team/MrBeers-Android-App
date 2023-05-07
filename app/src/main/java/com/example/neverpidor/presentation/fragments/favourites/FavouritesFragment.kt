package com.example.neverpidor.presentation.fragments.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.neverpidor.databinding.FragmentFavouritesBinding
import com.example.neverpidor.presentation.fragments.favourites.epoxy.FavouritesEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding: FragmentFavouritesBinding
        get() = _binding!!

    private val viewModel: FavouritesViewModel by viewModels()
    private lateinit var controller: FavouritesEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    setupController()
                    observeState()
                }.join()
                viewModel.getLikedItems()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.itemListRv.adapter = null
        _binding = null
    }

    private fun setupController() {
        controller = FavouritesEpoxyController(
            onItemClick = {
                val direction =
                    FavouritesFragmentDirections.actionFavouritesFragmentToFragmentSingleItem(
                        it.UID
                    )
                findNavController().navigate(direction)
            },
            onFavClick = { item ->
                viewModel.likeOrDislike(item.UID)
            },
            onRetry = {
                viewModel.getLikedItems()
            },
            onToMenuClick = {
                val direction =
                    FavouritesFragmentDirections.actionFavouritesFragmentToListFragment()
                findNavController().navigate(direction)
            },
            onPlusClick = { viewModel.plusCartItem(it) },
            onMinusClick = { viewModel.minusCartItem(it) },
            onAddToCartClick = viewModel::addItemToCart
        )
        binding.itemListRv.setController(controller)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                controller.items = it.likedItems
                controller.isError = it.errorState
                controller.inCartState = it.inCartItems
                controller.requestModelBuild()
            }
        }
    }
}