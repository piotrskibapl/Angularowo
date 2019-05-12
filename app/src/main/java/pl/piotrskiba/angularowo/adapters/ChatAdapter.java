package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.models.ChatMessage;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatMessageViewHolder> {

    private List<ChatMessage> mMessageList;

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
        holder.mMessageSender.setText(message.getUsername());
        holder.mMessageBody.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        if(mMessageList == null)
            return 0;
        else
            return mMessageList.size();
    }

    class ChatMessageViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_player_name)
        TextView mMessageSender;

        @BindView(R.id.tv_message)
        TextView mMessageBody;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public void addMessage(ChatMessage message){
        if(mMessageList == null)
            mMessageList = new ArrayList<>();

        mMessageList.add(message);

        notifyDataSetChanged();
    }
}
