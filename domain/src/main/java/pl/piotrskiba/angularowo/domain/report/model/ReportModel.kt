package pl.piotrskiba.angularowo.domain.report.model

import java.util.Date

data class ReportModel(
    val id: Int,
    val status: ReportStatus,
    val appreciation: ReportAppreciation,
    val date: Date,
    val reporterName: String,
    val reportedName: String,
    val reason: String,
    val archived: Boolean
)
