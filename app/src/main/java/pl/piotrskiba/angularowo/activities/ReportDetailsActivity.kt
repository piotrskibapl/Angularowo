package pl.piotrskiba.angularowo.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.activities.base.BaseActivity
import pl.piotrskiba.angularowo.models.Report

class ReportDetailsActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.tv_player_name)
    lateinit var mPlayerName: TextView

    @BindView(R.id.tv_report_reason)
    lateinit var mReportReason: TextView

    @BindView(R.id.tv_report_status)
    lateinit var mReportStatus: TextView

    @BindView(R.id.iv_report_status)
    lateinit var mReportStatusImage: ImageView

    @BindView(R.id.tv_reported)
    lateinit var mReportedPlayer: TextView

    @BindView(R.id.tv_reason)
    lateinit var mFullReportReason: TextView

    @BindView(R.id.tv_reporter)
    lateinit var mReportReporter: TextView

    @BindView(R.id.tv_date)
    lateinit var mReportDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_report_details)

        ButterKnife.bind(this)

        setSupportActionBar(mToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.report_info)

        if (intent.hasExtra(Constants.EXTRA_REPORT)) {
            val report = intent.getSerializableExtra(Constants.EXTRA_REPORT) as Report
            populateReport(report)
        }
    }

    private fun populateReport(report: Report) {
        // banner
        mPlayerName.text = report.reported
        mReportReason.text = getString(R.string.report_reason, report.reason)

        when (report.appreciation) {
            Constants.API_RESPONSE_REPORT_ACCEPTED -> {
                mReportStatus.text = getString(R.string.report_status_true)
                mReportStatusImage.setImageResource(R.drawable.ic_report_accepted)
            }
            Constants.API_RESPONSE_REPORT_REJECTED -> {
                mReportStatus.text = getString(R.string.report_status_false)
                mReportStatusImage.setImageResource(R.drawable.ic_report_rejected)
            }
            Constants.API_RESPONSE_REPORT_PENDING -> {
                mReportStatus.text = getString(R.string.report_status_awaiting)
                mReportStatusImage.setImageResource(R.drawable.ic_report_pending)
            }
            Constants.API_RESPONSE_REPORT_UNCERTAIN -> {
                mReportStatus.text = getString(R.string.report_status_uncertain)
                mReportStatusImage.setImageResource(R.drawable.ic_report_uncertain)
            }
        }

        // details
        mReportedPlayer.text = report.reported
        mFullReportReason.text = report.reason.replace("&8", "")
        mReportReporter.text = report.reporter
        mReportDate.text = report.date.replace('/', '.')
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}