package pl.piotrskiba.angularowo.main.report.list.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData
import pl.piotrskiba.angularowo.main.report.model.toReportBannerDataList

data class ReportListTabData(
    val banners: List<ReportBannerData>,
    val isOthersTab: Boolean,
) {

    val emptyTextVisible = banners.isEmpty()

    fun emptyText(context: Context) =
        context.getString(
            if (isOthersTab) {
                R.string.error_no_reports_admin
            } else {
                R.string.error_no_reports
            }
        )
}

fun List<ReportModel>.toUi(isOthersTab: Boolean) =
    ReportListTabData(
        banners = toReportBannerDataList(),
        isOthersTab = isOthersTab,
    )
