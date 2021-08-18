package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity

class PlayerDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupMainFragment()
    }

    private fun setupMainFragment() {
        val playerDetailsFragment = PlayerDetailsFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, playerDetailsFragment)
            .commit()
    }
}