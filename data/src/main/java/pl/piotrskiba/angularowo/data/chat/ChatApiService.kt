package pl.piotrskiba.angularowo.data.chat

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.chat.model.ChatMessageRemote
import retrofit2.http.GET

interface ChatApiService {

    @GET("get_last_chat_messages.php")
    fun getLastChatMessages(): Single<List<ChatMessageRemote>>
}
