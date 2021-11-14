package pl.piotrskiba.angularowo.main.punishment.details.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.OldBaseActivity
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData

class PunishmentDetailsActivity : OldBaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private lateinit var previewedPunishment: DetailedPunishmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_punishment_details)
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
        previewedPunishment = intent.getSerializableExtra(Constants.EXTRA_PUNISHMENT) as DetailedPunishmentData
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
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
