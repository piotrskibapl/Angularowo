package pl.piotrskiba.angularowo.data.report

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.report.model.ReportRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApiService {

    @GET("get_report_list.php")
    fun getReportList(
        @Query("filter") filter: String,
    ): Single<List<ReportRemote>>
}
