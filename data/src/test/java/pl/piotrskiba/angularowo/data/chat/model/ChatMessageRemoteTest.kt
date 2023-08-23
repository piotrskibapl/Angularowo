package pl.piotrskiba.angularowo.data.chat.model

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

class ChatMessageRemoteTest {

    @Test
    fun `SHOULD map to domain model`() {
        val remoteModel = ChatMessageRemote(
            uuid = "uuid",
            username = "username",
            rank = "rank",
            message = "message",
        )

        remoteModel.toDomain() shouldBeEqualTo ChatMessageModel(
            uuid = "uuid",
            username = "username",
            rank = RankModel.unknownRank("rank"),
            message = "message",
        )
    }

    @Test
    fun `SHOULD map list to domain models`() {
        mockkStatic(ChatMessageRemote::toDomain) {
            val domainModel: ChatMessageModel = mockk()
            val remoteModel: ChatMessageRemote = mockk {
                every { toDomain() } returns domainModel
            }

            listOf(remoteModel).toDomain() shouldBeEqualTo listOf(domainModel)
        }
    }
}
