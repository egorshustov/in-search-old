package com.egorshustov.vpoiske

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.egorshustov.vpoiske.R
import com.egorshustov.vpoiske.main.SearchProcessState
import com.egorshustov.vpoiske.main.MainViewModel
import com.egorshustov.vpoiske.util.VPoiskeTheme
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_v_poiske.*
import javax.inject.Inject

class VPoiskeActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel> { viewModelFactory }

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(viewModel.currentThemeId)
        setContentView(R.layout.activity_v_poiske)
        setupNavigation()
        setChangeThemeListener()
        customizeNavigationBar()
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
            viewModel.onNavChangeThemeClicked()
            recreate()
            true
        }
    }

    private fun customizeNavigationBar() {
        if (viewModel.currentThemeId == VPoiskeTheme.LIGHT_THEME.id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                findViewById<View>(android.R.id.content).systemUiVisibility =
                    findViewById<View>(android.R.id.content).systemUiVisibility or
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }
    }

    private fun observeSearchState() {
        viewModel.searchState.observe(this, Observer {
            nav_view.menu.findItem(R.id.searchParamsFragment).isVisible =
                it == SearchProcessState.INACTIVE
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}
