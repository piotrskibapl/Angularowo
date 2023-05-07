package pl.piotrskiba.angularowo.main.base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.multidex.MultiDex
import com.google.android.gms.ads.MobileAds
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.applock.ui.AppLockActivity
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityMainBinding
import pl.piotrskiba.angularowo.interfaces.UnauthorizedResponseListener
import pl.piotrskiba.angularowo.login.ui.LoginActivity
import pl.piotrskiba.angularowo.main.base.nav.MainNavigator
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.chat.ui.ChatFragment
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragment
import pl.piotrskiba.angularowo.main.offers.ui.OffersFragment
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragment
import pl.piotrskiba.angularowo.main.punishment.list.ui.PunishmentListFragment
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListContainerFragment
import pl.piotrskiba.angularowo.network.UnauthorizedInterceptor.Companion.setUnauthorizedListener
import pl.piotrskiba.angularowo.settings.ui.SettingsActivity
import pl.piotrskiba.angularowo.utils.NotificationUtils
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class MainActivity : BaseActivity<MainViewModel>(MainViewModel::class), UnauthorizedResponseListener, MainNavigator {

    private lateinit var preferenceUtils: PreferenceUtils
    private var waitingForLogin = false

    private var mainScreenFragment = MainScreenFragment()
    private var playerListFragment = PlayerListFragment()
    private var chatFragment = ChatFragment()
    private var punishmentListFragment = PunishmentListFragment()
    private var offersFragment = OffersFragment()
    private var reportListContainerFragment = ReportListContainerFragment()
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

        setUnauthorizedListener(this)

        if (preferenceUtils.accessToken != null) {
            if (savedInstanceState == null)
                setupMainFragment()

            populateUi()
        }
        viewModel.loadData() // TODO: onFirstCreate should be handled in viewmodel instead
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

    private fun setupBinding() {
        viewModel.navigator = this
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun displayAppLock() {
        val intent = Intent(this, AppLockActivity::class.java)
        startActivity(intent)
    }

    private fun setupMainFragment() {
        val mainScreenFragment = MainScreenFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, mainScreenFragment, TAG_MAIN_FRAGMENT)
            .commit()
    }

    private fun initializeFragments() {
        val frag1: MainScreenFragment? =
            supportFragmentManager.findFragmentByTag(TAG_MAIN_FRAGMENT) as MainScreenFragment?
        frag1?.run {
            mainScreenFragment = this
        }

        val frag2: PlayerListFragment? =
            supportFragmentManager.findFragmentByTag(TAG_PLAYER_LIST_FRAGMENT) as PlayerListFragment?
        frag2?.run {
            playerListFragment = this
        }

        val frag3: ChatFragment? =
            supportFragmentManager.findFragmentByTag(TAG_CHAT_FRAGMENT) as ChatFragment?
        frag3?.run {
            chatFragment = this
        }

        val frag4: PunishmentListFragment? =
            supportFragmentManager.findFragmentByTag(TAG_BAN_LIST_FRAGMENT) as PunishmentListFragment?
        frag4?.run {
            punishmentListFragment = this
        }

        val frag5: OffersFragment? =
            supportFragmentManager.findFragmentByTag(TAG_FREE_RANKS_FRAGMENT) as OffersFragment?
        frag5?.run {
            offersFragment = this
        }

        val frag6: ReportListContainerFragment? =
            supportFragmentManager.findFragmentByTag(TAG_REPORT_LIST_FRAGMENT) as ReportListContainerFragment?
        frag6?.run {
            reportListContainerFragment = this
        }
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
                    R.id.nav_main_screen -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, mainScreenFragment, TAG_MAIN_FRAGMENT)
                            .commit()
                    }
                    R.id.nav_player_list -> {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                playerListFragment,
                                TAG_PLAYER_LIST_FRAGMENT
                            )
                            .commit()
                    }
                    R.id.nav_chat -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, chatFragment, TAG_CHAT_FRAGMENT)
                            .commit()
                    }
                    R.id.nav_last_punishments -> {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                punishmentListFragment,
                                TAG_BAN_LIST_FRAGMENT
                            )
                            .commit()
                    }
                    R.id.nav_free_ranks -> {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                offersFragment,
                                TAG_FREE_RANKS_FRAGMENT
                            )
                            .commit()
                    }
                    R.id.nav_reports_history -> {
                        supportFragmentManager.beginTransaction()
                            .replace(
                                R.id.fragment_container,
                                reportListContainerFragment,
                                TAG_REPORT_LIST_FRAGMENT
                            )
                            .commit()
                    }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onUnauthorizedResponse() {
        if (!waitingForLogin) {
            waitingForLogin = true

            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER)
        }
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

    companion object {
        private const val TAG_MAIN_FRAGMENT = "fragment_main"
        private const val TAG_PLAYER_LIST_FRAGMENT = "fragment_player_list"
        private const val TAG_CHAT_FRAGMENT = "fragment_chat"
        private const val TAG_BAN_LIST_FRAGMENT = "fragment_ban_list"
        private const val TAG_FREE_RANKS_FRAGMENT = "fragment_offers"
        private const val TAG_REPORT_LIST_FRAGMENT = "fragment_report_list"
    }
}
