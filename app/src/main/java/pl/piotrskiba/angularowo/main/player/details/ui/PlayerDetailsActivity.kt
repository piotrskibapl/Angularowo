package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import android.view.MenuItem
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.serializable
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityPlayerDetailsBinding
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.main.player.details.viewmodel.PlayerDetailsViewModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData

class PlayerDetailsActivity : BaseActivity<PlayerDetailsViewModel>(PlayerDetailsViewModel::class) {

    private lateinit var player: DetailedPlayerModel
    private lateinit var previewedPlayer: PlayerBannerData
    private lateinit var binding: ActivityPlayerDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
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

    private fun setupBinding() {
        binding = ActivityPlayerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun loadArguments() {
        player = intent.serializable(Constants.EXTRA_PLAYER)!!
        previewedPlayer = intent.serializable(Constants.EXTRA_PREVIEWED_PLAYER)!!
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
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
