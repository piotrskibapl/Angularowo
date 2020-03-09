package pl.piotrskiba.angularowo.interfaces

import android.view.View
import pl.piotrskiba.angularowo.models.ChatMessage

interface ChatMessageClickListener {
    fun onChatMessageClick(view: View, clickedChatMessage: ChatMessage)
}