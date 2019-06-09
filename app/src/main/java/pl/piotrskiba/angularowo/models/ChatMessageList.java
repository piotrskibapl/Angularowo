package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChatMessageList {

    @SerializedName("messages")
    private final List<ChatMessage> chatMessageList;

    public ChatMessageList(List<ChatMessage> chatMessageList){
        this.chatMessageList = chatMessageList;
    }

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }
}
