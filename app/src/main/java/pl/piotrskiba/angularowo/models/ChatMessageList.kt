package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName

class ChatMessageList(@field:SerializedName("messages") val chatMessageList: ArrayList<ChatMessage>)