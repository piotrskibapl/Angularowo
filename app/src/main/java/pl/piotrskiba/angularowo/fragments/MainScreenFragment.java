package pl.piotrskiba.angularowo.fragments;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.AppViewModel;
import pl.piotrskiba.angularowo.BuildConfig;
import pl.piotrskiba.angularowo.Constants;
import pl.piotrskiba.angularowo.IntegerVersionSignature;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.activities.BanDetailsActivity;
import pl.piotrskiba.angularowo.adapters.BanListAdapter;
import pl.piotrskiba.angularowo.interfaces.BanClickListener;
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener;
import pl.piotrskiba.angularowo.models.Ban;
import pl.piotrskiba.angularowo.models.Rank;
import pl.piotrskiba.angularowo.utils.GlideUtils;
import pl.piotrskiba.angularowo.utils.PreferenceUtils;
import pl.piotrskiba.angularowo.utils.RankUtils;
import pl.piotrskiba.angularowo.utils.TextUtils;
import pl.piotrskiba.angularowo.utils.UrlUtils;

public class MainScreenFragment extends Fragment implements BanClickListener, NetworkErrorListener {

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

    private BanListAdapter mBanListAdapter;

    private boolean loadedServerStatus = false;
    private boolean loadedPlayer = false;
    private boolean loadedActivePlayerBans = false;

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

        mSwipeRefreshLayout.setOnRefreshListener(() -> refreshData());

        populateUi();

        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.setNetworkErrorListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        seekForServerStatusUpdates();
        seekForPlayerUpdates();
        seekForLastPlayerBans();


        return view;
    }

    private void seekForServerStatusUpdates(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getServerStatus().observe(this, serverStatus -> {
            if(serverStatus != null) {
                loadedServerStatus = true;
                showDefaultLayoutIfLoadedAllData();

                Rank rank = RankUtils.getRankFromPreferences(getContext());

                if (rank != null && rank.isStaff()) {
                    String tps = TextUtils.formatTps(serverStatus.getTps());
                    mPlayerCountTextView.setText(getResources().getQuantityString(R.plurals.playercount_tps, serverStatus.getPlayerCount(), serverStatus.getPlayerCount(), tps));
                } else {
                    mPlayerCountTextView.setText(getResources().getQuantityString(R.plurals.playercount, serverStatus.getPlayerCount(), serverStatus.getPlayerCount()));
                }
            }
        });
    }

    private void seekForPlayerUpdates(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getPlayer().observe(this, player -> {
            if (player != null) {
                loadedPlayer = true;
                showDefaultLayoutIfLoadedAllData();

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
            }
        });
    }

    private void seekForLastPlayerBans(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getActivePlayerBans().observe(this, banList -> {
            if(banList != null) {
                loadedActivePlayerBans = true;
                showDefaultLayoutIfLoadedAllData();

                mBanListAdapter.setBanList(banList);
                if (!banList.getBanList().isEmpty())
                    showLastBans();
            }
        });
    }

    private void showDefaultLayoutIfLoadedAllData(){
        if(loadedServerStatus && loadedPlayer && loadedActivePlayerBans) {
            showDefaultLayout();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void refreshData(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        loadedServerStatus = false;
        loadedPlayer = false;
        loadedActivePlayerBans = false;

        viewModel.refreshServerStatus();
        viewModel.refreshPlayer();
        viewModel.refreshActivePlayerBans();
    }

    @Override
    public void onResume() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        String username = PreferenceUtils.getUsername(getContext());

        if(username != null){
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
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + username)
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

        String username = PreferenceUtils.getUsername(getContext());
        mGreetingTextView.setText(getString(R.string.greeting, username));

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.app_name);
    }

    private void subscribeToFirebaseRankTopic(Rank rank){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String previous_rank_name = sharedPreferences.getString(getString(R.string.pref_key_rank), null);

        if (!rank.getName().equals(previous_rank_name)) {
            if (previous_rank_name != null) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + TextUtils.normalize(previous_rank_name));
            }
            FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + TextUtils.normalize(rank.getName()));

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.pref_key_rank), rank.getName());
            editor.apply();
        }
    }

    private void checkFirebaseNewReportsTopicSubscription(Rank rank){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (rank != null && rank.isStaff()) {
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

    private void showDefaultLayout(){
        mDefaultLayout.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
    }
    private void showNoInternetLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mServerErrorLayout.setVisibility(View.GONE);
    }
    private void showServerErrorLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void onNoInternet() {
        showNoInternetLayout();
    }

    @Override
    public void onServerError() {
        showServerErrorLayout();
    }
}
