package pl.piotrskiba.angularowo.main.report.details.ui

import android.os.Bundle
import android.view.MenuItem
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.serializable
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityReportDetailsBinding
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.main.report.details.viewmodel.ReportDetailsViewModel

class ReportDetailsActivity : BaseActivity<ReportDetailsViewModel>(ReportDetailsViewModel::class) {

    private lateinit var binding: ActivityReportDetailsBinding

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
        binding = ActivityReportDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.report_info)
    }
}
