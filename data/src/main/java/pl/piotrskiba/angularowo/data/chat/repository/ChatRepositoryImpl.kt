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

    override fun getLastChatMessages(): Single<List<ChatMessageModel>> =
        chatApi.getLastChatMessages()
            .map { it.toDomain() }

    override fun observeChatMessages(): Observable<ChatMessageModel> =
        chatWebSocket.open()
            .map { it.toDomain() }
}
