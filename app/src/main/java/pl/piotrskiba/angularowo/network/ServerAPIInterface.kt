package pl.piotrskiba.angularowo.network

import pl.piotrskiba.angularowo.models.ChatMessageList
import pl.piotrskiba.angularowo.models.DetailedPlayer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPIInterface {
    @GET("get_player_info.php")
    fun getPlayerInfoFromUuid(@Query("api_key") apiKey: String, @Query("uuid") uuid: String, @Query("access_token") access_token: String): Call<DetailedPlayer>

    @GET("get_last_chat_messages.php")
    fun getLastChatMessages(@Query("api_key") apiKey: String, @Query("access_token") access_token: String): Call<ChatMessageList>
}
