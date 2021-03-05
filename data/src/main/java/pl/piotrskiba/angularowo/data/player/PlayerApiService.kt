package pl.piotrskiba.angularowo.data.player

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.player.model.DetailedPlayerData
import pl.piotrskiba.angularowo.data.player.model.PlayerData
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerApiService {

    @GET("get_player_info.php")
    fun getPlayerInfoFromUsername(
        @Query("api_key") apiKey: String,
        @Query("username") username: String,
        @Query("access_token") access_token: String
    ): Single<DetailedPlayerData>

    @GET("get_player_info.php")
    fun getPlayerInfoFromUuid(
        @Query("api_key") apiKey: String,
        @Query("uuid") uuid: String,
        @Query("access_token") access_token: String
    ): Single<DetailedPlayerData>

    @GET("get_online_players.php")
    fun getOnlinePlayerList(
        @Query("api_key") apiKey: String,
        @Query("access_token") access_token: String
    ): Single<List<PlayerData>>
}
