package pl.piotrskiba.angularowo.fragments

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.*
import pl.piotrskiba.angularowo.activities.BanDetailsActivity
import pl.piotrskiba.angularowo.adapters.BanListAdapter
import pl.piotrskiba.angularowo.adapters.BanListAdapter.BanViewHolder
import pl.piotrskiba.angularowo.interfaces.BanClickListener
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.models.*
import pl.piotrskiba.angularowo.utils.GlideUtils.getSignatureVersionNumber
import pl.piotrskiba.angularowo.utils.PreferenceUtils.getUsername
import pl.piotrskiba.angularowo.utils.RankUtils.getRankFromPreferences
import pl.piotrskiba.angularowo.utils.TextUtils.formatPlaytime
import pl.piotrskiba.angularowo.utils.TextUtils.formatTps
import pl.piotrskiba.angularowo.utils.TextUtils.normalize
import pl.piotrskiba.angularowo.utils.UrlUtils.buildBodyUrl

class MainScreenFragment : Fragment(), BanClickListener, NetworkErrorListener {

    private lateinit var mBanListAdapter: BanListAdapter
    private var loadedServerStatus = false
    private var loadedPlayer = false
    private var loadedActivePlayerBans = false

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.tv_greeting)
    lateinit var mGreetingTextView: TextView

    @BindView(R.id.tv_playercount)
    lateinit var mPlayerCountTextView: TextView

    @BindView(R.id.iv_player_body)
    lateinit var mPlayerBodyImageView: ImageView

    @BindView(R.id.tv_balance)
    lateinit var mPlayerBalanceTextView: TextView

    @BindView(R.id.tv_tokens)
    lateinit var mPlayerTokensTextView: TextView

    @BindView(R.id.tv_playtime)
    lateinit var mPlayerPlayTimeTextView: TextView

    @BindView(R.id.tv_last_bans_title)
    lateinit var mLastBansTitleTextView: TextView

    @BindView(R.id.rv_bans)
    lateinit var mBanList: RecyclerView

    @BindView(R.id.default_layout)
    lateinit var mDefaultLayout: View

    @BindView(R.id.no_internet_layout)
    lateinit var mNoInternetLayout: LinearLayout

    @BindView(R.id.server_error_layout)
    lateinit var mServerErrorLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_screen, container, false)

        ButterKnife.bind(this, view)

        mBanListAdapter = BanListAdapter(context!!, this)
        mBanList.adapter = mBanListAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        mBanList.layoutManager = layoutManager

        mBanList.setHasFixedSize(true)
        mSwipeRefreshLayout.setOnRefreshListener { refreshData() }

        populateUi()

        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.setNetworkErrorListener(this)

        mSwipeRefreshLayout.isRefreshing = true
        seekForServerStatusUpdates()
        seekForPlayerUpdates()
        seekForLastPlayerBans()

        return view
    }

    private fun seekForServerStatusUpdates() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.getServerStatus().observe(viewLifecycleOwner, Observer { serverStatus: ServerStatus? ->
            if (serverStatus != null) {
                loadedServerStatus = true
                showDefaultLayoutIfLoadedAllData()

                val rank = getRankFromPreferences(context!!)
                if (rank != null && rank.isStaff) {
                    val tps = formatTps(serverStatus.tps)
                    mPlayerCountTextView.text = resources.getQuantityString(R.plurals.playercount_tps, serverStatus.playerCount, serverStatus.playerCount, tps)
                } else {
                    mPlayerCountTextView.text = resources.getQuantityString(R.plurals.playercount, serverStatus.playerCount, serverStatus.playerCount)
                }
            }
        })
    }

    private fun seekForPlayerUpdates() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.getPlayer().observe(viewLifecycleOwner, Observer { player: DetailedPlayer? ->
            if (player != null) {
                loadedPlayer = true
                showDefaultLayoutIfLoadedAllData()

                if (player.uuid != null && context != null) {
                    Glide.with(context!!)
                            .load(buildBodyUrl(player.uuid, true))
                            .signature(IntegerVersionSignature(getSignatureVersionNumber(1)))
                            .placeholder(R.drawable.default_body)
                            .into(mPlayerBodyImageView)
                }

                mPlayerBalanceTextView.text = getString(R.string.balance_format, player.balance.toInt())
                mPlayerTokensTextView.text = player.tokens.toString()
                mPlayerPlayTimeTextView.text = formatPlaytime(context!!, player.playtime)

                subscribeToFirebaseRankTopic(player.getRank())
                checkFirebaseNewReportsTopicSubscription(player.getRank())
            }
        })
    }

    private fun seekForLastPlayerBans() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.getActivePlayerBans().observe(viewLifecycleOwner, Observer { banList: BanList? ->
            if (banList != null) {
                loadedActivePlayerBans = true
                showDefaultLayoutIfLoadedAllData()

                mBanListAdapter.setBanList(banList)

                if (banList.banList.isNotEmpty())
                    showLastBans()
            }
        })
    }

    private fun showDefaultLayoutIfLoadedAllData() {
        if (loadedServerStatus && loadedPlayer && loadedActivePlayerBans) {
            showDefaultLayout()

            mSwipeRefreshLayout.isRefreshing = false
        }
    }

    private fun refreshData() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        loadedServerStatus = false
        loadedPlayer = false
        loadedActivePlayerBans = false

        viewModel.refreshServerStatus()
        viewModel.refreshPlayer()
        viewModel.refreshActivePlayerBans()
    }

    override fun onResume() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val username = getUsername(context!!)
        if (username != null) {
            // subscribe to current app version Firebase topic
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_app_version_topic, BuildConfig.VERSION_CODE))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_APP_VERSION_TOPIC_PREFIX + BuildConfig.VERSION_CODE)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (isAdded) {
                                if (task.isSuccessful) {
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_app_version_topic, BuildConfig.VERSION_CODE), true)
                                    editor.apply()
                                }
                            }
                        }
            }

            // subscribe to player's individual Firebase topic
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_player_topic))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + username)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (isAdded) {
                                if (task.isSuccessful) {
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_player_topic), true)
                                    editor.apply()
                                }
                            }
                        }
            }

            // subscribe to new events Firebase topic
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_events))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_EVENT_TOPIC)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (isAdded) {
                                if (task.isSuccessful) {
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_events), true)
                                    editor.apply()
                                }
                            }
                        }
            }
        }
        super.onResume()
    }

    private fun populateUi() {
        showDefaultLayout()

        val username = getUsername(context!!)
        mGreetingTextView.text = getString(R.string.greeting, username)

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.app_name)
    }

    private fun subscribeToFirebaseRankTopic(rank: Rank) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val previousRankName = sharedPreferences.getString(getString(R.string.pref_key_rank), null)

        if (rank.name != previousRankName) {
            if (previousRankName != null) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + normalize(previousRankName))
            }

            FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + normalize(rank.name))

            val editor = sharedPreferences.edit()
            editor.putString(getString(R.string.pref_key_rank), rank.name)
            editor.apply()
        }
    }

    private fun checkFirebaseNewReportsTopicSubscription(rank: Rank?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        if (rank != null && rank.isStaff) {
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                        .addOnCompleteListener {
                            if (isAdded) {
                                val editor = sharedPreferences.edit()
                                editor.putBoolean(getString(R.string.pref_key_subscribed_to_new_reports), true)
                                editor.apply()
                            } else if (activity != null) {
                                val sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)
                                val editor = sharedPreferences1.edit()
                                editor.putBoolean(getString(R.string.pref_key_subscribed_to_new_reports), true)
                                editor.apply()
                            }
                        }
            }
        } else {
            if (sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports)) && sharedPreferences.getBoolean(getString(R.string.pref_key_subscribed_to_new_reports), false)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                        .addOnCompleteListener {
                            if (isAdded) {
                                val editor = sharedPreferences.edit()
                                editor.remove(getString(R.string.pref_key_subscribed_to_new_reports))
                                editor.apply()
                            } else if (activity != null) {
                                val sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(activity!!.applicationContext)
                                val editor = sharedPreferences1.edit()
                                editor.remove(getString(R.string.pref_key_subscribed_to_new_reports))
                                editor.apply()
                            }
                        }
            }
        }
    }

    private fun showDefaultLayout() {
        mDefaultLayout.visibility = View.VISIBLE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
    }

    private fun showNoInternetLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mDefaultLayout.visibility = View.GONE
        mNoInternetLayout.visibility = View.VISIBLE
        mServerErrorLayout.visibility = View.GONE
    }

    private fun showServerErrorLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mDefaultLayout.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.VISIBLE
    }

    private fun showLastBans() {
        mLastBansTitleTextView.visibility = View.VISIBLE
        mBanList.visibility = View.VISIBLE
    }

    private fun hideLastBans() {
        mLastBansTitleTextView.visibility = View.GONE
        mBanList.visibility = View.GONE
    }

    override fun onBanClick(view: View, clickedBan: Ban) {
        val banViewHolder = view.tag as BanViewHolder

        val intent = Intent(context, BanDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_BAN, clickedBan)

        if (banViewHolder.mPlayerAvatar.drawable != null) {
            val avatarBitmap = (banViewHolder.mPlayerAvatar.drawable as BitmapDrawable).bitmap
            intent.putExtra(Constants.EXTRA_BITMAP, avatarBitmap)
        }

        if (activity != null) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, view, getString(R.string.ban_banner_transition_name))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    override fun onNoInternet() {
        showNoInternetLayout()
    }

    override fun onServerError() {
        showServerErrorLayout()
    }
}