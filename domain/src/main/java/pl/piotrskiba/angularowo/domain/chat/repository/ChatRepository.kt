package pl.piotrskiba.angularowo.domain.chat.repository

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel

interface ChatRepository {

    fun getLastChatMessages(): Single<List<ChatMessageModel>>

    fun observeChatMessages(accessToken: String): Observable<ChatMessageModel>
}
