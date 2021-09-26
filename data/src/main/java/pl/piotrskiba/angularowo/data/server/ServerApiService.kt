package pl.piotrskiba.angularowo.data.server

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.server.model.ServerStatusRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerApiService {

    @GET("get_server_status.php")
    fun getServerStatus(
        @Query("api_key") apiKey: String,
        @Query("access_token") access_token: String
    ): Single<ServerStatusRemote>
}
