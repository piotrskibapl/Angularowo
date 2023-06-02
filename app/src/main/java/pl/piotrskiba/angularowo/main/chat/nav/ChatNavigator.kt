package pl.piotrskiba.angularowo.main.chat.nav

import pl.piotrskiba.angularowo.main.chat.model.ChatMessage

interface ChatNavigator {
    fun onChatMessageClick(chatMessage: ChatMessage)
}
