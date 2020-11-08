package pl.piotrskiba.angularowo.network

import pl.piotrskiba.angularowo.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPIInterface {
    @GET("get_server_status.php")
    fun getServerStatus(@Query("api_key") apiKey: String, @Query("access_token") access_token: String): Call<ServerStatus>

    @GET("get_online_players.php")
    fun getPlayers(@Query("api_key") apiKey: String, @Query("access_token") access_token: String): Call<PlayerList>

    @GET("get_ban_list.php")
    fun getAllBans(@Query("api_key") apiKey: String, @Query("type") type: String, @Query("username") username: String?, @Query("access_token") access_token: String): Call<BanList>

    @GET("get_ban_list.php?filter=active")
    fun getActiveBans(@Query("api_key") apiKey: String, @Query("type") type: String, @Query("username") username: String?, @Query("access_token") access_token: String): Call<BanList>

    @GET("register_device.php")
    fun registerDevice(@Query("api_key") apiKey: String, @Query("user_code") user_code: String): Call<AccessToken>

    @GET("get_player_info.php")
    fun getPlayerInfoFromUsername(@Query("api_key") apiKey: String, @Query("username") username: String, @Query("access_token") access_token: String): Call<DetailedPlayer>

    @GET("get_player_info.php")
    fun getPlayerInfoFromUuid(@Query("api_key") apiKey: String, @Query("uuid") uuid: String, @Query("access_token") access_token: String): Call<DetailedPlayer>

    @GET("get_offers_info.php")
    fun getOffersInfo(@Query("api_key") apiKey: String, @Query("access_token") access_token: String): Call<OffersInfo>

    @GET("redeem_ad_offer.php")
    fun redeemAdOffer(@Query("api_key") apiKey: String, @Query("offer_id") offerId: String, @Query("access_token") access_token: String): Call<Void>

    @GET("redeem_offer.php")
    fun redeemOffer(@Query("api_key") apiKey: String, @Query("offer_id") offerId: String, @Query("access_token") access_token: String): Call<Void>

    @GET("get_last_chat_messages.php")
    fun getLastChatMessages(@Query("api_key") apiKey: String, @Query("access_token") access_token: String): Call<ChatMessageList>

    @GET("get_reports.php?own")
    fun getUserReports(@Query("api_key") apiKey: String, @Query("access_token") access_token: String): Call<ReportList>

    @GET("get_reports.php")
    fun getAllReports(@Query("api_key") apiKey: String, @Query("archived") archived: Boolean, @Query("access_token") access_token: String): Call<ReportList>

    @GET("punish_player.php?type=mute")
    fun mutePlayer(@Query("api_key") apiKey: String, @Query("uuid") uuid: String, @Query("reason") reason: String, @Query("length") length: Long, @Query("access_token") access_token: String): Call<Void>

    @GET("punish_player.php?type=kick")
    fun kickPlayer(@Query("api_key") apiKey: String, @Query("uuid") uuid: String, @Query("reason") reason: String, @Query("access_token") access_token: String): Call<Void>

    @GET("punish_player.php?type=warn")
    fun warnPlayer(@Query("api_key") apiKey: String, @Query("uuid") uuid: String, @Query("reason") reason: String, @Query("length") length: Long, @Query("access_token") access_token: String): Call<Void>

    @GET("punish_player.php?type=player_ban")
    fun banPlayer(@Query("api_key") apiKey: String, @Query("uuid") uuid: String, @Query("reason") reason: String, @Query("length") length: Long, @Query("access_token") access_token: String): Call<Void>

}