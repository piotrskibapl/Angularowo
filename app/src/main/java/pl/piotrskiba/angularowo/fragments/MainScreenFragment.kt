package pl.piotrskiba.angularowo.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
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
import pl.piotrskiba.angularowo.utils.PreferenceUtils
import pl.piotrskiba.angularowo.utils.RankUtils.getRankFromPreferences
import pl.piotrskiba.angularowo.utils.TextUtils
import pl.piotrskiba.angularowo.utils.TextUtils.formatPlaytime
import pl.piotrskiba.angularowo.utils.TextUtils.formatTps
import pl.piotrskiba.angularowo.utils.TextUtils.normalize
import pl.piotrskiba.angularowo.utils.UrlUtils.buildBodyUrl

class MainScreenFragment : Fragment(), BanClickListener, NetworkErrorListener {

    private lateinit var mViewModel: AppViewModel
    private lateinit var mBanListAdapter: BanListAdapter
    private var loadedServerStatus = false
    private var loadedPlayer = false
    private var loadedActivePlayerBans = false

    private var motd: Motd? = null

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.tv_motd)
    lateinit var mMotdTextView: TextView

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main_screen, container, false)

        ButterKnife.bind(this, view)

        mBanListAdapter = BanListAdapter(requireContext(), this)
        mBanList.adapter = mBanListAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        mBanList.layoutManager = layoutManager

        mBanList.setHasFixedSize(true)
        mSwipeRefreshLayout.setOnRefreshListener { refreshData() }

        mMotdTextView.setOnClickListener { onMotdClick() }

        populateUi()

        mViewModel.setNetworkErrorListener(this)

        mSwipeRefreshLayout.isRefreshing = true
        seekForServerStatusUpdates()
        seekForPlayerUpdates()
        seekForLastPlayerBans()

        return view
    }

    private fun seekForServerStatusUpdates() {
        mViewModel.getServerStatus().observe(viewLifecycleOwner, Observer { serverStatus: ServerStatus? ->
            if (serverStatus != null) {
                loadedServerStatus = true
                showDefaultLayoutIfLoadedAllData()

                val rank = getRankFromPreferences(requireContext())
                if (rank != null && rank.staff) {
                    val tps = formatTps(serverStatus.tps)
                    mPlayerCountTextView.text = resources.getQuantityString(R.plurals.playercount_tps, serverStatus.playerCount, serverStatus.playerCount, tps)
                } else {
                    mPlayerCountTextView.text = resources.getQuantityString(R.plurals.playercount, serverStatus.playerCount, serverStatus.playerCount)
                }

                motd = serverStatus.motd
                if (motd != null && context != null) {
                    mMotdTextView.visibility = View.VISIBLE
                    mMotdTextView.text = TextUtils.replaceQualifiers(requireContext(), motd!!.text)
                    mMotdTextView.setTextColor(Color.parseColor(motd!!.textColor))
                    mMotdTextView.setBackgroundColor(Color.parseColor(motd!!.backgroundColor))

                    if (motd?.url != null) {
                        mMotdTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right, 0)

                        for (drawable in mMotdTextView.compoundDrawables) {
                            drawable?.colorFilter = PorterDuffColorFilter(Color.parseColor(motd!!.textColor), PorterDuff.Mode.SRC_IN)
                        }
                    }
                    else {
                        mMotdTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }
                }
                else {
                    mMotdTextView.visibility = View.GONE
                }
            }
        })
    }

    private fun seekForPlayerUpdates() {
        mViewModel.getPlayer().observe(viewLifecycleOwner, Observer { player: DetailedPlayer? ->
            if (player != null) {
                loadedPlayer = true
                showDefaultLayoutIfLoadedAllData()

                if (context != null) {
                    if (PreferenceUtils.getUsername(requireContext()) != player.username) {
                        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                        val editor = sharedPreferences.edit()
                        editor.putString(getString(R.string.pref_key_nickname), player.username)
                        editor.commit()
                    }
                }

                mPlayerBalanceTextView.text = getString(R.string.balance_format, player.balance.toInt())
                mPlayerTokensTextView.text = player.tokens.toString()
                mPlayerPlayTimeTextView.text = formatPlaytime(requireContext(), player.playtime)

                subscribeToFirebaseRankTopic(player.rank)
                checkFirebaseNewReportsTopicSubscription(player.rank)
            }
        })
    }

    private fun seekForLastPlayerBans() {
        mViewModel.getActivePlayerBans().observe(viewLifecycleOwner, Observer { banList: BanList? ->
            if (banList != null) {
                loadedActivePlayerBans = true
                showDefaultLayoutIfLoadedAllData()

                mBanListAdapter.setBanList(banList)

                if (banList.banList.isNotEmpty())
                    showLastBans()
                else
                    hideLastBans()
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
        loadedServerStatus = false
        loadedPlayer = false
        loadedActivePlayerBans = false

        mViewModel.refreshServerStatus()
        mViewModel.refreshPlayer()
        mViewModel.refreshActivePlayerBans()
    }

    override fun onResume() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val uuid = PreferenceUtils.getUuid(requireContext())
        if (uuid != null) {
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
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_uuid_topic))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + uuid)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (isAdded) {
                                if (task.isSuccessful) {
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_uuid_topic), true)
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

            // subscribe to private messages Firebase topic
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_private_messages))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (isAdded) {
                                if (task.isSuccessful) {
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_private_messages), true)
                                    editor.apply()
                                }
                            }
                        }
            }

            // subscribe to account incidents Firebase topic
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_account_incidents))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
                        .addOnCompleteListener { task: Task<Void?> ->
                            if (isAdded) {
                                if (task.isSuccessful) {
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_account_incidents), true)
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

        val username = PreferenceUtils.getUsername(requireContext())
        mGreetingTextView.text = getString(R.string.greeting, username)

        val uuid = PreferenceUtils.getUuid(requireContext())
        if (uuid != null) {
            Glide.with(requireContext())
                    .load(buildBodyUrl(uuid, true))
                    .signature(IntegerVersionSignature(getSignatureVersionNumber(1)))
                    .placeholder(R.drawable.default_body)
                    .into(mPlayerBodyImageView)
        }

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
        if (rank != null && rank.staff) {
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                        .addOnCompleteListener {
                            if (isAdded) {
                                val editor = sharedPreferences.edit()
                                editor.putBoolean(getString(R.string.pref_key_subscribed_to_new_reports), true)
                                editor.apply()
                            } else if (activity != null) {
                                val sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
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
                                val sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)
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
        if (!mSwipeRefreshLayout.isRefreshing) {
            val banViewHolder = view.tag as BanViewHolder

            val intent = Intent(context, BanDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_BAN, clickedBan)

            if (banViewHolder.mPlayerAvatar.drawable != null) {
                val avatarBitmap = (banViewHolder.mPlayerAvatar.drawable as BitmapDrawable).bitmap
                intent.putExtra(Constants.EXTRA_BITMAP, avatarBitmap)
            }

            if (activity != null) {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), view, getString(R.string.ban_banner_transition_name))
                startActivity(intent, options.toBundle())
            } else {
                startActivity(intent)
            }
        }
    }

    private fun onMotdClick() {
        if (motd?.url != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(motd!!.url))
            if (context != null && intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }
    }

    override fun onNoInternet() {
        showNoInternetLayout()
    }

    override fun onServerError() {
        showServerErrorLayout()
    }
}