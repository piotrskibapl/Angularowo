package pl.piotrskiba.angularowo.main.report.details.ui

import android.os.Bundle
import android.view.MenuItem
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.serializable
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityReportDetailsBinding
import pl.piotrskiba.angularowo.domain.report.model.ReportModel

class ReportDetailsActivity : BaseActivity() {

    private lateinit var report: ReportModel
    private lateinit var binding: ActivityReportDetailsBinding

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
        binding = ActivityReportDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun loadArguments() {
        report = intent.serializable(Constants.EXTRA_REPORT)!!
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.report_info)
    }

    private fun setupMainFragment() {
        val reportDetailsFragment = ReportDetailsFragment()
        val bundle = Bundle()
        bundle.putSerializable(Constants.EXTRA_REPORT, report)
        reportDetailsFragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, reportDetailsFragment)
            .commit()
    }
}
