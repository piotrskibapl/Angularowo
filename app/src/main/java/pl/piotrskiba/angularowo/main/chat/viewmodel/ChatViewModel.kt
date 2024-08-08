package pl.piotrskiba.angularowo.main.chat.viewmodel

import androidx.lifecycle.MutableLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.chat.usecase.ObserveChatMessagesUseCase
import pl.piotrskiba.angularowo.main.chat.model.ChatMessage
import pl.piotrskiba.angularowo.main.chat.model.toUi
import pl.piotrskiba.angularowo.main.chat.nav.ChatNavigator
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val observeChatMessagesUseCase: ObserveChatMessagesUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    // TODO: add possibility to refresh in case of error state
    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val chatMessages = MutableLiveData<List<ChatMessage>>(emptyList())
    val chatMessagesBinding = ItemBinding.of<ChatMessage>(BR.chatMessage, R.layout.chat_message_list_item)
    lateinit var navigator: ChatNavigator

    override fun onFirstCreate() {
        disposables.add(
            observeChatMessagesUseCase.execute()
                .applyDefaultSchedulers(facade)
                .subscribe(
                    { chatMessageModel ->
                        chatMessages.value = chatMessages.value!! + chatMessageModel.toUi()
                        state.value = Loaded
                    },
                    { error ->
                        state.value = Error(error)
                    },
                ),
        )
    }

    fun onChatMessageClick(chatMessage: ChatMessage) {
        navigator.navigateToPlayerDetails(chatMessage.uuid)
    }
}
