package com.example.beers_app.presentation.fragments.menu_categories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.beers.databinding.FragmentMenuCategoriesListBinding
import com.example.beers_app.presentation.fragments.menu_categories_list.epoxy.MenuCategoriesListEpoxyController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuCategoriesListFragment : Fragment() {

    private var _binding: FragmentMenuCategoriesListBinding? = null
    private val binding: FragmentMenuCategoriesListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuCategoriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = MenuCategoriesListEpoxyController {
            val direction =
                MenuCategoriesListFragmentDirections.actionListFragmentToMenuItemListFragment(it.toString())
            findNavController().navigate(direction)
        }
        binding.recyclerView.setControllerAndBuildModels(controller)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerView.adapter = null
        _binding = null
    }
}