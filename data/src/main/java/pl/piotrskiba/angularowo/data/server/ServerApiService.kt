package pl.piotrskiba.angularowo.data.server

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.server.model.ServerStatusRemote
import retrofit2.http.GET

interface ServerApiService {

    @GET("get_server_status.php")
    fun getServerStatus(): Single<ServerStatusRemote>
}
