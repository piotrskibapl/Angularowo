package pl.piotrskiba.angularowo.main.report.details.viewmodel

import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.report.details.model.ReportData
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData
import javax.inject.Inject

class ReportDetailsViewModel @Inject constructor() : LifecycleViewModel() {
    lateinit var reportBanner: ReportBannerData
    lateinit var reportDetails: ReportData
}
