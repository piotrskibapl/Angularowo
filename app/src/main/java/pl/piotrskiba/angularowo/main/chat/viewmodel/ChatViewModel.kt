package pl.piotrskiba.angularowo.main.chat.viewmodel

import androidx.lifecycle.MutableLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
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

    val chatMessages = MutableLiveData<List<ChatMessage>>(emptyList())
    val chatMessagesBinding = ItemBinding.of<ChatMessage>(BR.chatMessage, R.layout.chat_message_list_item)
    lateinit var navigator: ChatNavigator

    // TODO: add state handling
    override fun onFirstCreate() {
        chatMessagesBinding.bindExtra(BR.navigator, navigator)
        disposables.add(
            observeChatMessagesUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe { chatMessageModel ->
                    chatMessages.value = chatMessages.value!! + chatMessageModel.toUi()
                }
        )
    }
}
