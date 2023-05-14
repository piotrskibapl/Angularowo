package pl.piotrskiba.angularowo.main.punishment.details.ui

import android.os.Bundle
import android.view.MenuItem
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityPunishmentDetailsBinding
import pl.piotrskiba.angularowo.main.punishment.details.viewmodel.PunishmentDetailsViewModel

class PunishmentDetailsActivity : BaseActivity<PunishmentDetailsViewModel>(PunishmentDetailsViewModel::class) {

    private lateinit var binding: ActivityPunishmentDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            supportFinishAfterTransition()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setupBinding() {
        binding = ActivityPunishmentDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.punishment_info)
    }
}
