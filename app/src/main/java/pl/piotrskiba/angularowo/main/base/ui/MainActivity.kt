package pl.piotrskiba.angularowo.main.base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDex
import com.google.android.gms.ads.MobileAds
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.applock.ui.AppLockActivity
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityMainBinding
import pl.piotrskiba.angularowo.login.ui.LoginActivity
import pl.piotrskiba.angularowo.main.base.nav.MainNavigator
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.chat.ui.ChatFragment
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragment
import pl.piotrskiba.angularowo.main.offers.ui.OffersFragment
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragment
import pl.piotrskiba.angularowo.main.punishment.list.ui.PunishmentListFragment
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListContainerFragment
import pl.piotrskiba.angularowo.settings.ui.SettingsActivity
import pl.piotrskiba.angularowo.utils.NotificationUtils
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class), MainNavigator {

    private lateinit var preferenceUtils: PreferenceUtils
    private var waitingForLogin = false

    private lateinit var mainScreenFragment: Fragment
    private lateinit var playerListFragment: Fragment
    private lateinit var chatFragment: Fragment
    private lateinit var punishmentListFragment: Fragment
    private lateinit var offersFragment: Fragment
    private lateinit var reportListContainerFragment: Fragment
    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()

        preferenceUtils = PreferenceUtils(this)

        NotificationUtils(this).createNotificationChannels()
        FirebaseRemoteConfig.getInstance().setDefaultsAsync(R.xml.remote_config_default_values)
        MobileAds.initialize(this)

        if (preferenceUtils.accessToken != null) {
            if (savedInstanceState == null) {
                setupMainFragment()
            }
            populateUi()
        }
    }

    override fun onPause() {
        super.onPause()
        binding.navView.setNavigationItemSelectedListener(null)
    }

    override fun onResume() {
        super.onResume()
        if (!waitingForLogin) {
            // checking uuid to force relogin after upgrading to v3.3
            if (preferenceUtils.accessToken == null || preferenceUtils.uuid == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER)

                waitingForLogin = true
            } else {
                val navHeaderUsernameTextView =
                    binding.navView.getHeaderView(0).findViewById<TextView>(R.id.navheader_username)
                val navHeaderRankTextView =
                    binding.navView.getHeaderView(0).findViewById<TextView>(R.id.navheader_rank)
                navHeaderUsernameTextView.text = preferenceUtils.username
                navHeaderRankTextView.text = preferenceUtils.rankName
            }
        }
        setNavigationItemSelectedListener()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_REGISTER) {
            waitingForLogin = false

            if (resultCode == Constants.RESULT_CODE_SUCCESS) {
                setupMainFragment()
                populateUi()
            }
        }
    }

    override fun displayAppLock() {
        // TODO: don't instantiate main screen fragment nor login activity
        val intent = Intent(this, AppLockActivity::class.java)
        startActivity(intent)
    }

    private fun setupBinding() {
        viewModel.navigator = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MainScreenFragment(), TAG_MAIN_FRAGMENT)
            .commit()
    }

    private fun initializeFragments() {
        mainScreenFragment = supportFragmentManager.findFragmentByTag(TAG_MAIN_FRAGMENT) ?: MainScreenFragment()
        playerListFragment = supportFragmentManager.findFragmentByTag(TAG_PLAYER_LIST_FRAGMENT) ?: PlayerListFragment()
        chatFragment = supportFragmentManager.findFragmentByTag(TAG_CHAT_FRAGMENT) ?: ChatFragment()
        punishmentListFragment = supportFragmentManager.findFragmentByTag(TAG_PUNISHMENT_LIST_FRAGMENT) ?: PunishmentListFragment()
        offersFragment = supportFragmentManager.findFragmentByTag(TAG_OFFERS_FRAGMENT) ?: OffersFragment()
        reportListContainerFragment = supportFragmentManager.findFragmentByTag(TAG_REPORT_LIST_FRAGMENT) ?: ReportListContainerFragment()
    }

    private fun populateUi() {
        initializeFragments()

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        binding.navView.setCheckedItem(R.id.nav_main_screen)
    }

    private fun setNavigationItemSelectedListener() {
        binding.navView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            if (!menuItem.isChecked) {
                when (menuItem.itemId) {
                    R.id.nav_main_screen -> replaceFragment(mainScreenFragment, TAG_MAIN_FRAGMENT)
                    R.id.nav_player_list -> replaceFragment(playerListFragment, TAG_PLAYER_LIST_FRAGMENT)
                    R.id.nav_chat -> replaceFragment(chatFragment, TAG_CHAT_FRAGMENT)
                    R.id.nav_last_punishments -> replaceFragment(punishmentListFragment, TAG_PUNISHMENT_LIST_FRAGMENT)
                    R.id.nav_free_ranks -> replaceFragment(offersFragment, TAG_OFFERS_FRAGMENT)
                    R.id.nav_reports_history -> replaceFragment(reportListContainerFragment, TAG_REPORT_LIST_FRAGMENT)
                    R.id.nav_settings -> {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }

    companion object {
        private const val TAG_MAIN_FRAGMENT = "fragment_main"
        private const val TAG_PLAYER_LIST_FRAGMENT = "fragment_player_list"
        private const val TAG_CHAT_FRAGMENT = "fragment_chat"
        private const val TAG_PUNISHMENT_LIST_FRAGMENT = "fragment_punishment_list"
        private const val TAG_OFFERS_FRAGMENT = "fragment_offers"
        private const val TAG_REPORT_LIST_FRAGMENT = "fragment_report_list"
    }
}
