package pl.piotrskiba.angularowo.data.punishment

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.punishment.model.PunishmentRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface PunishmentApiService {

    @GET("get_punishment_list.php")
    fun getPunishmentList(
        @Query("username") username: String?,
        @Query("type") type: String,
        @Query("filter") filter: String,
    ): Single<List<PunishmentRemote>>

    @GET("punish_player.php?type=mute")
    fun mutePlayer(
        @Query("uuid") uuid: String,
        @Query("reason") reason: String,
        @Query("length") length: Long,
    ): Completable

    @GET("punish_player.php?type=kick")
    fun kickPlayer(
        @Query("uuid") uuid: String,
        @Query("reason") reason: String,
    ): Completable

    @GET("punish_player.php?type=warn")
    fun warnPlayer(
        @Query("uuid") uuid: String,
        @Query("reason") reason: String,
        @Query("length") length: Long,
    ): Completable

    @GET("punish_player.php?type=player_ban")
    fun banPlayer(
        @Query("uuid") uuid: String,
        @Query("reason") reason: String,
        @Query("length") length: Long,
    ): Completable
}
