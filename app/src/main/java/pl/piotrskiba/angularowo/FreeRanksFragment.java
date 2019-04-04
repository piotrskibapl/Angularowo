package pl.piotrskiba.angularowo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.FreeRankListAdapter;
import pl.piotrskiba.angularowo.interfaces.FreeRankClickListener;
import pl.piotrskiba.angularowo.models.Reward;

public class FreeRanksFragment extends Fragment implements FreeRankClickListener {

    @BindView(R.id.rv_free_ranks)
    RecyclerView mFreeRankList;

    private RewardedVideoAd mRewardedVideoAd;

    public FreeRanksFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_ranks, container, false);

        ButterKnife.bind(this, view);

        final FreeRankListAdapter adapter = new FreeRankListAdapter(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mFreeRankList.setAdapter(adapter);
        mFreeRankList.setLayoutManager(layoutManager);
        mFreeRankList.setHasFixedSize(true);

        ArrayList<Reward> list = new ArrayList<>();

        list.add(new Reward(R.drawable.default_avatar, "Chłopak", "Ranga Chłopak na 3 dni", "ca-app-pub-7790074991647252/6832708642"));
        list.add(new Reward(R.drawable.default_avatar_female, "Dziewczyna", "Ranga Dziewczyna na 3 dni", "ca-app-pub-7790074991647252/7392015807"));

        adapter.setRewardList(list);

        return view;
    }

    @Override
    public void onFreeRankClick(View view, Reward clickedReward) {
        loadRewardedVideoAd(clickedReward.getAdId());
    }

    private void loadRewardedVideoAd(String adId) {
        mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
    }

    public void setRewardedVideoAd(RewardedVideoAd ad){
        mRewardedVideoAd = ad;
    }
}
