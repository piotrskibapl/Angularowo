package pl.piotrskiba.angularowo.domain.report.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter
import pl.piotrskiba.angularowo.domain.report.repository.ReportRepository
import javax.inject.Inject

class GetOwnedReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute() =
        preferencesRepository.accessToken()
            .toSingle()
            .flatMap { accessToken ->
                reportRepository.getReportList(accessToken, listOf(ReportFilter.OWN))
            }
}
