package pl.piotrskiba.angularowo.data.chat.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.piotrskiba.angularowo.data.chat.ChatApiService
import pl.piotrskiba.angularowo.data.chat.ChatWebSocket
import pl.piotrskiba.angularowo.data.chat.model.ChatMessageRemote
import pl.piotrskiba.angularowo.data.chat.model.toDomain
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatRepositoryImplTest {

    val chatApi: ChatApiService = mockk()
    val chatWebSocket: ChatWebSocket = mockk()
    val chatMessageDomain: ChatMessageModel = mockk()
    val chatMessageRemote: ChatMessageRemote = mockk()
    val tested = ChatRepositoryImpl(chatApi, chatWebSocket)

    @BeforeAll
    fun setup() {
        mockkStatic(ChatMessageRemote::toDomain)
        every { chatMessageRemote.toDomain() } returns chatMessageDomain
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD return list of last chat messages WHEN getLastChatMessages called`() {
        every { chatApi.getLastChatMessages() } returns Single.just(listOf(chatMessageRemote))

        val result = tested.getLastChatMessages().test()

        result.assertValue(listOf(chatMessageDomain))
    }

    @Test
    fun `SHOULD observe chat messages WHEN observeChatMessages called`() {
        every { chatWebSocket.open() } returns Observable.just(chatMessageRemote)

        val result = tested.observeChatMessages().test()

        result.assertValue(chatMessageDomain)
    }
}
