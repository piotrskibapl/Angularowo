package pl.piotrskiba.angularowo.data.login

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.login.model.AccessTokenRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface LoginApiService {

    @GET("register_device.php")
    fun registerDevice(
        @Query("api_key") apiKey: String,
        @Query("user_code") userCode: String
    ): Single<AccessTokenRemote>
}