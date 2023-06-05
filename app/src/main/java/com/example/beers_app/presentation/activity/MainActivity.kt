package com.example.beers_app.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.beers.R
import com.example.beers.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel: MainViewModel by viewModels()
    private lateinit var exitButton: ImageView
    private lateinit var header: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupNavViewHeader()
        observeNavViewState()
        this.resources.configuration.orientation
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.menuFragment,
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.profileFragment,
                R.id.favouritesFragment,
                R.id.cartFragment
            ), binding.drawerLayout
        )
        binding.navView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.loginFragment -> {
                    navController.navigate(R.id.loginFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.registerFragment -> {
                    navController.navigate(R.id.registerFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.menuFragment -> {
                    navController.navigate(R.id.menuFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.favouritesFragment -> {
                    navController.navigate(R.id.favouritesFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.cartFragment -> {
                    navController.navigate(R.id.cartFragment)
                    binding.drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupNavViewHeader() {
        header = binding.navView.getHeaderView(0)
        exitButton = header.findViewById(R.id.exitIcon)
        exitButton.setOnClickListener {
            viewModel.logout()
            val fragment = navController.currentDestination?.id ?: 0
            if (fragment != R.id.menuFragment) {
                navController.navigate(R.id.menuFragment)
            }
        }
    }

    private fun observeNavViewState() {
        lifecycleScope.launch {
            viewModel.state.collectLatest {
                header.findViewById<TextView>(R.id.userName).text = it.user.name
                binding.navView.menu.findItem(R.id.profileFragment).isEnabled = it.user.name != ""
                binding.navView.menu.findItem(R.id.loginFragment).isEnabled = it.user.name == ""
                binding.navView.menu.findItem(R.id.registerFragment).isEnabled = it.user.name == ""
                binding.navView.menu.findItem(R.id.favouritesFragment).isEnabled = it.user.name != ""
                binding.navView.menu.findItem(R.id.cartFragment).isEnabled = it.user.name != ""
                binding.navView.menu.findItem(R.id.cartFragment).title =
                    "Корзина" + if (it.inCartQuantity > 0) "(${it.inCartQuantity})" else ""
                exitButton.isGone = it.user.name == ""
                header.findViewById<TextView>(R.id.exitText).isGone = it.user.name == ""
            }
        }
    }
}