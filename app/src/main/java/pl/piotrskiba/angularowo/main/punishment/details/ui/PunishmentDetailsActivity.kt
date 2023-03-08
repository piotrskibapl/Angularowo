package pl.piotrskiba.angularowo.main.punishment.details.ui

import android.os.Bundle
import android.view.MenuItem
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityPunishmentDetailsBinding
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData

class PunishmentDetailsActivity : BaseActivity() {

    private lateinit var previewedPunishment: DetailedPunishmentData
    private lateinit var binding: ActivityPunishmentDetailsBinding

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
        binding = ActivityPunishmentDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun loadArguments() {
        previewedPunishment =
            intent.getSerializableExtra(Constants.EXTRA_PUNISHMENT) as DetailedPunishmentData
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.punishment_info)
    }

    private fun setupMainFragment() {
        val punishmentDetailsFragment = PunishmentDetailsFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constants.EXTRA_PUNISHMENT, previewedPunishment)
        punishmentDetailsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, punishmentDetailsFragment)
            .commit()
    }
}
