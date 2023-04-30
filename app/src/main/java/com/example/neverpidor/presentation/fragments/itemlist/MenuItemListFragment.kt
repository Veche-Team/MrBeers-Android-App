package com.example.neverpidor.presentation.fragments.itemlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.FragmentMenuItemListBinding
import com.example.neverpidor.domain.model.User
import com.example.neverpidor.presentation.MainActivity
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.MenuItemEpoxyModel
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.MenuItemListEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MenuItemListFragment : Fragment() {

    private var _binding: FragmentMenuItemListBinding? = null
    private val binding: FragmentMenuItemListBinding
        get() = _binding!!

    private val viewModel: MenuItemListViewModel by viewModels()

    private lateinit var controller: MenuItemListEpoxyController
    private val args: MenuItemListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuItemListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val category = MenuCategory.toMenuCategory(args.category)
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    showActionBarTitle(category)
                    setEpoxyController(category)
                    observeErrorState()
                    observeUserRole()
                }.join()
                launch(Dispatchers.Main) {
                    observeResponse()
                }
                withContext(Dispatchers.IO) {
                    viewModel.getItemList(category)
                }
            }
        }

        binding.fab.setOnClickListener {
            val direction =
                MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment(category.toString())
            findNavController().navigate(direction)
        }

        viewModel.getLikes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.itemListRv.adapter = null
        _binding = null
    }

    private fun setEpoxyController(category: MenuCategory) {

        controller = MenuItemListEpoxyController(
            category,
            onEditClick = {
                val direction =
                    MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment(
                        category = category.toString(),
                        it
                    )
                findNavController().navigate(direction)
            },
            onItemClick = {
                val direction =
                    MenuItemListFragmentDirections.actionMenuItemListFragmentToFragmentSingleItem(
                        it.UID
                    )
                findNavController().navigate(direction)
            },
            onFavClick = (viewModel::likeOrDislike),
            onRetry = { viewModel.getItemList(category) },
            onPlusClick = (viewModel::plusCartItem),
            onMinusClick = (viewModel::minusCartItem),
            onAddToCartClick = (viewModel::addItemToCart),
            onNoUserClick = {
                Toast.makeText(requireContext(), "Необходимо зайти в аккаунт", Toast.LENGTH_SHORT)
                    .show()
            }
        )
        binding.itemListRv.setControllerAndBuildModels(controller)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                controller.likes = it.likedItems
                controller.items = it.menuItems
                controller.inCartState = it.inCartItems
                controller.userRole = it.user.role
                controller.searchInput = binding.searchEditText.text.toString()
            }
        }
        binding.searchEditText.doAfterTextChanged {
            controller.searchInput = it?.toString() ?: ""
        }

        binding.itemListRv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
        addSwipeToDelete()
    }

    private suspend fun observeResponse() {
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.response.collectLatest {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addSwipeToDelete() {
        EpoxyTouchHelper.initSwiping(binding.itemListRv)
            .right()
            .withTarget(MenuItemEpoxyModel::class.java)
            .andCallbacks(object :
                SwipeCallbacks<MenuItemEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: MenuItemEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val removedItemId = model?.domainItem?.UID ?: return
                    val category = model.domainItem.category
                    viewModel.deleteItem(removedItemId, category)
                }

                override fun isSwipeEnabledForModel(model: MenuItemEpoxyModel?): Boolean {
                    if (!viewModel.isConnected(requireActivity())) return false
                    return super.isSwipeEnabledForModel(model)
                }
            })
    }

    private fun showActionBarTitle(category: MenuCategory) {
        when (category) {
            MenuCategory.BeerCategory -> {
                (activity as MainActivity).supportActionBar?.title =
                    resources.getString(R.string.beer)
            }
            MenuCategory.SnackCategory -> {
                (activity as MainActivity).supportActionBar?.title =
                    resources.getString(R.string.snacks)
            }
        }
    }

    private fun observeErrorState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                if (it.errorState) {
                    controller.isError = true
                }
            }
        }
    }

    private fun observeUserRole() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when (it.user.role) {
                    User.Role.Admin -> binding.fab.isVisible = true
                    else -> binding.fab.isGone = true
                }
            }
        }
    }
}