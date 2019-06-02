package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.interfaces.ChatMessageClickListener;
import pl.piotrskiba.angularowo.models.ChatMessage;
import pl.piotrskiba.angularowo.utils.ColorUtils;
import pl.piotrskiba.angularowo.utils.RankUtils;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageViewHolder> {

    private Context context;
    private List<ChatMessage> mMessageList;

    private ChatMessageClickListener mClickListener;

    public ChatAdapter(Context context, ChatMessageClickListener chatMessageClickListener){
        this.context = context;
        this.mClickListener = chatMessageClickListener;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.chat_message, parent, false);

        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        ChatMessage message = mMessageList.get(position);

        String rank_color = String.format("#%06X", (0xFFFFFF & ColorUtils.changeBrightness(ResourcesCompat.getColor(
                context.getResources(),
                RankUtils.getRankColorId(message.getRank()),
                null
        ), 1.4f)));
        String message_color = String.format("#%06X", (0xFFFFFF & ColorUtils.changeBrightness(ResourcesCompat.getColor(
                context.getResources(),
                RankUtils.getRankColorId(message.getRank()),
                null
        ), 1.8f)));

        String coloredmessage = context.getString(
                R.string.chat_user_message,
                message.getRank(),
                message.getUsername(),
                message.getMessage(),
                rank_color,
                message_color
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mMessage.setText(Html.fromHtml(coloredmessage,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            holder.mMessage.setText(Html.fromHtml(coloredmessage), TextView.BufferType.SPANNABLE);
        }
    }

    @Override
    public int getItemCount() {
        if(mMessageList == null)
            return 0;
        else
            return mMessageList.size();
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.tv_message)
        TextView mMessage;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            mClickListener.onChatMessageClick(view, mMessageList.get(pos));
        }
    }

    public void addMessage(ChatMessage message){
        if(mMessageList == null)
            mMessageList = new ArrayList<>();

        mMessageList.add(message);

        notifyDataSetChanged();

    }
}
