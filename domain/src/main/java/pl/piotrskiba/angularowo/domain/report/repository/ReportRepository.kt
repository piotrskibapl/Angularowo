package pl.piotrskiba.angularowo.domain.report.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter
import pl.piotrskiba.angularowo.domain.report.model.ReportModel

interface ReportRepository {

    fun getReportList(
        filterList: List<ReportFilter>,
    ): Single<List<ReportModel>>
}
