package pl.piotrskiba.angularowo.domain.report.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.domain.report.repository.ReportRepository

class GetOwnedReportsUseCaseTest {

    val reportRepository: ReportRepository = mockk()
    val tested = GetOwnedReportsUseCase(reportRepository)

    @Test
    fun `SHOULD get report list`() {
        val reportModels: List<ReportModel> = mockk()
        every { reportRepository.getReportList(listOf(ReportFilter.OWN)) } returns Single.just(reportModels)

        val result = tested.execute().test()

        result.assertValue(reportModels)
    }
}
