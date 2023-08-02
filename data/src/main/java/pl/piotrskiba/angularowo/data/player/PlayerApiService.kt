package pl.piotrskiba.angularowo.data.player

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.player.model.DetailedPlayerRemote
import pl.piotrskiba.angularowo.data.player.model.PlayerRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayerApiService {

    @GET("get_player_info.php")
    fun getPlayerInfoFromUsername(
        @Query("username") username: String,
    ): Single<DetailedPlayerRemote>

    @GET("get_player_info.php")
    fun getPlayerInfoFromUuid(
        @Query("uuid") uuid: String,
    ): Single<DetailedPlayerRemote>

    @GET("get_online_players.php")
    fun getOnlinePlayerList(): Single<List<PlayerRemote>>
}
