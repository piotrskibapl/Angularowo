package pl.piotrskiba.angularowo.main.report.list.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.main.report.model.ReportBanner
import pl.piotrskiba.angularowo.main.report.model.toReportBanners

data class ReportListTabData(
    val banners: List<ReportBanner>,
    val isOthersTab: Boolean,
) {

    val emptyTextVisible = banners.isEmpty()

    fun emptyText(context: Context) =
        context.getString(
            if (isOthersTab) {
                R.string.error_no_reports_admin
            } else {
                R.string.error_no_reports
            },
        )
}

fun List<ReportModel>.toUi(isOthersTab: Boolean) =
    ReportListTabData(
        banners = toReportBanners(),
        isOthersTab = isOthersTab,
    )
