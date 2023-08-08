package pl.piotrskiba.angularowo.data.report.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.data.report.ReportApiService
import pl.piotrskiba.angularowo.data.report.model.ReportRemote
import pl.piotrskiba.angularowo.data.report.model.toDomain
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.domain.report.model.toRemote

class ReportRepositoryImplTest {

    val reportApi: ReportApiService = mockk()
    val tested = ReportRepositoryImpl(reportApi)

    @BeforeEach
    fun setup() {
        mockkStatic(
            List<ReportFilter>::toRemote,
            List<ReportRemote>::toDomain,
        )
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD return report list`() {
        val filterListRemote = "filter"
        val filterList: List<ReportFilter> = mockk {
            every { toRemote() } returns filterListRemote
        }
        val reportList: List<ReportModel> = mockk()
        val reportListRemote: List<ReportRemote> = mockk {
            every { toDomain() } returns reportList
        }
        every { reportApi.getReportList(filterListRemote) } returns Single.just(reportListRemote)

        val result = tested.getReportList(filterList).test()

        result.assertValue(reportList)
    }
}
