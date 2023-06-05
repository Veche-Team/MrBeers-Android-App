package com.example.beers_app.presentation.fragments.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.beers.R
import com.example.beers.databinding.FragmentCartBinding
import com.example.beers_app.presentation.fragments.cart.epoxy.CartEpoxyController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding: FragmentCartBinding
        get() = _binding!!

    private val viewModel: CartViewModel by viewModels()
    private lateinit var controller: CartEpoxyController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUsersCart()
        setupController()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }

    private fun setupController() {
        controller = CartEpoxyController(
            onAddClick = {
                viewModel.addItem(it)
            },
            onRemoveClick = {
                viewModel.removeItem(it)
            },
            onToMenuClick = {
                val direction = CartFragmentDirections.actionCartFragmentToListFragment()
                findNavController().navigate(direction)
            },
            onItemClick = {
                val direction = CartFragmentDirections.actionCartFragmentToFragmentSingleItem(it)
                findNavController().navigate(direction)
            }
        )
        binding.recyclerView.setController(controller)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.state.collect {
                    controller.cartItems = it.filter { inCartItem -> inCartItem.title != "" }
                }
            }
            launch {
                viewModel.price.collect {
                    binding.sumText.text = getString(R.string.price, it)
                    binding.orderButton.isEnabled = viewModel.price.value != 0.0
                }
            }
            binding.orderButton.setOnClickListener {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.order_toast),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.onOrderClick()
            }
        }
    }
}