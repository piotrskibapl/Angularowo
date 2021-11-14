package pl.piotrskiba.angularowo.main.punishment.details

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.OldBaseActivity
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData

class PunishmentDetailsActivity : OldBaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private lateinit var previewedPunishment: PunishmentBannerData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_punishment_details)
        ButterKnife.bind(this)
        loadArguments()
        setupToolbar()
    }

    private fun loadArguments() {
        previewedPunishment = intent.getSerializableExtra(Constants.EXTRA_PUNISHMENT) as PunishmentBannerData
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.punishment_info)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}