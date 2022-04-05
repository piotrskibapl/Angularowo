package pl.piotrskiba.angularowo.network

import pl.piotrskiba.angularowo.models.ChatMessageList
import pl.piotrskiba.angularowo.models.DetailedPlayer
import pl.piotrskiba.angularowo.models.OffersInfo
import pl.piotrskiba.angularowo.models.ReportList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPIInterface {
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
}
