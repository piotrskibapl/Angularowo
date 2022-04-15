package pl.piotrskiba.angularowo.main.report.details.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.domain.report.model.ReportModel

class ReportDetailsActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    private lateinit var report: ReportModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_details)
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
        report = intent.getSerializableExtra(Constants.EXTRA_REPORT) as ReportModel
    }

    private fun setupToolbar() {
        setSupportActionBar(mToolbar)
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
