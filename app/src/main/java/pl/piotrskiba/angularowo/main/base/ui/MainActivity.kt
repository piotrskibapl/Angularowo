package pl.piotrskiba.angularowo.main.base.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.multidex.MultiDex
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.applock.ui.ApplicationLockedActivity
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.interfaces.UnauthorizedResponseListener
import pl.piotrskiba.angularowo.login.ui.LoginActivity
import pl.piotrskiba.angularowo.main.ban.list.ui.BanListFragment
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.chat.ui.ChatFragment
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragment
import pl.piotrskiba.angularowo.main.offers.ui.OffersFragment
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragment
import pl.piotrskiba.angularowo.main.report.history.ui.ReportsHistoryFragment
import pl.piotrskiba.angularowo.main.report.history.ui.ReportsHistoryFragmentContainerFragment
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import pl.piotrskiba.angularowo.network.UnauthorizedInterceptor.Companion.setUnauthorizedListener
import pl.piotrskiba.angularowo.settings.ui.SettingsActivity
import pl.piotrskiba.angularowo.utils.NotificationUtils
import pl.piotrskiba.angularowo.utils.PreferenceUtils
import pl.piotrskiba.angularowo.utils.RankUtils.getRankFromPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivity : BaseActivity(), UnauthorizedResponseListener, RewardedVideoAdListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.drawer_layout)
    lateinit var mDrawerLayout: DrawerLayout

    @BindView(R.id.nav_view)
    lateinit var mNavigationView: NavigationView

    private lateinit var preferenceUtils: PreferenceUtils
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private lateinit var viewModel: MainViewModel
    private var waitingForLogin = false

    private var mainScreenFragment: MainScreenFragment = MainScreenFragment()
    private var playerListFragment: PlayerListFragment = PlayerListFragment()
    private var chatFragment: ChatFragment = ChatFragment()
    private var banListFragment: BanListFragment = BanListFragment()
    private var offersFragment: OffersFragment = OffersFragment()
    private var reportsHistoryFragment: Fragment? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()

        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        preferenceUtils = PreferenceUtils(this)

        NotificationUtils(this).createNotificationChannels()
        setupRemoteConfig()
        MobileAds.initialize(this)

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this

        setUnauthorizedListener(this)

        if (preferenceUtils.accessToken != null) {
            if (savedInstanceState == null)
                setupMainFragment()

            populateUi()
        }
    }

    override fun onPause() {
        super.onPause()

        mNavigationView.setNavigationItemSelectedListener(null)
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
                val navHeaderUsernameTextView = mNavigationView.getHeaderView(0).findViewById<TextView>(R.id.navheader_username)
                val navHeaderRankTextView = mNavigationView.getHeaderView(0).findViewById<TextView>(R.id.navheader_rank)
                navHeaderUsernameTextView.text = preferenceUtils.username
                navHeaderRankTextView.text = preferenceUtils.rankName
            }
        }
        setNavigationItemSelectedListener()
    }

    private fun bindViewModel() {
        viewModel = viewModelFactory.obtainViewModel(this)
    }

    private fun setupRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default_values)
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this) { onRemoteConfigLoaded() }
    }

    private fun onRemoteConfigLoaded() {
        val start = mFirebaseRemoteConfig.getLong(Constants.REMOTE_CONFIG_APP_LOCK_START_TIMESTAMP)

        if (start * 1000 <= System.currentTimeMillis()) {
            val end = mFirebaseRemoteConfig.getLong(Constants.REMOTE_CONFIG_APP_LOCK_END_TIMESTAMP)

            if (end * 1000 > System.currentTimeMillis()) {
                val intent = Intent(this, ApplicationLockedActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupMainFragment() {
        val mainScreenFragment = MainScreenFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mainScreenFragment, TAG_MAIN_FRAGMENT)
                .commit()
    }

    private fun initializeFragments() {
        val frag1: MainScreenFragment? = supportFragmentManager.findFragmentByTag(TAG_MAIN_FRAGMENT) as MainScreenFragment?
        frag1?.run {
            mainScreenFragment = frag1
        }

        val frag2: PlayerListFragment? = supportFragmentManager.findFragmentByTag(
            TAG_PLAYER_LIST_FRAGMENT
        ) as PlayerListFragment?
        frag2?.run {
            playerListFragment = frag2
        }

        val frag3: ChatFragment? = supportFragmentManager.findFragmentByTag(TAG_CHAT_FRAGMENT) as ChatFragment?
        frag3?.run {
            chatFragment = frag3
        }

        val frag4: BanListFragment? = supportFragmentManager.findFragmentByTag(TAG_BAN_LIST_FRAGMENT) as BanListFragment?
        frag4?.run {
            banListFragment = frag4
        }

        val frag5: OffersFragment? = supportFragmentManager.findFragmentByTag(
            TAG_FREE_RANKS_FRAGMENT
        ) as OffersFragment?
        frag5?.run {
            offersFragment = frag5
        }

        reportsHistoryFragment = supportFragmentManager.findFragmentByTag(
            TAG_REPORTS_HISTORY_FRAGMENT
        )

        offersFragment.setRewardedVideoAd(mRewardedVideoAd)

        if (reportsHistoryFragment == null) {
            val rank = getRankFromPreferences(this)
            reportsHistoryFragment = if (rank != null && rank.staff) {
                ReportsHistoryFragmentContainerFragment()
            } else {
                ReportsHistoryFragment.newInstance(false)
            }
        }
    }

    private fun populateUi() {
        initializeFragments()

        setSupportActionBar(mToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        mNavigationView.setCheckedItem(R.id.nav_main_screen)
    }

    private fun setNavigationItemSelectedListener() {
        mNavigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            if (!menuItem.isChecked) {
                when (menuItem.itemId) {
                    R.id.nav_main_screen -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, mainScreenFragment, TAG_MAIN_FRAGMENT)
                                .commit()
                    }
                    R.id.nav_player_list -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, playerListFragment, TAG_PLAYER_LIST_FRAGMENT)
                                .commit()
                    }
                    R.id.nav_chat -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, chatFragment, TAG_CHAT_FRAGMENT)
                                .commit()
                    }
                    R.id.nav_last_bans -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, banListFragment, TAG_BAN_LIST_FRAGMENT)
                                .commit()
                    }
                    R.id.nav_free_ranks -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, offersFragment, TAG_FREE_RANKS_FRAGMENT)
                                .commit()
                    }
                    R.id.nav_reports_history -> {
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, reportsHistoryFragment!!, TAG_REPORTS_HISTORY_FRAGMENT)
                                .commit()
                    }
                    R.id.nav_settings -> {
                        val intent = Intent(this, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            mDrawerLayout.closeDrawers()
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START)
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

    override fun onRewardedVideoAdLoaded() {
        offersFragment.hideLoadingIndicator()
        mRewardedVideoAd.show()
    }

    override fun onRewardedVideoAdOpened() {}
    override fun onRewardedVideoStarted() {}
    override fun onRewardedVideoAdClosed() {}

    override fun onRewarded(rewardItem: RewardItem) {
        val accessToken = preferenceUtils.accessToken
        val context: Context = this

        accessToken?.run {
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.redeemAdOffer(ServerAPIClient.API_KEY, rewardItem.type, accessToken).enqueue(object : Callback<Void?> {
                override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                    if (!isFinishing) {
                        AlertDialog.Builder(context)
                                .setTitle(R.string.ad_offer_redeemed)
                                .setMessage(R.string.ad_offer_redeemed_description)
                                .setPositiveButton(R.string.button_dismiss, null)
                                .show()

                        offersFragment.refreshData()
                    }
                }

                override fun onFailure(call: Call<Void?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    override fun onRewardedVideoAdLeftApplication() {}
    override fun onRewardedVideoAdFailedToLoad(i: Int) {
        if (!isFinishing) {
            offersFragment.hideLoadingIndicator()
            if (i == 3) {
                AlertDialog.Builder(this)
                        .setTitle(R.string.no_ads)
                        .setMessage(R.string.no_ads_description)
                        .setPositiveButton(R.string.button_dismiss, null)
                        .show()
            }
        }
    }

    override fun onRewardedVideoCompleted() {}

    companion object {
        private const val TAG_MAIN_FRAGMENT = "fragment_main"
        private const val TAG_PLAYER_LIST_FRAGMENT = "fragment_player_list"
        private const val TAG_CHAT_FRAGMENT = "fragment_chat"
        private const val TAG_BAN_LIST_FRAGMENT = "fragment_ban_list"
        private const val TAG_FREE_RANKS_FRAGMENT = "fragment_offers"
        private const val TAG_REPORTS_HISTORY_FRAGMENT = "fragment_reports_history"
    }
}