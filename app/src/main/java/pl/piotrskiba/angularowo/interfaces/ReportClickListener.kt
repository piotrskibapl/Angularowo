package pl.piotrskiba.angularowo.interfaces

import android.view.View
import pl.piotrskiba.angularowo.models.Report

interface ReportClickListener {
    fun onReportClick(view: View, clickedReport: Report)
}