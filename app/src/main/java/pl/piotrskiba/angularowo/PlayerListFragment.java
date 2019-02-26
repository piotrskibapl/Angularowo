package pl.piotrskiba.angularowo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.PlayerListAdapter;
import pl.piotrskiba.angularowo.interfaces.PlayerClickListener;
import pl.piotrskiba.angularowo.models.Player;
import pl.piotrskiba.angularowo.models.PlayerList;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerListFragment extends Fragment implements PlayerClickListener {

    @BindView(R.id.rv_players)
    RecyclerView mPlayerList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    public PlayerListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_list, container, false);

        ButterKnife.bind(this, view);

        final PlayerListAdapter adapter = new PlayerListAdapter(getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mPlayerList.setAdapter(adapter);
        mPlayerList.setLayoutManager(layoutManager);
        mPlayerList.setHasFixedSize(true);

        loadPlayerList(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(() -> loadPlayerList(adapter));

        return view;
    }

    private void loadPlayerList(PlayerListAdapter adapter){
        mSwipeRefreshLayout.setRefreshing(true);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayers(ServerAPIClient.API_KEY).enqueue(new Callback<PlayerList>() {
            @Override
            public void onResponse(Call<PlayerList> call, Response<PlayerList> response) {
                if(response.isSuccessful() && response.body() != null) {
                    adapter.setPlayerList(response.body());
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<PlayerList> call, Throwable t) {
                t.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onPlayerClick(View view, Player clickedPlayer) {
        Intent intent = new Intent(getContext(), PlayerDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_PLAYER, clickedPlayer);
        if(getActivity() != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), view, getString(R.string.player_banner_transition_name));
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }
}
