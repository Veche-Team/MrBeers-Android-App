package com.example.neverpidor.ui.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.neverpidor.ui.MainActivity

abstract class BaseFragment: Fragment() {

    val navController: NavController by lazy {
        (activity as MainActivity).navController
    }
}