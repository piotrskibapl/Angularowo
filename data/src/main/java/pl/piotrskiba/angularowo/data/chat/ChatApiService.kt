package pl.piotrskiba.angularowo.data.chat

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.chat.model.ChatMessageRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatApiService {

    @GET("get_last_chat_messages.php")
    fun getLastChatMessages(
        @Query("api_key") apiKey: String,
        @Query("access_token") access_token: String,
    ): Single<List<ChatMessageRemote>>
}
