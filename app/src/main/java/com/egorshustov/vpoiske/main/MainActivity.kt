package com.egorshustov.vpoiske.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.egorshustov.vpoiske.R
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(viewModel.state.currentTheme.themeId)
        setContentView(R.layout.activity_main)
        setupNavigation()
        setChangeThemeListener()
        observeSearchState()
    }

    private fun setupNavigation() {
        setSupportActionBar(findViewById(R.id.toolbar))
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer_layout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        nav_view.setupWithNavController(navController)
    }

    private fun setChangeThemeListener() {
        val navChangeTheme = nav_view.menu.findItem(R.id.nav_change_theme)
        navChangeTheme.setOnMenuItemClickListener {
            viewModel.state.currentTheme = viewModel.state.currentTheme.getNext()
            recreate()
            true
        }
    }

    private fun observeSearchState() {
        viewModel.searchState.observe(this, Observer {
            nav_view.menu.findItem(R.id.newSearchFragment).isVisible =
                it == MainViewModel.SearchState.INACTIVE
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
