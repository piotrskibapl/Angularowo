package pl.piotrskiba.angularowo.data.report.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.data.report.ReportApiService
import pl.piotrskiba.angularowo.data.report.model.toDomain
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.domain.report.model.toRemote
import pl.piotrskiba.angularowo.domain.report.repository.ReportRepository

class ReportRepositoryImpl(
    private val reportApi: ReportApiService,
) : ReportRepository {

    override fun getReportList(
        accessToken: String,
        filterList: List<ReportFilter>
    ): Single<List<ReportModel>> =
        reportApi.getReportList(
            BuildConfig.API_KEY,
            accessToken,
            filterList.toRemote()
        ).map { it.toDomain() }
}
