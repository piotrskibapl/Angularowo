package pl.piotrskiba.angularowo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.Constants;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.models.Report;

public class ReportDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_player_name)
    TextView mPlayerName;

    @BindView(R.id.tv_report_reason)
    TextView mReportReason;

    @BindView(R.id.tv_report_status)
    TextView mReportStatus;

    @BindView(R.id.iv_report_status)
    ImageView mReportStatusImage;

    //

    @BindView(R.id.tv_reported)
    TextView mReportedPlayer;

    @BindView(R.id.tv_reason)
    TextView mFullReportReason;

    @BindView(R.id.tv_reporter)
    TextView mReportReporter;

    @BindView(R.id.tv_date)
    TextView mReportDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_details);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.report_info);
        }

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(Constants.EXTRA_REPORT)){
            Report report = (Report) parentIntent.getSerializableExtra(Constants.EXTRA_REPORT);
            populateReport(report);
        }
    }

    private void populateReport(Report report) {
        // banner
        mPlayerName.setText(report.getReported());
        mReportReason.setText(getString(R.string.report_reason, report.getReason()));
        switch (report.getAppreciation()){
            case Constants.API_RESPONSE_REPORT_ACCEPTED:
                mReportStatus.setText(getString(R.string.report_status_true));
                mReportStatusImage.setImageResource(R.drawable.ic_report_accepted);
                break;
            case Constants.API_RESPONSE_REPORT_REJECTED:
                mReportStatus.setText(getString(R.string.report_status_false));
                mReportStatusImage.setImageResource(R.drawable.ic_report_rejected);
                break;
            case Constants.API_RESPONSE_REPORT_PENDING:
                mReportStatus.setText(getString(R.string.report_status_awaiting));
                mReportStatusImage.setImageResource(R.drawable.ic_report_pending);
                break;
            case Constants.API_RESPONSE_REPORT_UNCERTAIN:
                mReportStatus.setText(getString(R.string.report_status_uncertain));
                mReportStatusImage.setImageResource(R.drawable.ic_report_uncertain);
                break;
        }

        // details
        mReportedPlayer.setText(report.getReported());
        mFullReportReason.setText(report.getReason());
        mReportReporter.setText(report.getReporter());
        mReportDate.setText(report.getDate().replace('/', '.'));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
