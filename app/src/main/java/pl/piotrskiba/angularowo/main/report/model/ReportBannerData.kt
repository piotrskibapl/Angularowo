package pl.piotrskiba.angularowo.main.report.model

import android.content.Context
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.report.model.ReportAppreciation
import pl.piotrskiba.angularowo.domain.report.model.ReportModel

data class ReportBannerData(
    val reportedName: String,
    private val reason: String,
    private val appreciation: ReportAppreciation,
) {

    fun reasonText(context: Context) =
        context.getString(R.string.report_reason, reason)

    fun appreciationText(context: Context) =
        context.getString(
            when (appreciation) {
                ReportAppreciation.TRUE -> R.string.report_status_true
                ReportAppreciation.FALSE -> R.string.report_status_false
                ReportAppreciation.UNCERTAIN -> R.string.report_status_uncertain
                ReportAppreciation.NONE -> R.string.report_status_awaiting
            }
        )

    @DrawableRes
    fun appreciationIcon() =
        when (appreciation) {
            ReportAppreciation.TRUE -> R.drawable.ic_report_accepted
            ReportAppreciation.FALSE -> R.drawable.ic_report_rejected
            ReportAppreciation.UNCERTAIN -> R.drawable.ic_report_uncertain
            ReportAppreciation.NONE -> R.drawable.ic_report_pending
        }
}

fun ReportModel.toReportBannerData() =
    ReportBannerData(
        reportedName,
        reason,
        appreciation
    )

fun List<ReportModel>.toReportBannerDataList() =
    map { it.toReportBannerData() }
