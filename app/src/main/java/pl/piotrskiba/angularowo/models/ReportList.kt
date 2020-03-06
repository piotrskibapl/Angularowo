package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName

class ReportList(@field:SerializedName("reports") val reportList: List<Report>)