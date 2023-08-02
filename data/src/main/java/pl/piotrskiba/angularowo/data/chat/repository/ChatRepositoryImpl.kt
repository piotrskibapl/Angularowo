package pl.piotrskiba.angularowo.data.chat.repository

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.chat.ChatApiService
import pl.piotrskiba.angularowo.data.chat.ChatWebSocket
import pl.piotrskiba.angularowo.data.chat.model.toDomain
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.chat.repository.ChatRepository

class ChatRepositoryImpl(
    private val chatApi: ChatApiService,
    private val chatWebSocket: ChatWebSocket,
) : ChatRepository {

    override fun getLastChatMessages(accessToken: String): Single<List<ChatMessageModel>> =
        chatApi.getLastChatMessages(accessToken)
            .map { it.toDomain() }

    override fun observeChatMessages(accessToken: String): Observable<ChatMessageModel> =
        chatWebSocket.open(accessToken)
            .map { it.toDomain() }
}
