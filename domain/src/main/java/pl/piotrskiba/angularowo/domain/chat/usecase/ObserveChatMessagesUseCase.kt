package pl.piotrskiba.angularowo.domain.chat.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.chat.repository.ChatRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class ObserveChatMessagesUseCase @Inject constructor(
    private val chatRepository: ChatRepository,
    private val rankRepository: RankRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute() =
        preferencesRepository.accessToken()
            .toSingle()
            .zipWith(rankRepository.getAllRanks(), ::Pair)
            .flatMapObservable { (accessToken, ranks) ->
                chatRepository.getLastChatMessages(accessToken)
                    .flattenAsObservable { it.reversed() }
                    .concatWith(chatRepository.observeChatMessages(accessToken))
                    .map { chatMessageModel ->
                        val updatedRank = ranks.firstOrNull { it.name == chatMessageModel.rank.name } ?: chatMessageModel.rank
                        chatMessageModel.copy(rank = updatedRank)
                    }
            }
}
