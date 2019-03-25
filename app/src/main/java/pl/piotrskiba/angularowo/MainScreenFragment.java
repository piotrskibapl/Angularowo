package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.interfaces.InvalidAccessTokenResponseListener;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.ServerStatus;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import pl.piotrskiba.angularowo.utils.RankUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenFragment extends Fragment {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_greeting)
    TextView mGreetingTextView;

    @BindView(R.id.tv_playercount)
    TextView mPlayerCountTextView;

    @BindView(R.id.iv_player_body)
    ImageView mPlayerBodyImageView;

    @BindView(R.id.tv_balance)
    TextView mPlayerBalanceTextView;

    @BindView(R.id.tv_islandlevel)
    TextView mPlayerIslandLevelTextView;

    private String username;

    private InvalidAccessTokenResponseListener listener;

    public MainScreenFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);

        if(username != null){
            populateUi();
        }

        mSwipeRefreshLayout.setOnRefreshListener(() -> populateUi());

        return view;
    }

    @Override
    public void onResume() {

        if(this.username == null){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);

            if(username != null){
                this.username = username;
                populateUi();
            }
        }

        // subscribe to player's individual Firebase topic
        if(this.username != null){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            if(!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_player_topic))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + this.username)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(getString(R.string.pref_key_subscribed_to_player_topic), true);
                                editor.apply();
                            }
                        });
            }
        }

        super.onResume();
    }

    private void populateUi(){
        mSwipeRefreshLayout.setRefreshing(true);

        mGreetingTextView.setText(getString(R.string.greeting, username));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getServerStatus(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<ServerStatus>() {
            @Override
            public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response) {
                if(response.isSuccessful() && response.body() != null && getContext() != null){
                    ServerStatus server = response.body();

                    mPlayerCountTextView.setText(getString(R.string.playercount, server.getPlayerCount()));
                }
                else if(response.code() == 401){
                    listener.onInvalidAccessTokenResponseReceived();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerStatus> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);


            }
        });

        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, username, access_token).enqueue(new Callback<DetailedPlayer>() {

            @Override
            public void onResponse(Call<DetailedPlayer> call, Response<DetailedPlayer> response) {
                if(response.isSuccessful() && response.body() != null){
                    DetailedPlayer player = response.body();

                    if(player.getUuid() != null && getContext() != null) {
                        Glide.with(getContext())
                                .load(Constants.BASE_BODY_URL + player.getUuid())
                                .into(mPlayerBodyImageView);
                    }

                    mPlayerBalanceTextView.setText(getString(R.string.balance_format, (int)player.getBalance()));
                    mPlayerIslandLevelTextView.setText(String.valueOf(player.getIslandLevel()));

                    // subscribe to player's rank Firebase topic
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String rank = sharedPreferences.getString(getString(R.string.pref_key_rank), null);
                    if(!player.getRank().equals(rank)){
                        if(rank != null){
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + rank);
                        }
                        FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + player.getRank());

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.pref_key_rank), player.getRank());
                        editor.apply();
                    }

                    // check subscription for new reports
                    if(RankUtils.isStaffRank(player.getRank())){
                        if(!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports))){
                            FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                                    .addOnCompleteListener(task -> {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(getString(R.string.pref_key_subscribed_to_new_reports), true);
                                        editor.apply();
                            });
                        }
                    }
                    else{
                        if(sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports)) && sharedPreferences.getBoolean(getString(R.string.pref_key_subscribed_to_new_reports), false)){
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                                    .addOnCompleteListener(task -> {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putBoolean(getString(R.string.pref_key_subscribed_to_new_reports), false);
                                        editor.apply();
                                    });
                        }
                    }
                }
                else if(response.code() == 401){
                    listener.onInvalidAccessTokenResponseReceived();
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DetailedPlayer> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setInvalidAccessTokenResponseListener(InvalidAccessTokenResponseListener listener){
        this.listener = listener;
    }
}
