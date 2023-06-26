package pl.piotrskiba.angularowo.main.report.details.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.report.model.ReportAppreciation
import pl.piotrskiba.angularowo.main.report.details.model.ReportData
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData
import java.util.Date

@Composable
fun ReportDetailsView(banner: ReportBannerData, report: ReportData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ReportBannerView(banner)
        Text(
            modifier = Modifier.padding(8.dp, 24.dp, 8.dp, 0.dp),
            style = MaterialTheme.typography.h1,
            text = report.reportedName,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.padding(8.dp, 24.dp, 8.dp, 0.dp),
            style = MaterialTheme.typography.body1,
            text = stringResource(id = R.string.report_details_reason),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp),
            style = MaterialTheme.typography.h2,
            text = report.reason,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.padding(8.dp, 24.dp, 8.dp, 0.dp),
            style = MaterialTheme.typography.body1,
            text = stringResource(id = R.string.report_details_reporter),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp),
            style = MaterialTheme.typography.h2,
            text = report.reporterName,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.padding(8.dp, 24.dp, 8.dp, 0.dp),
            style = MaterialTheme.typography.body1,
            text = stringResource(id = R.string.report_details_date),
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 8.dp),
            style = MaterialTheme.typography.h2,
            text = report.date(LocalContext.current),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun ReportDetailsViewPreview() {
    ReportDetailsView(
        banner = ReportBannerData(
            id = 123,
            reportedName = "Reported",
            reason = "Reason",
            appreciation = ReportAppreciation.TRUE,
        ),
        report = ReportData(
            id = 123,
            reporterName = "Reporter",
            reportedName = "Reported",
            reason = "Reason",
            date = Date(123L),
        ),
    )
}
