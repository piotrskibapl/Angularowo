package pl.piotrskiba.angularowo.main.chat.viewmodel

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.amshove.kluent.assertSoftly
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pl.piotrskiba.angularowo.base.InstantTaskExecutor
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.rx.TestSchedulersFacade
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.chat.usecase.ObserveChatMessagesUseCase
import pl.piotrskiba.angularowo.main.chat.model.ChatMessage
import pl.piotrskiba.angularowo.main.chat.model.toUi
import pl.piotrskiba.angularowo.main.chat.nav.ChatNavigator

@ExtendWith(InstantTaskExecutor::class)
class ChatViewModelTest {

    val observeChatMessagesUseCase: ObserveChatMessagesUseCase = mockk()
    val navigator: ChatNavigator = mockk(relaxed = true)
    val tested = ChatViewModel(
        observeChatMessagesUseCase = observeChatMessagesUseCase,
        facade = TestSchedulersFacade(),
    )

    @Test
    fun `SHOULD observe chat messages and set state to loaded WHEN onFirstCreate called`() {
        mockkStatic(ChatMessageModel::toUi) {
            val uiChatMessage1: ChatMessage = mockk()
            val uiChatMessage2: ChatMessage = mockk()
            val domainChatMessage1: ChatMessageModel = mockk(relaxed = true) {
                every { toUi() } returns uiChatMessage1
            }
            val domainChatMessage2: ChatMessageModel = mockk(relaxed = true) {
                every { toUi() } returns uiChatMessage2
            }
            every { observeChatMessagesUseCase.execute() } returns Observable.just(domainChatMessage1, domainChatMessage2)

            tested.onFirstCreate()

            assertSoftly {
                tested.chatMessages.value shouldBeEqualTo listOf(uiChatMessage1, uiChatMessage2)
                tested.state.value shouldBeEqualTo ViewModelState.Loaded
            }
        }
    }

    @Test
    fun `SHOULD set state to Error WHEN onFirstCreate called AND observeChatMessagesUseCase fails`() {
        val exception = Exception()
        every { observeChatMessagesUseCase.execute() } returns Observable.error(exception)

        tested.onFirstCreate()

        tested.state.value shouldBeEqualTo ViewModelState.Error(exception)
    }

    @Test
    fun `SHOULD navigate to player details WHEN onChatMessageClick called`() {
        val previewedPlayerUuid = "uuid"
        val chatMessage: ChatMessage = mockk {
            every { uuid } returns previewedPlayerUuid
        }
        tested.navigator = navigator

        tested.onChatMessageClick(chatMessage)

        verify { tested.navigator.navigateToPlayerDetails(previewedPlayerUuid) }
    }
}
