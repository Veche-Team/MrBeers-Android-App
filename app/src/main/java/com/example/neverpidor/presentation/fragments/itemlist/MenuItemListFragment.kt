package com.example.neverpidor.presentation.fragments.itemlist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.example.neverpidor.R
import com.example.neverpidor.data.providers.MenuCategory
import com.example.neverpidor.databinding.FragmentMenuItemListBinding
import com.example.neverpidor.domain.model.DomainBeer
import com.example.neverpidor.domain.model.DomainSnack
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.models.MenuItemEpoxyModel
import com.example.neverpidor.presentation.fragments.BaseFragment
import com.example.neverpidor.presentation.fragments.itemlist.epoxy.MenuItemListEpoxyController
import dagger.hilt.android.AndroidEntryPoint

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

        category = viewModel.getItem()

        binding.fab.setOnClickListener {
            val direction =
                MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment()
            navController.navigate(direction)
        }

        binding.searchEditText.doAfterTextChanged {
            controller.searchInput = it?.toString() ?: ""
        }

        setEpoxyController()

        when (category) {
            MenuCategory.BeerCategory -> {
                supportActionBar?.title = resources.getString(R.string.beer)
                viewModel.getBeers()
                viewModel.beers.observe(viewLifecycleOwner) {
                    controller.beerList = it
                }
                observeBeerDeleteResponse()
            }
            MenuCategory.SnackCategory -> {
                supportActionBar?.title = resources.getString(R.string.snacks)
                viewModel.getSnacks()
                viewModel.snacks.observe(viewLifecycleOwner) {
                    controller.snacks = it
                }
                observeSnackDeleteResponse()
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
                if (it.category == MenuCategory.BeerCategory) {
                    viewModel.faveBeer(it as DomainBeer)
                } else {
                    viewModel.faveSnack(it as DomainSnack)
                }
            }
        )
        controller.isLoading = true
        binding.itemListRv.setControllerAndBuildModels(controller)
    }

    private fun observeBeerDeleteResponse() {
        viewModel.beerResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let { beerResponse ->
                Toast.makeText(requireContext(), beerResponse.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeSnackDeleteResponse() {
        viewModel.snackResponse.observe(viewLifecycleOwner) {
            it.getContent()?.let { snackResponse ->
                Toast.makeText(requireContext(), snackResponse.msg, Toast.LENGTH_SHORT).show()
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
                    if (category == MenuCategory.BeerCategory) {
                        viewModel.deleteBeer(removedItemId)
                    } else {
                        viewModel.deleteSnack(removedItemId)
                    }
                }

                override fun isSwipeEnabledForModel(model: MenuItemEpoxyModel?): Boolean {
                    val connectivityManager =
                        requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork = connectivityManager.activeNetwork ?: return false
                    val capabilities =
                        connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
                    val connected =
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                    if (!connected) return false
                    return super.isSwipeEnabledForModel(model)
                }
            })
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveEpoxyState(controller.getShownState())
    }

    override fun onResume() {
        super.onResume()
        controller.setShownState(viewModel.shownEpoxyState.value ?: emptySet())
    }
}