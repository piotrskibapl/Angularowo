package pl.piotrskiba.angularowo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.google.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.interfaces.InvalidAccessTokenResponseListener;

public class MainActivity extends AppCompatActivity implements InvalidAccessTokenResponseListener, RewardedVideoAdListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.nav_view)
    NavigationView mNavigationView;

    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        MobileAds.initialize(this, Constants.ADMOB_APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPreferences.contains(getString(R.string.pref_key_access_token))){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER);
        }
        else {
            populateUi();
        }
    }

    private void populateUi(){
        MainScreenFragment mainScreenFragment = new MainScreenFragment();
        mainScreenFragment.setInvalidAccessTokenResponseListener(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mainScreenFragment)
                .commit();

        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mNavigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    if(!menuItem.isChecked()) {
                        if (menuItem.getItemId() == R.id.nav_main_screen) {
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, mainScreenFragment)
                                    .commit();
                        }
                        else if(menuItem.getItemId() == R.id.nav_player_list){
                            PlayerListFragment playerListFragment = new PlayerListFragment();
                            playerListFragment.setInvalidAccessTokenResponseListener(this);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, playerListFragment)
                                    .commit();
                        } else if (menuItem.getItemId() == R.id.nav_last_bans) {
                            BanListFragment banListFragment = new BanListFragment();
                            banListFragment.setInvalidAccessTokenResponseListener(this);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, banListFragment)
                                    .commit();
                        } else if (menuItem.getItemId() == R.id.nav_free_ranks) {
                            FreeRanksFragment freeRanksFragment = new FreeRanksFragment();
                            freeRanksFragment.setRewardedVideoAd(mRewardedVideoAd);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, freeRanksFragment)
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
    public void onInvalidAccessTokenResponseReceived() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, Constants.REQUEST_CODE_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Constants.REQUEST_CODE_REGISTER){
            if(resultCode == Constants.RESULT_CODE_SUCCESS){
                populateUi();
            }
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
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

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        if(i == 3) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.no_ads)
                    .setMessage(R.string.no_ads_description)

                    .setPositiveButton(R.string.button_dismiss, null)
                    .show();
        }
    }

    @Override
    public void onRewardedVideoCompleted() {

    }
}
