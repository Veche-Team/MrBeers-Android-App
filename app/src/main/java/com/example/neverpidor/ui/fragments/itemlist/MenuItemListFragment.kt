package com.example.neverpidor.ui.fragments.itemlist

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
import com.example.neverpidor.databinding.FragmentMenuItemListBinding
import com.example.neverpidor.ui.fragments.itemlist.epoxy.models.MenuItemEpoxyModel
import com.example.neverpidor.ui.fragments.BaseFragment
import com.example.neverpidor.ui.fragments.itemlist.epoxy.MenuItemListEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class MenuItemListFragment : BaseFragment() {

    private var _binding: FragmentMenuItemListBinding? = null
    private val binding: FragmentMenuItemListBinding
        get() = _binding!!

    private val viewModel: MenuItemListViewModel by viewModels()
    private var item by Delegates.notNull<Int>()
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

        item = viewModel.getItem()

        binding.fab.setOnClickListener {
            val direction =
                MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment()
            navController.navigate(direction)
        }
         controller = MenuItemListEpoxyController(item, onEditClick = {
            val direction =
                MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment(it)
            navController.navigate(direction)
        },
            onItemClick = {
                val direction = MenuItemListFragmentDirections.actionMenuItemListFragmentToFragmentSingleItem(it.UID, it.itemType)
                navController.navigate(direction)
            }
        )

        binding.searchEditText.doAfterTextChanged {
            controller.searchInput = it?.toString() ?: ""
        }
        when (item) {
            R.string.beer -> {
                supportActionBar?.title = resources.getString( R.string.beer)
                viewModel.getBeers()
                viewModel.beers.observe(viewLifecycleOwner) {
                    controller.beerList = it
                }
                observeBeerDeleteResponse()
            }
            R.string.snacks -> {
                supportActionBar?.title = resources.getString( R.string.snacks)
                viewModel.getSnacks()
                viewModel.snacks.observe(viewLifecycleOwner) {
                    controller.snacks = it
                }
                observeSnackDeleteResponse()
            }
        }

        controller.isLoading = true
        binding.itemListRv.setControllerAndBuildModels(controller)
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

    private fun observeBeerDeleteResponse() {
        viewModel.beerResponse.observe(viewLifecycleOwner) {
            viewModel.getBeers()
            it.getContent()?.let { beerResponse ->
                Toast.makeText(requireContext(), beerResponse.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeSnackDeleteResponse() {
        viewModel.snackResponse.observe(viewLifecycleOwner) {
            viewModel.getSnacks()
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
                    if (item == R.string.beer) {
                        viewModel.deleteBeer(removedItemId)
                    } else {
                        viewModel.deleteSnack(removedItemId)
                    }
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