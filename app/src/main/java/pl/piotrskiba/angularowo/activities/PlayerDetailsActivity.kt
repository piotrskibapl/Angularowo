package pl.piotrskiba.angularowo.activities

import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.models.DetailedPlayer
import pl.piotrskiba.angularowo.models.Player
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import pl.piotrskiba.angularowo.utils.ColorUtils.getColorFromCode
import pl.piotrskiba.angularowo.utils.GlideUtils.getSignatureVersionNumber
import pl.piotrskiba.angularowo.utils.TextUtils.formatPlaytime
import pl.piotrskiba.angularowo.utils.UrlUtils.buildAvatarUrl
import pl.piotrskiba.angularowo.utils.UrlUtils.buildBodyUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerDetailsActivity : AppCompatActivity(), OnRefreshListener {

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player_details)

        ButterKnife.bind(this)

        setSupportActionBar(mToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.player_info)

        if (intent.hasExtra(Constants.EXTRA_PLAYER)) {
            val player = intent.getSerializableExtra(Constants.EXTRA_PLAYER) as Player

            populatePlayer(player)
            loadDetailedPlayerData(player)
        }

        mSwipeRefreshLayout.setOnRefreshListener(this)
    }

    private fun loadDetailedPlayerData(player: Player) {
        mSwipeRefreshLayout.isRefreshing = true

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val accessToken = sharedPreferences.getString(getString(R.string.pref_key_access_token), null)

        accessToken?.run {
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, player.username, accessToken).enqueue(object : Callback<DetailedPlayer?> {
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
        if (!isFinishing) {
            if (player.uuid != null) {
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
            } else {
                mPlayerAvatar.setImageDrawable(resources.getDrawable(R.drawable.default_avatar))
            }

            mPlayerName.text = player.username
            mPlayerRank.text = player.getRank().name

            val color = getColorFromCode(this, player.getRank().colorCode)
            (mPlayerAvatar.parent as ConstraintLayout).setBackgroundColor(color)

            if (player.vanished)
                mPlayerVanishIcon.visibility = View.VISIBLE
            else
                mPlayerVanishIcon.visibility = View.INVISIBLE
        }
    }

    private fun populatePlayer(detailedPlayer: DetailedPlayer) {
        val player = Player(detailedPlayer.username, detailedPlayer.uuid,
                detailedPlayer.getRank().name, detailedPlayer.isVanished)
        populatePlayer(player)

        mPlayerBalanceTextView.text = getString(R.string.balance_format, detailedPlayer.balance.toInt())
        mPlayerTokensTextView.text = detailedPlayer.tokens.toString()
        mPlayerPlayTimeTextView.text = formatPlaytime(this, detailedPlayer.playtime)
    }

    override fun onRefresh() {
        if (intent.hasExtra(Constants.EXTRA_PLAYER)) {
            val player = intent.getSerializableExtra(Constants.EXTRA_PLAYER) as Player
            loadDetailedPlayerData(player)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}