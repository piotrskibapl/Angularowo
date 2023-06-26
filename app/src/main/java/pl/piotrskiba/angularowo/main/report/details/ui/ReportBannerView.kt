package pl.piotrskiba.angularowo.main.report.details.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.report.model.ReportAppreciation
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData

@Composable
fun ReportBannerView(banner: ReportBannerData) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.color_minecraft_7))
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.h6,
                text = banner.reportedName,
            )
            Text(
                style = MaterialTheme.typography.body2,
                text = banner.reasonText(LocalContext.current),
            )
            Text(
                style = MaterialTheme.typography.body2,
                text = banner.appreciationText(LocalContext.current),
            )
        }
        Image(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp, 8.dp, 0.dp, 8.dp),
            painter = painterResource(id = banner.appreciationIcon()),
            contentDescription = stringResource(id = R.string.report_status_image_description),
        )
    }
}

@Preview
@Composable
fun ReportBannerViewPreview() {
    ReportBannerView(
        banner = ReportBannerData(
            id = 123,
            reportedName = "Reported",
            reason = "Reason",
            appreciation = ReportAppreciation.TRUE,
        ),
    )
}
