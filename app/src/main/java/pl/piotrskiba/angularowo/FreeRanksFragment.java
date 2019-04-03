package pl.piotrskiba.angularowo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.FreeRankListAdapter;
import pl.piotrskiba.angularowo.models.Reward;

public class FreeRanksFragment extends Fragment {

    @BindView(R.id.rv_free_ranks)
    RecyclerView mFreeRankList;

    public FreeRanksFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_ranks, container, false);

        ButterKnife.bind(this, view);

        final FreeRankListAdapter adapter = new FreeRankListAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mFreeRankList.setAdapter(adapter);
        mFreeRankList.setLayoutManager(layoutManager);
        mFreeRankList.setHasFixedSize(true);

        ArrayList<Reward> list = new ArrayList<>();

        list.add(new Reward(R.drawable.default_avatar, "Chłopak", "Ranga Chłopak na 3 dni"));
        list.add(new Reward(R.drawable.default_avatar_female, "Dziewczyna", "Ranga Dziewczyna na 3 dni"));

        adapter.setRewardList(list);

        return view;
    }
}
