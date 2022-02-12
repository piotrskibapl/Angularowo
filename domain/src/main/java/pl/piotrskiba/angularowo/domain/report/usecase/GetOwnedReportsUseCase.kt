package pl.piotrskiba.angularowo.domain.report.usecase

import pl.piotrskiba.angularowo.domain.report.model.ReportFilter
import pl.piotrskiba.angularowo.domain.report.repository.ReportRepository
import javax.inject.Inject

class GetOwnedReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
) {

    fun execute(accessToken: String) =
        reportRepository.getReportList(
            accessToken,
            listOf(ReportFilter.OWN)
        )
}
