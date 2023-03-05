package com.example.neverpidor.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.neverpidor.R
import com.example.neverpidor.databinding.FragmentListBinding
import com.example.neverpidor.ui.fragments.BaseFragment

class ListFragment: BaseFragment() {

    val viewModel: CategoryListViewModel by viewModels()
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
        val controller = ListEpoxyController{
            val direction = ListFragmentDirections.actionListFragmentToMenuItemListFragment(it)
            navController.navigate(direction)
        }
        viewModel.menuCategoriesLiveData.observe(viewLifecycleOwner) {
            controller.list = it

            binding.recyclerView.setControllerAndBuildModels(controller)
        }
        viewModel.getCategories()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}