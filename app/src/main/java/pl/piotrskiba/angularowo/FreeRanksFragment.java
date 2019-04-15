package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    @BindView(R.id.rv_free_ranks)
    RecyclerView mFreeRankList;

    @BindView(R.id.pb_loading)
    ProgressBar mLoadingIndicator;

    private RewardedVideoAd mRewardedVideoAd;

    private InvalidAccessTokenResponseListener listener;

    private ArrayList<Reward> mRewards = new ArrayList<>();

    public FreeRanksFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_ranks, container, false);

        ButterKnife.bind(this, view);

        final FreeRankListAdapter adapter = new FreeRankListAdapter(getContext(), this);
        adapter.setRewardList(mRewards);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mFreeRankList.setAdapter(adapter);
        mFreeRankList.setLayoutManager(layoutManager);
        mFreeRankList.setHasFixedSize(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        showLoadingIndicator();

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getAdCampaigns(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<RewardList>() {
            @Override
            public void onResponse(Call<RewardList> call, Response<RewardList> response) {
                if(isAdded()) {
                    if (response.isSuccessful() && response.body() != null) {
                        RewardList rewardList = response.body();
                        for (Reward reward: rewardList.getRewards()) {
                            mRewards.add(reward);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    else if (response.code() == 401) {
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

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_free_ranks);

        return view;
    }

    @Override
    public void onFreeRankClick(View view, Reward clickedReward) {
        if(mLoadingIndicator.getVisibility() != View.VISIBLE) {
            showLoadingIndicator();
            loadRewardedVideoAd(clickedReward.getAdId());
        }
    }

    private void loadRewardedVideoAd(String adId) {
        mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
    }

    private void showLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
    public void hideLoadingIndicator(){
        mLoadingIndicator.setVisibility(View.GONE);
    }

    public void setRewardedVideoAd(RewardedVideoAd ad){
        mRewardedVideoAd = ad;
    }

    public void setInvalidAccessTokenResponseListener(InvalidAccessTokenResponseListener listener){
        this.listener = listener;
    }
}
