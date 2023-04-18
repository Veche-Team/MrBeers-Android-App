package com.example.neverpidor.presentation.fragments.itemlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.FragmentMenuItemListBinding
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.MenuItemEpoxyModel
import com.example.neverpidor.presentation.fragments.BaseFragment
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.MenuItemListEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MenuItemListFragment : BaseFragment() {

    private var _binding: FragmentMenuItemListBinding? = null
    private val binding: FragmentMenuItemListBinding
        get() = _binding!!

    private val viewModel: MenuItemListViewModel by viewModels()
    private lateinit var category: MenuCategory
    private lateinit var controller: MenuItemListEpoxyController

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
        category = viewModel.getCategory()

        binding.fab.setOnClickListener {
            val direction =
                MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment()
            navController.navigate(direction)
        }
        setEpoxyController()
        binding.searchEditText.doAfterTextChanged {
            controller.searchInput = it?.toString() ?: ""
        }
        when (category) {
            MenuCategory.BeerCategory -> {
                supportActionBar?.title = resources.getString(R.string.beer)
            }
            MenuCategory.SnackCategory -> {
                supportActionBar?.title = resources.getString(R.string.snacks)
            }
        }
        viewModel.getItemList()
        viewModel.getLikes()
        observeResponse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setEpoxyController() {
        controller = MenuItemListEpoxyController(category, onEditClick = {
            val direction =
                MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment(it)
            navController.navigate(direction)
        },
            onItemClick = {
                val direction =
                    MenuItemListFragmentDirections.actionMenuItemListFragmentToFragmentSingleItem(
                        it.UID,
                        it.category.toString()
                    )
                navController.navigate(direction)
            },
            onFavClick = {
                viewModel.likeOrDislike(it.UID)
            }
        )
        controller.isLoading = true
        binding.itemListRv.setControllerAndBuildModels(controller)

        lifecycleScope.launch {
            viewModel.state.collectLatest {
                controller.likes = it.likedItems
                controller.items = it.menuItems
            }
        }
        binding.itemListRv.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                RecyclerView.VERTICAL
            )
        )
        addSwipeToDelete()
    }

    private fun observeResponse() {
        lifecycleScope.launch {
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
                    viewModel.deleteItem(removedItemId)
                }

                override fun isSwipeEnabledForModel(model: MenuItemEpoxyModel?): Boolean {
                    if (!viewModel.isConnected(requireActivity())) return false
                    return super.isSwipeEnabledForModel(model)
                }
            })
    }
}