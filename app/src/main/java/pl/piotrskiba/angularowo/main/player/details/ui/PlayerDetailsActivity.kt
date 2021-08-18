package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData

class PlayerDetailsActivity : BaseActivity() {

    private lateinit var previewedPlayer: PlayerBannerData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadArguments()
        setupMainFragment()
    }

    private fun loadArguments() {
        previewedPlayer = intent.getSerializableExtra(Constants.EXTRA_PREVIEWED_PLAYER) as PlayerBannerData
    }

    private fun setupMainFragment() {
        val playerDetailsFragment = PlayerDetailsFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constants.EXTRA_PREVIEWED_PLAYER, previewedPlayer)
        playerDetailsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, playerDetailsFragment)
            .commit()
    }
}