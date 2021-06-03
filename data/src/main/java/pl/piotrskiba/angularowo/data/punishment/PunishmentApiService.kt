package pl.piotrskiba.angularowo.data.punishment

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.punishment.model.PunishmentData
import retrofit2.http.GET
import retrofit2.http.Query

interface PunishmentApiService {

    @GET("get_ban_list.php")
    fun getBanList(
        @Query("api_key") apiKey: String,
        @Query("access_token") accessToken: String,
        @Query("username") username: String?,
        @Query("type") type: String,
        @Query("type") filter: String
    ): Single<List<PunishmentData>>
}
