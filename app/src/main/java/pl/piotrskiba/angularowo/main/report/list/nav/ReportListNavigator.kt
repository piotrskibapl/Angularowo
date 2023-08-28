package pl.piotrskiba.angularowo.main.report.list.nav

import android.view.View
import pl.piotrskiba.angularowo.main.report.model.ReportBanner

interface ReportListNavigator {

    fun onReportClick(view: View, report: ReportBanner)
}
