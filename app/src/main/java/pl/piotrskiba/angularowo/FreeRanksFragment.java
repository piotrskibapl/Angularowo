package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.FreeRankListAdapter;
import pl.piotrskiba.angularowo.interfaces.FreeRankClickListener;
import pl.piotrskiba.angularowo.models.Reward;
import pl.piotrskiba.angularowo.models.RewardList;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FreeRanksFragment extends Fragment implements FreeRankClickListener {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.rv_free_ranks)
    RecyclerView mFreeRankList;

    @BindView(R.id.noRanksAvailableTextView)
    TextView mNoRanksAvailableTextView;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    @BindView(R.id.account_banned_layout)
    LinearLayout mAccountBannedLayout;

    private RewardedVideoAd mRewardedVideoAd;

    private ArrayList<Reward> mRewards = new ArrayList<>();

    private FreeRankListAdapter mFreeRankListAdapter;

    public FreeRanksFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_ranks, container, false);

        ButterKnife.bind(this, view);

        mFreeRankListAdapter = new FreeRankListAdapter(getContext(), this);
        mFreeRankListAdapter.setRewardList(mRewards);

        RecyclerView.LayoutManager layoutManager;
        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(getContext(), 2);
        }
        else{
            layoutManager = new GridLayoutManager(getContext(), 4);
        }
        mFreeRankList.setAdapter(mFreeRankListAdapter);
        mFreeRankList.setLayoutManager(layoutManager);
        mFreeRankList.setHasFixedSize(true);

        loadRewards();

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_free_ranks);

        mSwipeRefreshLayout.setOnRefreshListener(() -> loadRewards());

        return view;
    }

    @Override
    public void onFreeRankClick(View view, Reward clickedReward) {
        if(getContext() != null && !mSwipeRefreshLayout.isRefreshing()) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.ad_question)
                    .setMessage(R.string.ad_question_description)
                    .setPositiveButton(R.string.button_yes, (dialogInterface, i) -> {
                        showLoadingIndicator();
                        loadRewardedVideoAd(clickedReward.getAdId());
                    })
                    .setNegativeButton(R.string.button_no, null)
                    .show();
        }
    }

    public void loadRewards(){
        if(isAdded()) {
            showLoadingIndicator();

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

            ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
            serverAPIInterface.getAdCampaigns(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<RewardList>() {
                @Override
                public void onResponse(Call<RewardList> call, Response<RewardList> response) {
                    if (isAdded()) {
                        if (response.isSuccessful() && response.body() != null) {
                            RewardList rewardList = response.body();
                            if (rewardList.getRewards() != null && rewardList.getRewards().size() > 0) {
                                showDefaultLayout();
                                mRewards.clear();
                                mRewards.addAll(rewardList.getRewards());
                                mFreeRankListAdapter.notifyDataSetChanged();
                            } else {
                                showNoRewardsLayout();
                            }
                        }
                        else if(response.code() == 403){
                            showAccoutBannedLayout();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RewardList> call, Throwable t) {
                    showNoInternetLayout();
                    t.printStackTrace();
                }
            });
        }
    }

    private void loadRewardedVideoAd(String adId) {
        mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
    }

    private void showDefaultLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.GONE);
        mFreeRankList.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
        mAccountBannedLayout.setVisibility(View.GONE);
    }
    private void showNoRewardsLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.VISIBLE);
        mFreeRankList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mAccountBannedLayout.setVisibility(View.GONE);
    }
    private void showNoInternetLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.GONE);
        mFreeRankList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mAccountBannedLayout.setVisibility(View.GONE);
    }
    private void showAccoutBannedLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.GONE);
        mFreeRankList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mAccountBannedLayout.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator(){
        mSwipeRefreshLayout.setRefreshing(true);
    }
    public void hideLoadingIndicator(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setRewardedVideoAd(RewardedVideoAd ad){
        mRewardedVideoAd = ad;
    }
}
