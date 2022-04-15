package pl.piotrskiba.angularowo.data.report.model

import pl.piotrskiba.angularowo.domain.report.model.ReportAppreciation
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.domain.report.model.ReportStatus
import java.util.Date

data class ReportRemote(
    val id: Int,
    val status: String,
    val appreciation: String,
    val date: Long,
    val reporter: String,
    val reported: String,
    val reason: String,
    val archived: Boolean
)

fun List<ReportRemote>.toDomain() = map { it.toDomain() }

private fun ReportRemote.toDomain() = ReportModel(
    id,
    status.toReportStatus(),
    appreciation.toReportAppreciation(),
    Date(date),
    reporter,
    reported,
    reason,
    archived
)

private fun String.toReportStatus() =
    when {
        this == "Waiting" -> ReportStatus.WAITING
        startsWith("Done") -> ReportStatus.DONE
        else -> throw IllegalArgumentException("\"$this\" can't be mapped to ReportStatus")
    }

private fun String.toReportAppreciation() =
    when (this) {
        "True" -> ReportAppreciation.TRUE
        "False" -> ReportAppreciation.FALSE
        "Uncertain" -> ReportAppreciation.UNCERTAIN
        "None" -> ReportAppreciation.NONE
        else -> throw IllegalArgumentException("\"$this\" can't be mapped to ReportAppreciation")
    }
