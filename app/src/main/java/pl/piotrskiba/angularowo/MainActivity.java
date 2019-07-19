package pl.piotrskiba.angularowo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.multidex.MultiDex;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.interfaces.UnauthorizedResponseListener;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import pl.piotrskiba.angularowo.network.UnauthorizedInterceptor;
import pl.piotrskiba.angularowo.utils.NotificationUtils;
import pl.piotrskiba.angularowo.utils.RankUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements UnauthorizedResponseListener, RewardedVideoAdListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private MainScreenFragment mainScreenFragment;
    private PlayerListFragment playerListFragment;
    private ChatFragment chatFragment;
    private BanListFragment banListFragment;
    private FreeRanksFragment freeRanksFragment;
    private Fragment reportsHistoryFragment;

    private final static String TAG_MAIN_FRAGMENT = "fragment_main";
    private final static String TAG_PLAYER_LIST_FRAGMENT = "fragment_player_list";
    private final static String TAG_CHAT_FRAGMENT = "fragment_chat";
    private final static String TAG_BAN_LIST_FRAGMENT = "fragment_ban_list";
    private final static String TAG_FREE_RANKS_FRAGMENT = "fragment_free_ranks";
    private final static String TAG_REPORTS_HISTORY_FRAGMENT = "fragment_reports_history";

    private RewardedVideoAd mRewardedVideoAd;

    private boolean waitingForLogin = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        new NotificationUtils(this).createNotificationChannels();

        setupRemoteConfig();

        MobileAds.initialize(this, Constants.ADMOB_APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        UnauthorizedInterceptor.setUnauthorizedListener(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPreferences.contains(getString(R.string.pref_key_access_token))){
            if(savedInstanceState == null)
                setupMainFragment();

            populateUi();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!waitingForLogin) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (!sharedPreferences.contains(getString(R.string.pref_key_access_token))) {
                waitingForLogin = true;
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER);
            }
        }
    }

    private void setupRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_default_values);

        mFirebaseRemoteConfig.fetch(60*30)
                .addOnCompleteListener(this, task -> mFirebaseRemoteConfig.activate().addOnCompleteListener(task1 -> onRemoteConfigLoaded()));
    }

    private void onRemoteConfigLoaded(){
        long start = mFirebaseRemoteConfig.getLong(Constants.REMOTE_CONFIG_APP_LOCK_START_TIMESTAMP);
        if(start*1000 <= System.currentTimeMillis()) {
            long end = mFirebaseRemoteConfig.getLong(Constants.REMOTE_CONFIG_APP_LOCK_END_TIMESTAMP);
            if(end*1000 > System.currentTimeMillis()) {
                Intent intent = new Intent(this, ApplicationLockedActivity.class);
                startActivity(intent);
            }
        }
    }

    private void setupMainFragment(){
        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mainScreenFragment, TAG_MAIN_FRAGMENT)
                .commit();
    }

    private void initializeFragments(){
        FragmentManager fragmentManager = getSupportFragmentManager();

        mainScreenFragment = (MainScreenFragment) fragmentManager.findFragmentByTag(TAG_MAIN_FRAGMENT);
        playerListFragment = (PlayerListFragment) fragmentManager.findFragmentByTag(TAG_PLAYER_LIST_FRAGMENT);
        chatFragment = (ChatFragment) fragmentManager.findFragmentByTag(TAG_CHAT_FRAGMENT);
        banListFragment = (BanListFragment) fragmentManager.findFragmentByTag(TAG_BAN_LIST_FRAGMENT);
        freeRanksFragment = (FreeRanksFragment) fragmentManager.findFragmentByTag(TAG_FREE_RANKS_FRAGMENT);
        reportsHistoryFragment = fragmentManager.findFragmentByTag(TAG_REPORTS_HISTORY_FRAGMENT);

        if(mainScreenFragment == null) {
            mainScreenFragment = new MainScreenFragment();
        }

        if(playerListFragment == null) {
            playerListFragment = new PlayerListFragment();
        }

        if(chatFragment == null) {
            chatFragment = new ChatFragment();
        }

        if(banListFragment == null) {
            banListFragment = new BanListFragment();
        }

        if(freeRanksFragment == null) {
            freeRanksFragment = new FreeRanksFragment();
        }
        freeRanksFragment.setRewardedVideoAd(mRewardedVideoAd);

        if (reportsHistoryFragment == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            if (RankUtils.isStaffRank(sharedPreferences.getString(getString(R.string.pref_key_rank), null))) {
                reportsHistoryFragment = new ReportsHistoryFragmentContainerFragment();
            } else {
                reportsHistoryFragment = new ReportsHistoryFragment(false);
            }
        }
    }

    private void populateUi(){
        initializeFragments();

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mNavigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    if(!menuItem.isChecked()) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        if (menuItem.getItemId() == R.id.nav_main_screen) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, mainScreenFragment, TAG_MAIN_FRAGMENT)
                                    .commit();
                        }
                        else if(menuItem.getItemId() == R.id.nav_player_list){
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, playerListFragment, TAG_PLAYER_LIST_FRAGMENT)
                                    .commit();
                        } else if (menuItem.getItemId() == R.id.nav_chat) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, chatFragment, TAG_CHAT_FRAGMENT)
                                    .commit();
                        } else if (menuItem.getItemId() == R.id.nav_last_bans) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, banListFragment, TAG_BAN_LIST_FRAGMENT)
                                    .commit();
                        } else if (menuItem.getItemId() == R.id.nav_free_ranks) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, freeRanksFragment, TAG_FREE_RANKS_FRAGMENT)
                                    .commit();
                        } else if (menuItem.getItemId() == R.id.nav_reports_history) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, reportsHistoryFragment, TAG_REPORTS_HISTORY_FRAGMENT)
                                    .commit();
                        } else if (menuItem.getItemId() == R.id.nav_settings) {
                            Intent intent = new Intent(this, SettingsActivity.class);
                            startActivity(intent);
                        }
                    }
                    mDrawerLayout.closeDrawers();

                    return true;
                });
        mNavigationView.setCheckedItem(R.id.nav_main_screen);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUnauthorizedResponse() {
        if(!waitingForLogin) {
            waitingForLogin = true;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE_REGISTER){
            waitingForLogin = false;
            if(resultCode == Constants.RESULT_CODE_SUCCESS){
                setupMainFragment();
                populateUi();
            }
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        freeRanksFragment.hideLoadingIndicator();
        mRewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        Context context = this;

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.redeemAdPrize(ServerAPIClient.API_KEY, rewardItem.getType(), access_token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!isFinishing()) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.prize_redeemed)
                            .setMessage(R.string.prize_redeemed_description)

                            .setPositiveButton(R.string.button_dismiss, null)
                            .show();

                    freeRanksFragment.loadRewards();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        if(!isFinishing()) {
            freeRanksFragment.hideLoadingIndicator();
            if (i == 3) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.no_ads)
                        .setMessage(R.string.no_ads_description)

                        .setPositiveButton(R.string.button_dismiss, null)
                        .show();
            }
        }
    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
