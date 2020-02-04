package pl.piotrskiba.angularowo.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import pl.piotrskiba.angularowo.activities.BanDetailsActivity;
import pl.piotrskiba.angularowo.adapters.BanListAdapter;
import pl.piotrskiba.angularowo.interfaces.BanClickListener;
import pl.piotrskiba.angularowo.models.Ban;

public class BanListFragment extends Fragment implements BanClickListener {

    @BindView(R.id.rv_bans)
    RecyclerView mBanList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    @BindView(R.id.server_error_layout)
    LinearLayout mServerErrorLayout;

    private BanListAdapter mBanListAdapter;

    public BanListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_list, container, false);

        ButterKnife.bind(this, view);

        mBanListAdapter = new BanListAdapter(getContext(), this);

        RecyclerView.LayoutManager layoutManager;
        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(getContext());
        }
        else {
            layoutManager = new GridLayoutManager(getContext(), 2);

            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_small);
            mBanList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        }

        mBanList.setAdapter(mBanListAdapter);
        mBanList.setLayoutManager(layoutManager);
        mBanList.setHasFixedSize(true);

        seekForBanList();

        mSwipeRefreshLayout.setOnRefreshListener(() -> refreshData());

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_ban_list);

        return view;
    }

    private void seekForBanList(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.getBanList().observe(this, banList -> {
            if(banList != null){
                mSwipeRefreshLayout.setRefreshing(false);
                mBanListAdapter.setBanList(banList);
            }
            else{
                // TODO: show err
            }
        });
    }

    private void refreshData(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        mSwipeRefreshLayout.setRefreshing(true);
        viewModel.refreshBanList();
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

    private void showDefaultLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mBanList.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
    }
    private void showNoInternetLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mBanList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mServerErrorLayout.setVisibility(View.GONE);
    }
    private void showServerErrorLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mBanList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.VISIBLE);
    }
}
