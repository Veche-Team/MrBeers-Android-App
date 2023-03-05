package com.example.neverpidor.ui.fragments.itemlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.airbnb.epoxy.EpoxyTouchHelper.SwipeCallbacks
import com.example.neverpidor.R
import com.example.neverpidor.databinding.FragmentMenuItemListBinding
import com.example.neverpidor.ui.fragments.BaseFragment

class MenuItemListFragment : BaseFragment() {

    private var _binding: FragmentMenuItemListBinding? = null
    private val binding: FragmentMenuItemListBinding
        get() = _binding!!
    private val args: MenuItemListFragmentArgs by navArgs()
    private val viewModel: MenuItemListViewModel by viewModels()

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

        binding.fab.setOnClickListener {
            val direction = MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment(args.itemId)
            navController.navigate(direction)
        }
        val controller = MenuItemListEpoxyController(args.itemId) {
            val direction = MenuItemListFragmentDirections.actionMenuItemListFragmentToAddBeerFragment(args.itemId, it)
            navController.navigate(direction)
        }

        when (args.itemId) {
            0 -> {

                viewModel.getBeers()
                viewModel.beers.observe(viewLifecycleOwner) {
                    controller.beerList = it
                }
                observeBeerDeleteResponse()
            }
            else -> {

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
            it.getContent()?.let {
                Toast.makeText(requireContext(), "${it.msg}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeSnackDeleteResponse() {
        viewModel.snackResponse.observe(viewLifecycleOwner) {
            viewModel.getSnacks()
            it.getContent()?.let {
                Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addSwipeToDelete() {
        EpoxyTouchHelper.initSwiping(binding.itemListRv)
            .right()
            .withTarget(MenuItemListEpoxyController.MenuItemEpoxyModel::class.java)
            .andCallbacks(object :
                SwipeCallbacks<MenuItemListEpoxyController.MenuItemEpoxyModel>() {
                override fun onSwipeCompleted(
                    model: MenuItemListEpoxyController.MenuItemEpoxyModel?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val removedItemId = model?.data?.UID ?: return
                    if (args.itemId == 0) {
                        viewModel.deleteBeer(removedItemId)
                    } else {
                        viewModel.deleteSnack(removedItemId)
                    }
                }
            })
    }

}