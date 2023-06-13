package pl.piotrskiba.angularowo.main.report.details.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReportData(
    val id: Int,
    val reporterName: String,
    val reportedName: String,
    val reason: String,
    private val date: Date,
) : Serializable {

    fun date(context: Context): String {
        val dateFormat = SimpleDateFormat(
            context.getString(R.string.report_date_format),
            Locale.getDefault(),
        )
        return dateFormat.format(date)
    }
}

fun ReportModel.toUi() =
    ReportData(
        id = id,
        reporterName = reporterName,
        reportedName = reportedName,
        reason = reason,
        date = date,
    )
