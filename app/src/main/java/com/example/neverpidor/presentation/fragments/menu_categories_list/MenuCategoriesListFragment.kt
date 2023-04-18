package com.example.neverpidor.presentation.fragments.menu_categories_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.neverpidor.databinding.FragmentListBinding
import com.example.neverpidor.presentation.fragments.BaseFragment
import com.example.neverpidor.presentation.fragments.menu_categories_list.epoxy.MenuCategoriesListEpoxyController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuCategoriesListFragment : BaseFragment() {

    private val viewModel: MenuCategoriseListViewModel by viewModels()
    private var _binding: FragmentListBinding? = null
    private val binding: FragmentListBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = MenuCategoriesListEpoxyController {
            val direction =
                MenuCategoriesListFragmentDirections.actionListFragmentToMenuItemListFragment()
            viewModel.setItem(it)
            navController.navigate(direction)
        }
        binding.recyclerView.setControllerAndBuildModels(controller)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}