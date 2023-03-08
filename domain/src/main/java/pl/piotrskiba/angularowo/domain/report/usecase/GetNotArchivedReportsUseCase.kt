package pl.piotrskiba.angularowo.domain.report.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter
import pl.piotrskiba.angularowo.domain.report.repository.ReportRepository
import javax.inject.Inject

class GetNotArchivedReportsUseCase @Inject constructor(
    private val reportRepository: ReportRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute() =
        reportRepository.getReportList(
            preferencesRepository.accessToken!!,
            listOf(ReportFilter.NOT_ARCHIVED)
        )
}
