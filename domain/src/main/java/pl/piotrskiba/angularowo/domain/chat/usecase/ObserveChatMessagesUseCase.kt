package pl.piotrskiba.angularowo.domain.chat.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.chat.repository.ChatRepository
import javax.inject.Inject

class ObserveChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute() =
        chatRepository.getLastChatMessages(preferencesRepository.accessToken!!)
            .flattenAsObservable { it.reversed() }
            .concatWith(chatRepository.observeChatMessages(preferencesRepository.accessToken!!))
}
