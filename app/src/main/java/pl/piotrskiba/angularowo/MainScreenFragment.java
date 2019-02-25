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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.ServerStatus;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
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
        if(username == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);
            if(username != null){
                this.username = username;
                populateUi();
            }
        }

        super.onResume();
    }

    private void populateUi(){
        mSwipeRefreshLayout.setRefreshing(true);

        mGreetingTextView.setText(getString(R.string.greeting, username));

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getServerStatus(ServerAPIClient.API_KEY).enqueue(new Callback<ServerStatus>() {
            @Override
            public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response) {
                if(response.isSuccessful() && response.body() != null && getContext() != null){
                    ServerStatus server = response.body();

                    mPlayerCountTextView.setText(getString(R.string.playercount, server.getPlayerCount()));
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ServerStatus> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, username).enqueue(new Callback<DetailedPlayer>() {

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
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DetailedPlayer> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
