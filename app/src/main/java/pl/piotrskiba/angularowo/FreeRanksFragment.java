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
import pl.piotrskiba.angularowo.interfaces.InvalidAccessTokenResponseListener;
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

    @BindView(R.id.errorTextView)
    TextView mErrorTextView;

    private RewardedVideoAd mRewardedVideoAd;

    private InvalidAccessTokenResponseListener listener;

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

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
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
                                for (Reward reward : rewardList.getRewards()) {
                                    mRewards.add(reward);
                                }
                                mFreeRankListAdapter.notifyDataSetChanged();
                            } else {
                                showNoRewardsLayout();
                            }
                        } else if (response.code() == 401) {
                            listener.onInvalidAccessTokenResponseReceived();
                        }
                        hideLoadingIndicator();
                    }
                }

                @Override
                public void onFailure(Call<RewardList> call, Throwable t) {
                    hideLoadingIndicator();
                }
            });
        }
    }

    private void loadRewardedVideoAd(String adId) {
        mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
    }

    private void showDefaultLayout(){
        mErrorTextView.setVisibility(View.GONE);
        mFreeRankList.setVisibility(View.VISIBLE);
    }
    private void showNoRewardsLayout(){
        mErrorTextView.setVisibility(View.VISIBLE);
        mFreeRankList.setVisibility(View.GONE);
        mErrorTextView.setText(R.string.no_rewards_found);
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

    public void setInvalidAccessTokenResponseListener(InvalidAccessTokenResponseListener listener){
        this.listener = listener;
    }
}
