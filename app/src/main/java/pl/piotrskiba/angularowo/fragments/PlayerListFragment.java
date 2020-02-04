package pl.piotrskiba.angularowo.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.AppViewModel;
import pl.piotrskiba.angularowo.Constants;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.SpacesItemDecoration;
import pl.piotrskiba.angularowo.activities.PlayerDetailsActivity;
import pl.piotrskiba.angularowo.adapters.PlayerListAdapter;
import pl.piotrskiba.angularowo.interfaces.PlayerClickListener;
import pl.piotrskiba.angularowo.models.Player;

public class PlayerListFragment extends Fragment implements PlayerClickListener {

    @BindView(R.id.rv_players)
    RecyclerView mPlayerList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_no_players)
    TextView mNoPlayersTextView;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    @BindView(R.id.server_error_layout)
    LinearLayout mServerErrorLayout;

    private PlayerListAdapter mPlayerListAdapter;

    public PlayerListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_list, container, false);

        ButterKnife.bind(this, view);

        mPlayerListAdapter = new PlayerListAdapter(getContext(), this);

        RecyclerView.LayoutManager layoutManager;
        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(getContext());
        }
        else {
            layoutManager = new GridLayoutManager(getContext(), 2);

            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_small);
            mPlayerList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        }

        mPlayerList.setAdapter(mPlayerListAdapter);
        mPlayerList.setLayoutManager(layoutManager);
        mPlayerList.setHasFixedSize(true);

        seekForPlayerList();

        mSwipeRefreshLayout.setOnRefreshListener(() -> refreshData());

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_player_list);

        return view;
    }

    private void seekForPlayerList(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getPlayerList().observe(this, playerList -> {
            if(playerList != null){
                mSwipeRefreshLayout.setRefreshing(false);
                mPlayerListAdapter.setPlayerList(playerList);

            }
            else{
                // TODO: show err
            }
        });
    }

    private void refreshData(){
        mSwipeRefreshLayout.setRefreshing(true);

        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.refreshPlayerList();
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

    private void showDefaultLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mNoPlayersTextView.setVisibility(View.GONE);
        mPlayerList.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
    }
    private void showNoPlayersLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mNoPlayersTextView.setVisibility(View.VISIBLE);
        mPlayerList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
    }
    private void showNoInternetLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mNoPlayersTextView.setVisibility(View.GONE);
        mPlayerList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mServerErrorLayout.setVisibility(View.GONE);
    }
    private void showServerErrorLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mNoPlayersTextView.setVisibility(View.GONE);
        mPlayerList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.VISIBLE);
    }
}
