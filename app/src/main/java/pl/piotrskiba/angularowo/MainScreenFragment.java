package pl.piotrskiba.angularowo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.BanListAdapter;
import pl.piotrskiba.angularowo.interfaces.BanClickListener;
import pl.piotrskiba.angularowo.interfaces.DataLoadedListener;
import pl.piotrskiba.angularowo.models.Ban;
import pl.piotrskiba.angularowo.models.BanList;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.ServerStatus;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import pl.piotrskiba.angularowo.utils.GlideUtils;
import pl.piotrskiba.angularowo.utils.RankUtils;
import pl.piotrskiba.angularowo.utils.TextUtils;
import pl.piotrskiba.angularowo.utils.UrlUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenFragment extends Fragment implements BanClickListener {

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

    @BindView(R.id.tv_tokens)
    TextView mPlayerTokensTextView;

    @BindView(R.id.tv_playtime)
    TextView mPlayerPlayTimeTextView;

    @BindView(R.id.tv_last_bans_title)
    TextView mLastBansTitleTextView;

    @BindView(R.id.rv_bans)
    RecyclerView mBanList;

    @BindView(R.id.default_layout)
    View mDefaultLayout;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    @BindView(R.id.server_error_layout)
    LinearLayout mServerErrorLayout;

    private String username;

    private BanListAdapter mBanListAdapter;

    private boolean error = false;

    public MainScreenFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        ButterKnife.bind(this, view);

        mBanListAdapter = new BanListAdapter(getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBanList.setAdapter(mBanListAdapter);
        mBanList.setLayoutManager(layoutManager);
        mBanList.setHasFixedSize(true);

        mSwipeRefreshLayout.setOnRefreshListener(() -> populateUi());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);
        populateUi();

        return view;
    }

    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);

        if(this.username == null || !this.username.equals(username) || error){
            this.username = username;

            if(username != null)
                populateUi();
        }

        if(this.username != null){
            // subscribe to current app version Firebase topic
            if(!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_app_version_topic, BuildConfig.VERSION_CODE))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_APP_VERSION_TOPIC_PREFIX + BuildConfig.VERSION_CODE)
                        .addOnCompleteListener(task -> {
                            if(isAdded()) {
                                if (task.isSuccessful()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_app_version_topic, BuildConfig.VERSION_CODE), true);
                                    editor.apply();
                                }
                            }
                        });
            }

            // subscribe to player's individual Firebase topic
            if(!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_player_topic))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + this.username)
                        .addOnCompleteListener(task -> {
                            if(isAdded()) {
                                if (task.isSuccessful()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_player_topic), true);
                                    editor.apply();
                                }
                            }
                        });
            }

            // subscribe to new events Firebase topic
            if(!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_events))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_EVENT_TOPIC)
                        .addOnCompleteListener(task -> {
                            if(isAdded()) {
                                if (task.isSuccessful()) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean(getString(R.string.pref_key_subscribed_to_events), true);
                                    editor.apply();
                                }
                            }
                        });
            }
        }

        super.onResume();
    }

    private void populateUi(){
        showDefaultLayout();
        mSwipeRefreshLayout.setRefreshing(true);

        mGreetingTextView.setText(getString(R.string.greeting, username));

        loadData();

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.app_name);
    }

    private void loadData(){
        loadServerStatus(() -> loadPlayerDetails(() -> loadBanList(mBanListAdapter, () -> mSwipeRefreshLayout.setRefreshing(false))));
    }

    private void loadServerStatus(DataLoadedListener listener){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getServerStatus(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<ServerStatus>() {
            @Override
            public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response) {
                if(response.isSuccessful() && response.body() != null && getContext() != null){
                    ServerStatus server = response.body();
                    mPlayerCountTextView.setText(getResources().getQuantityString(R.plurals.playercount, server.getPlayerCount(), server.getPlayerCount()));

                    listener.onDataLoaded();
                }
                else if(!response.isSuccessful()){
                    showServerErrorLayout();
                }
            }

            @Override
            public void onFailure(Call<ServerStatus> call, Throwable t) {
                showNoInternetLayout();
                t.printStackTrace();
            }
        });
    }

    private void loadPlayerDetails(DataLoadedListener listener){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, username, access_token).enqueue(new Callback<DetailedPlayer>() {

            @Override
            public void onResponse(Call<DetailedPlayer> call, Response<DetailedPlayer> response) {
                if(isAdded()) {
                    if (response.isSuccessful() && response.body() != null) {
                        DetailedPlayer player = response.body();

                        if (player.getUuid() != null && getContext() != null) {
                            Glide.with(getContext())
                                    .load(UrlUtils.buildBodyUrl(player.getUuid(), true))
                                    .signature(new IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(1)))
                                    .placeholder(R.drawable.default_body)
                                    .into(mPlayerBodyImageView);
                        }

                        mPlayerBalanceTextView.setText(getString(R.string.balance_format, (int) player.getBalance()));
                        mPlayerTokensTextView.setText(String.valueOf(player.getTokens()));
                        mPlayerPlayTimeTextView.setText(TextUtils.formatPlaytime(getContext(), player.getPlaytime()));

                        if (player.getRank() != null) {
                            subscribeToFirebaseRankTopic(player.getRank());
                            checkFirebaseNewReportsTopicSubscription(player.getRank());
                        }

                        listener.onDataLoaded();
                    }
                    else if(!response.isSuccessful()){
                        showServerErrorLayout();
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailedPlayer> call, Throwable t) {
                if(isAdded()) {
                    showNoInternetLayout();
                }
                t.printStackTrace();
            }
        });
    }

    private void subscribeToFirebaseRankTopic(String rank){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String previous_rank = sharedPreferences.getString(getString(R.string.pref_key_rank), null);

        if (!rank.equals(previous_rank)) {
            if (previous_rank != null) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + previous_rank);
            }
            FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + rank);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.pref_key_rank), rank);
            editor.apply();
        }
    }

    private void checkFirebaseNewReportsTopicSubscription(String rank){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (RankUtils.isStaffRank(rank)) {
            if (!sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports))) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                        .addOnCompleteListener(task -> {
                            if(isAdded()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(getString(R.string.pref_key_subscribed_to_new_reports), true);
                                editor.apply();
                            }
                            else if(getActivity() != null){
                                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putBoolean(getString(R.string.pref_key_subscribed_to_new_reports), true);
                                editor.apply();
                            }
                        });
            }
        } else {
            if (sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports)) && sharedPreferences.getBoolean(getString(R.string.pref_key_subscribed_to_new_reports), false)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                        .addOnCompleteListener(task -> {
                            if(isAdded()) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove(getString(R.string.pref_key_subscribed_to_new_reports));
                                editor.apply();
                            }
                            else if(getActivity() != null){
                                SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.remove(getString(R.string.pref_key_subscribed_to_new_reports));
                                editor.apply();
                            }
                        });
            }
        }
    }

    private void loadBanList(BanListAdapter adapter, DataLoadedListener listener){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getActiveBans(ServerAPIClient.API_KEY, Constants.ACTIVE_BAN_TYPES, username, access_token).enqueue(new Callback<BanList>() {
            @Override
            public void onResponse(Call<BanList> call, Response<BanList> response) {
                if(isAdded()) {
                    if (response.isSuccessful() && response.body() != null) {
                        adapter.setBanList(response.body());
                        if (!response.body().getBanList().isEmpty())
                            showLastBans();
                    } else if (response.code() == 401) {
                        hideLastBans();
                    }
                    else{
                        hideLastBans();
                    }

                    listener.onDataLoaded();
                }
            }

            @Override
            public void onFailure(Call<BanList> call, Throwable t) {
                if(isAdded()) {
                    showNoInternetLayout();
                }
                t.printStackTrace();
            }
        });
    }

    private void showDefaultLayout(){
        mDefaultLayout.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
        error = false;
    }
    private void showNoInternetLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mServerErrorLayout.setVisibility(View.GONE);
        error = true;
    }
    private void showServerErrorLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.VISIBLE);
        error = true;
    }

    private void showLastBans(){
        mLastBansTitleTextView.setVisibility(View.VISIBLE);
        mBanList.setVisibility(View.VISIBLE);
    }
    private void hideLastBans(){
        mLastBansTitleTextView.setVisibility(View.GONE);
        mBanList.setVisibility(View.GONE);
    }

    @Override
    public void onBanClick(View view, Ban clickedBan) {
        BanListAdapter.BanViewHolder banViewHolder = (BanListAdapter.BanViewHolder) view.getTag();

        Intent intent = new Intent(getContext(), BanDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_BAN, clickedBan);
        if(banViewHolder.mPlayerAvatar.getDrawable() != null) {
            Bitmap avatarBitmap = ((BitmapDrawable) banViewHolder.mPlayerAvatar.getDrawable()).getBitmap();
            intent.putExtra(Constants.EXTRA_BITMAP, avatarBitmap);
        }
        if(getActivity() != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), view, getString(R.string.ban_banner_transition_name));
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }
}
