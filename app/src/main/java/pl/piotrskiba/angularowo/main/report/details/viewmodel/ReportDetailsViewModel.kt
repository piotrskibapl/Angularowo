package pl.piotrskiba.angularowo.main.report.details.viewmodel

import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.report.details.model.Report
import pl.piotrskiba.angularowo.main.report.details.model.toUi
import pl.piotrskiba.angularowo.main.report.details.ui.ReportDetailsFragmentArgs
import pl.piotrskiba.angularowo.main.report.model.ReportBanner
import pl.piotrskiba.angularowo.main.report.model.toReportBanner
import javax.inject.Inject

class ReportDetailsViewModel @Inject constructor() : LifecycleViewModel() {

    lateinit var args: ReportDetailsFragmentArgs
    val reportBanner: ReportBanner by lazy { args.report.toReportBanner() }
    val reportDetails: Report by lazy { args.report.toUi() }
}
