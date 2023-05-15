package pl.piotrskiba.angularowo.main.report.details.viewmodel

import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.base.extensions.serializable
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.main.report.details.model.ReportData
import pl.piotrskiba.angularowo.main.report.details.model.toUi
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData
import pl.piotrskiba.angularowo.main.report.model.toReportBannerData
import javax.inject.Inject

class ReportDetailsViewModel @Inject constructor() : LifecycleViewModel() {
    val reportBanner: ReportBannerData by lazy { intent.serializable<ReportModel>(Constants.EXTRA_REPORT)!!.toReportBannerData() }
    val reportDetails: ReportData by lazy { intent.serializable<ReportModel>(Constants.EXTRA_REPORT)!!.toUi() }
}
