package pl.piotrskiba.angularowo.main.base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.multidex.MultiDex
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.install.model.AppUpdateType
import pl.piotrskiba.angularowo.MainNavGraphDirections
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityMainBinding
import pl.piotrskiba.angularowo.main.base.InAppUpdateManager
import pl.piotrskiba.angularowo.main.base.model.NavigationComponent
import pl.piotrskiba.angularowo.main.base.nav.MainNavigator
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class), MainNavigator {

    @Inject
    lateinit var inAppUpdateManager: InAppUpdateManager

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfiguration = AppBarConfiguration(NavigationComponent.topLevelDestinations, drawerLayout)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        findViewById<NavigationView>(R.id.nav_view).setupWithNavController(navController)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            if (navController.currentDestination?.id in NavigationComponent.topLevelDestinations) {
                drawerLayout.open()
            } else {
                onBackPressed()
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in NavigationComponent.topLevelDestinations) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
            if (destination.id in NavigationComponent.noToolbarNavigationIconDestinations) {
                toolbar.navigationIcon = null
            }
        }
        inAppUpdateManager.init(this)
    }

    override fun onPause() {
        super.onPause()
        binding.navView.setNavigationItemSelectedListener(null)
    }

    override fun onResume() {
        super.onResume()
        setNavigationItemSelectedListener()
        inAppUpdateManager.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdateType.IMMEDIATE && resultCode == RESULT_CANCELED) {
            finishAffinity()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        inAppUpdateManager.onDestroy()
    }

    private fun setupBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel.navigator = this
        setContentView(binding.root)
    }

    private fun setNavigationItemSelectedListener() {
        binding.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            menuItem.onNavDestinationSelected(navController)
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    override fun navigateToLogin() {
        navController.navigate(MainNavGraphDirections.globalToLoginFragment())
    }
}
