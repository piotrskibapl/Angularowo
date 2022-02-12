package pl.piotrskiba.angularowo.domain.report.model

enum class ReportFilter {
    OWN,
    ARCHIVED,
    NOT_ARCHIVED,
}

fun List<ReportFilter>.toRemote() =
    joinToString { it.toString().lowercase() }
