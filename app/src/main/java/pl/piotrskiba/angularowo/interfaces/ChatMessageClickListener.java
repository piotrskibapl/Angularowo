package pl.piotrskiba.angularowo.interfaces;

import android.view.View;

import pl.piotrskiba.angularowo.models.ChatMessage;

public interface ChatMessageClickListener {

    void onChatMessageClick(View view, ChatMessage clickedChatMessage);
}
