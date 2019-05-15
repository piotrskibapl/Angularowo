package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import pl.piotrskiba.angularowo.adapters.ChatAdapter;
import pl.piotrskiba.angularowo.models.ChatMessage;

public class ChatFragment extends Fragment {

    @BindView(R.id.rv_chat)
    RecyclerView mChat;

    @BindView(R.id.errorTextView)
    TextView mErrorTextView;

    private ChatAdapter mChatAdapter;

    private OkHttpClient mOkHttpClient;

    public ChatFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, view);

        mChatAdapter = new ChatAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mChat.setAdapter(mChatAdapter);
        mChat.setLayoutManager(layoutManager);
        mChat.setHasFixedSize(true);

        mOkHttpClient = new OkHttpClient();
        startChatWebSocket();

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_chat);

        return view;
    }

    private void showErrorLayout(){
        mChat.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(R.string.chat_error);
    }

    private void startChatWebSocket() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        Request request = new Request.Builder()
                .url(Constants.CHAT_WEBSOCKET_URL)
                .addHeader(Constants.CHAT_WEBSOCKET_ACCESSTOKEN_HEADER, access_token)
                .build();
        ChatWebSocketListener listener = new ChatWebSocketListener();
        mOkHttpClient.newWebSocket(request, listener);
        mOkHttpClient.dispatcher().executorService().shutdown();
    }

    private final class ChatWebSocketListener extends WebSocketListener{
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            String[] messageInfo = text.split(";");
            String username = messageInfo[0];
            String rank = messageInfo[1];
            String body = messageInfo[2];

            ChatMessage chatMessage = new ChatMessage(username, rank, body);
            getActivity().runOnUiThread(() -> {
                mChatAdapter.addMessage(chatMessage);
            });
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, @javax.annotation.Nullable Response response) {
            t.printStackTrace();

            if(isAdded()) {
                getActivity().runOnUiThread(() -> showErrorLayout());
            }
        }
    }
}
