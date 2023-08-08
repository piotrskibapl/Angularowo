package pl.piotrskiba.angularowo.data.report.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.report.model.ReportAppreciation
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.domain.report.model.ReportStatus
import java.util.Date

class ReportRemoteTest {

    companion object {

        @JvmStatic
        fun parameters() =
            listOf(
                Triple("Waiting", "True", ReportStatus.WAITING to ReportAppreciation.TRUE),
                Triple("Waiting", "False", ReportStatus.WAITING to ReportAppreciation.FALSE),
                Triple("Waiting", "Uncertain", ReportStatus.WAITING to ReportAppreciation.UNCERTAIN),
                Triple("Waiting", "None", ReportStatus.WAITING to ReportAppreciation.NONE),
                Triple("Done", "True", ReportStatus.DONE to ReportAppreciation.TRUE),
                Triple("Done", "False", ReportStatus.DONE to ReportAppreciation.FALSE),
                Triple("Done", "Uncertain", ReportStatus.DONE to ReportAppreciation.UNCERTAIN),
                Triple("Done", "None", ReportStatus.DONE to ReportAppreciation.NONE),
                Triple("Done by XYZ", "True", ReportStatus.DONE to ReportAppreciation.TRUE),
                Triple("Done by XYZ", "False", ReportStatus.DONE to ReportAppreciation.FALSE),
                Triple("Done by XYZ", "Uncertain", ReportStatus.DONE to ReportAppreciation.UNCERTAIN),
                Triple("Done by XYZ", "None", ReportStatus.DONE to ReportAppreciation.NONE),
            ).map { Arguments.of(it.first, it.second, it.third) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map list of remote objects to domain WHEN status is {0} AND appreciation is {1}`(
        statusRemote: String,
        appreciationRemote: String,
        statusAndAppreciation: Pair<ReportStatus, ReportAppreciation>,
    ) {
        val reportRemote = ReportRemote(
            id = 123,
            status = statusRemote,
            appreciation = appreciationRemote,
            date = 123L,
            reporter = "reporter",
            reported = "reported",
            reason = "reason",
            archived = false,
        )

        listOf(reportRemote).toDomain() shouldBeEqualTo listOf(
            ReportModel(
                id = 123,
                status = statusAndAppreciation.first,
                appreciation = statusAndAppreciation.second,
                date = Date(123L),
                reporterName = "reporter",
                reportedName = "reported",
                reason = "reason",
                archived = false,
            ),
        )
    }
}
