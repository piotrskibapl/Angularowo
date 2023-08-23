package pl.piotrskiba.angularowo.domain.chat.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.chat.repository.ChatRepository
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository

class ObserveChatMessagesUseCaseTest {

    val rankName = "rank"
    val rankModel: RankModel = mockk {
        every { name } returns rankName
    }
    val chatRepository: ChatRepository = mockk()
    val rankRepository: RankRepository = mockk()
    val tested = ObserveChatMessagesUseCase(chatRepository, rankRepository)

    @Test
    fun `SHOULD get existing chat messages in reversed orderAND observe new ones`() {
        val oldChatMessageModels: List<ChatMessageModel> = listOf(
            mockedChatMessage(),
            mockedChatMessage(),
            mockedChatMessage(),
        )
        val newChatMessageModel: ChatMessageModel = mockedChatMessageWithUnknownRank()
        every { rankRepository.getAllRanks() } returns Single.just(listOf(rankModel))
        every { chatRepository.getLastChatMessages() } returns Single.just(oldChatMessageModels)
        every { chatRepository.observeChatMessages() } returns Observable.just(newChatMessageModel)

        val result = tested.execute().test()

        result.assertValueSequence(oldChatMessageModels.reversed() + newChatMessageModel)
    }

    fun mockedChatMessage(): ChatMessageModel = mockk {
        val selfChatMessageModel = this
        every { rank } returns mockk {
            every { name } returns rankName
            every { copy(rank = rankModel) } returns selfChatMessageModel
        }
    }

    fun mockedChatMessageWithUnknownRank(): ChatMessageModel = mockk {
        val selfChatMessageModel = this
        every { rank } returns mockk {
            val selfRankModel = this
            every { name } returns "unknownRank"
            every { copy(rank = selfRankModel) } returns selfChatMessageModel
        }
    }
}
