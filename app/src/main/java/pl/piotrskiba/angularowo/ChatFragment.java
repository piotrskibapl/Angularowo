package pl.piotrskiba.angularowo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.collect.Lists;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import pl.piotrskiba.angularowo.adapters.ChatAdapter;
import pl.piotrskiba.angularowo.interfaces.ChatMessageClickListener;
import pl.piotrskiba.angularowo.interfaces.InvalidAccessTokenResponseListener;
import pl.piotrskiba.angularowo.models.ChatMessage;
import pl.piotrskiba.angularowo.models.ChatMessageList;
import pl.piotrskiba.angularowo.models.Player;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;

public class ChatFragment extends Fragment implements ChatMessageClickListener {

    @BindView(R.id.rv_chat)
    RecyclerView mChat;

    @BindView(R.id.errorTextView)
    TextView mErrorTextView;

    @BindView(R.id.pb_loading)
    ProgressBar mLoadingIndicator;

    private ChatAdapter mChatAdapter;

    private OkHttpClient mOkHttpClient;

    private InvalidAccessTokenResponseListener listener;

    private WebSocket mWebSocket;

    public ChatFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        ButterKnife.bind(this, view);

        mChatAdapter = new ChatAdapter(getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mChat.setAdapter(mChatAdapter);
        mChat.setLayoutManager(layoutManager);
        mChat.setHasFixedSize(true);

        preloadChatMessages();

        mOkHttpClient = new OkHttpClient();
        startChatWebSocket();

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_chat);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mWebSocket.close(1000, null);
    }

    private void preloadChatMessages(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getLastChatMessages(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<ChatMessageList>() {
            @Override
            public void onResponse(Call<ChatMessageList> call, retrofit2.Response<ChatMessageList> response) {
                if(isAdded()){
                    if (response.isSuccessful() && response.body() != null) {
                        mChatAdapter.setMessageList(new ChatMessageList(Lists.reverse(response.body().getChatMessageList())));
                        mChat.scrollToPosition(mChatAdapter.getItemCount() - 1);
                        showDefaultLayout();
                    } else if (response.code() == 401) {
                        listener.onInvalidAccessTokenResponseReceived();
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatMessageList> call, Throwable t) {
            }
        });
    }

    private void showDefaultLayout(){
        mChat.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void showErrorLayout(){
        mChat.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(R.string.chat_error);
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void startChatWebSocket() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        Request request = new Request.Builder()
                .url(Constants.CHAT_WEBSOCKET_URL)
                .addHeader(Constants.CHAT_WEBSOCKET_ACCESSTOKEN_HEADER, access_token)
                .build();
        ChatWebSocketListener listener = new ChatWebSocketListener();
        mWebSocket = mOkHttpClient.newWebSocket(request, listener);
        mOkHttpClient.dispatcher().executorService().shutdown();
    }

    @Override
    public void onChatMessageClick(View view, ChatMessage clickedChatMessage) {
        Intent intent = new Intent(getContext(), PlayerDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_PLAYER,
                new Player(
                        clickedChatMessage.getUsername(),
                        null,
                        clickedChatMessage.getRank(),
                        false
                )
        );

        startActivity(intent);
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
                if(!mChat.canScrollVertically(1)) {
                    mChatAdapter.addMessage(chatMessage);
                    mChat.scrollToPosition(mChatAdapter.getItemCount() - 1);
                }
                else{
                    mChatAdapter.addMessage(chatMessage);
                }
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

    public void setInvalidAccessTokenResponseListener(InvalidAccessTokenResponseListener listener){
        this.listener = listener;
    }
}
