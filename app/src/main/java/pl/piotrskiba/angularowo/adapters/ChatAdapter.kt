package pl.piotrskiba.angularowo.adapters

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.ChatAdapter.ChatMessageViewHolder
import pl.piotrskiba.angularowo.interfaces.ChatMessageClickListener
import pl.piotrskiba.angularowo.models.ChatMessage
import pl.piotrskiba.angularowo.models.ChatMessageList
import pl.piotrskiba.angularowo.utils.ColorUtils.changeBrightness
import pl.piotrskiba.angularowo.utils.ColorUtils.getColorFromCode
import pl.piotrskiba.angularowo.utils.RankUtils.getRankFromName
import pl.piotrskiba.angularowo.utils.TextUtils.replaceColorCodes

class ChatAdapter(private val context: Context, private val mClickListener: ChatMessageClickListener) : RecyclerView.Adapter<ChatMessageViewHolder>() {
    private var mMessageList: ArrayList<ChatMessage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val context = parent.context

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.chat_message, parent, false)

        return ChatMessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val message = mMessageList[position]
        val rank = getRankFromName(message.rank)

        val rankColor = String.format("#%06X", 0xFFFFFF and changeBrightness(getColorFromCode(context, rank.colorCode), 1.4f))
        val messageColor = String.format("#%06X", 0xFFFFFF and changeBrightness(getColorFromCode(context, rank.chatColorCode), 1.4f))

        var coloredmessage = context.getString(
                R.string.chat_user_message,
                message.rank,
                message.username,
                message.message,
                rankColor,
                messageColor
        )
        coloredmessage = replaceColorCodes(context, coloredmessage)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mMessage.setText(Html.fromHtml(coloredmessage, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE)
        } else {
            holder.mMessage.setText(Html.fromHtml(coloredmessage), TextView.BufferType.SPANNABLE)
        }
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    inner class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @BindView(R.id.tv_message)
        lateinit var mMessage: TextView

        override fun onClick(view: View) {
            mClickListener.onChatMessageClick(view, mMessageList[adapterPosition])
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    fun setMessageList(messageList: ChatMessageList) {
        mMessageList = messageList.chatMessageList
        notifyDataSetChanged()
    }

    fun addMessage(message: ChatMessage) {
        mMessageList.clear()
        mMessageList.add(message)
        notifyDataSetChanged()
    }

}