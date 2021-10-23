package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData

class PlayerDetailsActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private lateinit var player: DetailedPlayerModel
    private lateinit var previewedPlayer: PlayerBannerData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        loadArguments()
        setupToolbar()
        setupMainFragment()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            supportFinishAfterTransition()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun loadArguments() {
        player = intent.getSerializableExtra(Constants.EXTRA_PLAYER) as DetailedPlayerModel
        previewedPlayer = intent.getSerializableExtra(Constants.EXTRA_PREVIEWED_PLAYER) as PlayerBannerData
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.player_info)
    }

    private fun setupMainFragment() {
        val playerDetailsFragment = PlayerDetailsFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constants.EXTRA_PLAYER, player)
        bundle.putSerializable(Constants.EXTRA_PREVIEWED_PLAYER, previewedPlayer)
        playerDetailsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, playerDetailsFragment)
            .commit()
    }
}