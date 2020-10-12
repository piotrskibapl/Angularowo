package pl.piotrskiba.angularowo.activities

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.github.florent37.tutoshowcase.TutoShowcase
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.*
import pl.piotrskiba.angularowo.activities.base.BaseActivity
import pl.piotrskiba.angularowo.database.entity.Friend
import pl.piotrskiba.angularowo.models.DetailedPlayer
import pl.piotrskiba.angularowo.models.Player
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import pl.piotrskiba.angularowo.utils.ColorUtils.getColorFromCode
import pl.piotrskiba.angularowo.utils.GlideUtils.getSignatureVersionNumber
import pl.piotrskiba.angularowo.utils.PreferenceUtils
import pl.piotrskiba.angularowo.utils.TextUtils.formatPlaytime
import pl.piotrskiba.angularowo.utils.UrlUtils.buildAvatarUrl
import pl.piotrskiba.angularowo.utils.UrlUtils.buildBodyUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerDetailsActivity : BaseActivity(), OnRefreshListener {

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.coordinatorLayout)
    lateinit var mCoordinatorLayout: CoordinatorLayout

    @BindView(R.id.iv_player_body)
    lateinit var mPlayerBodyImageView: ImageView

    @BindView(R.id.tv_balance)
    lateinit var mPlayerBalanceTextView: TextView

    @BindView(R.id.tv_tokens)
    lateinit var mPlayerTokensTextView: TextView

    @BindView(R.id.tv_playtime)
    lateinit var mPlayerPlayTimeTextView: TextView

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.iv_player_avatar)
    lateinit var mPlayerAvatar: ImageView

    @BindView(R.id.tv_player_name)
    lateinit var mPlayerName: TextView

    @BindView(R.id.tv_player_rank)
    lateinit var mPlayerRank: TextView

    @BindView(R.id.iv_vanish_status)
    lateinit var mPlayerVanishIcon: ImageView

    @BindView(R.id.iv_heart)
    lateinit var mPlayerHeartIcon: ImageView

    private lateinit var mViewModel: AppViewModel
    private lateinit var mPlayer: DetailedPlayer
    private lateinit var mPreviewedPlayer: Player

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player_details)
        ButterKnife.bind(this)
        mViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.player_info)

        if (intent.hasExtra(Constants.EXTRA_PLAYER)) {
            mPlayer = intent.getSerializableExtra(Constants.EXTRA_PLAYER) as DetailedPlayer
        }

        if (intent.hasExtra(Constants.EXTRA_PREVIEWED_PLAYER)) {
            mPreviewedPlayer = intent.getSerializableExtra(Constants.EXTRA_PREVIEWED_PLAYER) as Player

            populatePlayer(mPreviewedPlayer)
            loadDetailedPlayerData(mPreviewedPlayer)
        }

        mSwipeRefreshLayout.setOnRefreshListener(this)

        mViewModel.allFriends.observe(this, Observer<List<Friend>> {
            invalidateOptionsMenu()

            val friends = mViewModel.allFriends.value
            if (friends != null && friends.contains(Friend(mPreviewedPlayer.uuid))) {
                mPlayerHeartIcon.visibility = View.VISIBLE
            } else {
                mPlayerHeartIcon.visibility = View.GONE
            }
        })
    }

    private fun loadDetailedPlayerData(player: Player) {
        mSwipeRefreshLayout.isRefreshing = true

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val accessToken = sharedPreferences.getString(getString(R.string.pref_key_access_token), null)

        accessToken?.run {
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.getPlayerInfoFromUsername(ServerAPIClient.API_KEY, player.username, accessToken).enqueue(object : Callback<DetailedPlayer?> {
                override fun onResponse(call: Call<DetailedPlayer?>, response: Response<DetailedPlayer?>) {
                    if (response.isSuccessful && response.body() != null) {
                        val detailedPlayer = response.body() as DetailedPlayer
                        populatePlayer(detailedPlayer)
                    }
                    mSwipeRefreshLayout.isRefreshing = false
                }

                override fun onFailure(call: Call<DetailedPlayer?>, t: Throwable) {
                    mSwipeRefreshLayout.isRefreshing = false
                    t.printStackTrace()
                }
            })
        }
    }

    private fun populatePlayer(player: Player) {
        var canPopulate = !isFinishing
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            canPopulate = !isFinishing && !isDestroyed
        }

        if (canPopulate) {
            Glide.with(this)
                    .load(buildAvatarUrl(player.uuid, true))
                    .signature(IntegerVersionSignature(getSignatureVersionNumber(5)))
                    .placeholder(R.drawable.default_body)
                    .into(mPlayerAvatar)

            Glide.with(this)
                    .load(buildBodyUrl(player.uuid, true))
                    .signature(IntegerVersionSignature(getSignatureVersionNumber(5)))
                    .placeholder(R.drawable.default_body)
                    .into(mPlayerBodyImageView)

            mPlayerName.text = player.username
            mPlayerRank.text = player.rank.name

            val color = getColorFromCode(this, player.rank.colorCode)
            (mPlayerAvatar.parent as ConstraintLayout).setBackgroundColor(color)

            if (player.isVanished)
                mPlayerVanishIcon.visibility = View.VISIBLE
            else
                mPlayerVanishIcon.visibility = View.INVISIBLE
        }
    }

    private fun populatePlayer(detailedPlayer: DetailedPlayer) {
        val player = Player(detailedPlayer.uuid, detailedPlayer.username,
                detailedPlayer.rank.name, detailedPlayer.isVanished)
        populatePlayer(player)

        mPlayerBalanceTextView.text = getString(R.string.balance_format, detailedPlayer.balance.toInt())
        mPlayerTokensTextView.text = detailedPlayer.tokens.toString()
        mPlayerPlayTimeTextView.text = formatPlaytime(this, detailedPlayer.playtime)

        showFavoriteShowcase()
    }

    private fun showFavoriteShowcase() {
        if (!PreferenceUtils.hasSeenFavoriteShowcase(this) && findViewById<View>(R.id.nav_favorite) != null) {
            TutoShowcase.from(this)
                    .setContentView(R.layout.showcase_favorite)
                    .on(R.id.nav_favorite)
                    .addCircle()
                    .withBorder()
                    .show()

            PreferenceUtils.setHasSeenFavoriteShowcase(this, true)
        }
    }

    override fun onRefresh() {
        if (intent.hasExtra(Constants.EXTRA_PREVIEWED_PLAYER)) {
            val player = intent.getSerializableExtra(Constants.EXTRA_PREVIEWED_PLAYER) as Player
            loadDetailedPlayerData(player)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val friends = mViewModel.allFriends.value
        if (mPreviewedPlayer.username != PreferenceUtils.getUsername(this)) {
            if (friends != null && friends.contains(Friend(mPreviewedPlayer.uuid))) {
                menu?.findItem(R.id.nav_favorite)?.isVisible = false
                menu?.findItem(R.id.nav_unfavorite)?.isVisible = true
            } else {
                menu?.findItem(R.id.nav_favorite)?.isVisible = true
                menu?.findItem(R.id.nav_unfavorite)?.isVisible = false
            }
        } else {
            menu?.findItem(R.id.nav_favorite)?.isVisible = false
            menu?.findItem(R.id.nav_unfavorite)?.isVisible = false
        }

        menu?.findItem(R.id.nav_mute)?.isVisible = mPlayer.hasPermission(Permissions.MUTE_PLAYERS)
        menu?.findItem(R.id.nav_kick)?.isVisible = mPlayer.hasPermission(Permissions.KICK_PLAYERS)
        menu?.findItem(R.id.nav_warn)?.isVisible = mPlayer.hasPermission(Permissions.WARN_PLAYERS)
        menu?.findItem(R.id.nav_ban)?.isVisible = mPlayer.hasPermission(Permissions.BAN_PLAYERS)

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.player_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
            R.id.nav_favorite -> {
                onFavorite()
            }
            R.id.nav_unfavorite -> {
                onUnfavorite()
            }
            R.id.nav_mute -> {
                onMute()
            }
            R.id.nav_kick -> {
                onKick()
            }
            R.id.nav_warn -> {
                onWarn()
            }
            R.id.nav_ban -> {
                onBan()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onFavorite() {
        mViewModel.insertFriend(Friend(mPreviewedPlayer.uuid))

        snackbar?.dismiss()
        snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.marked_as_favorite), Snackbar.LENGTH_SHORT)
        snackbar?.show()

        AnalyticsUtils().logFavorite(
                PreferenceUtils.getUuid(this) ?: "",
                PreferenceUtils.getUsername(this) ?: "",
                mPreviewedPlayer.uuid,
                mPreviewedPlayer.username
        )
    }

    private fun onUnfavorite() {
        mViewModel.deleteFriend(Friend(mPreviewedPlayer.uuid))

        snackbar?.dismiss()
        snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.unmarked_as_favorite), Snackbar.LENGTH_SHORT)
        snackbar?.show()

        AnalyticsUtils().logUnfavorite(
                PreferenceUtils.getUuid(this) ?: "",
                PreferenceUtils.getUsername(this) ?: "",
                mPreviewedPlayer.uuid,
                mPreviewedPlayer.username
        )
    }

    private fun onMute() {
        throw NotImplementedError()
    }

    private fun onKick() {
        throw NotImplementedError()
    }

    private fun onWarn() {
        throw NotImplementedError()
    }

    private fun onBan() {
        throw NotImplementedError()
    }
}