package com.example.neverpidor.presentation.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.neverpidor.presentation.MainActivity

abstract class BaseFragment: Fragment() {

   protected val navController: NavController by lazy {
        (activity as MainActivity).navController
    }
     protected val supportActionBar by lazy {
         (activity as MainActivity).supportActionBar
     }
}