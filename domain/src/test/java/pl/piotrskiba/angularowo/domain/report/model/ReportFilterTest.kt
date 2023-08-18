package pl.piotrskiba.angularowo.domain.report.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter.ARCHIVED
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter.NOT_ARCHIVED
import pl.piotrskiba.angularowo.domain.report.model.ReportFilter.OWN

class ReportFilterTest {

    companion object {

        @JvmStatic
        fun parameters() =
            listOf(
                listOf(OWN) to "own",
                listOf(ARCHIVED) to "archived",
                listOf(NOT_ARCHIVED) to "not_archived",
                listOf(OWN, ARCHIVED, NOT_ARCHIVED) to "own,archived,not_archived",
            ).map { Arguments.of(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map domain object to remote`(reportFilters: List<ReportFilter>, expected: String) {
        reportFilters.toRemote() shouldBeEqualTo expected
    }
}
