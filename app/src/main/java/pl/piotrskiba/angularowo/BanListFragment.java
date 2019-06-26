package pl.piotrskiba.angularowo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.BanListAdapter;
import pl.piotrskiba.angularowo.interfaces.BanClickListener;
import pl.piotrskiba.angularowo.interfaces.InvalidAccessTokenResponseListener;
import pl.piotrskiba.angularowo.models.Ban;
import pl.piotrskiba.angularowo.models.BanList;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanListFragment extends Fragment implements BanClickListener {

    @BindView(R.id.rv_bans)
    RecyclerView mBanList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    private InvalidAccessTokenResponseListener listener;

    public BanListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_list, container, false);

        ButterKnife.bind(this, view);

        final BanListAdapter adapter = new BanListAdapter(getContext(), this);

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

        mBanList.setAdapter(adapter);
        mBanList.setLayoutManager(layoutManager);
        mBanList.setHasFixedSize(true);

        loadBanList(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(() -> loadBanList(adapter));

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_ban_list);

        return view;
    }

    private void loadBanList(BanListAdapter adapter){
        mSwipeRefreshLayout.setRefreshing(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getBanList(ServerAPIClient.API_KEY, Constants.BAN_TYPES, null, access_token).enqueue(new Callback<BanList>() {
            @Override
            public void onResponse(Call<BanList> call, Response<BanList> response) {
                if(isAdded()) {
                    showDefaultLayout();

                    if (response.isSuccessful() && response.body() != null) {
                        adapter.setBanList(response.body());
                    } else if (response.code() == 401) {
                        listener.onInvalidAccessTokenResponseReceived();
                    }
                }
            }

            @Override
            public void onFailure(Call<BanList> call, Throwable t) {
                if(isAdded()) {
                    showNoInternetLayout();
                }
            }
        });
    }

    public void setInvalidAccessTokenResponseListener(InvalidAccessTokenResponseListener listener){
        this.listener = listener;
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
    }
    private void showNoInternetLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mBanList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
    }
}
