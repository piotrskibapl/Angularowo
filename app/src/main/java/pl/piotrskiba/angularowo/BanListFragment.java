package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.BanListAdapter;
import pl.piotrskiba.angularowo.interfaces.InvalidAccessTokenResponseListener;
import pl.piotrskiba.angularowo.models.BanList;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanListFragment extends Fragment {

    @BindView(R.id.rv_bans)
    RecyclerView mBanList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private InvalidAccessTokenResponseListener listener;

    public BanListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_list, container, false);

        ButterKnife.bind(this, view);

        final BanListAdapter adapter = new BanListAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBanList.setAdapter(adapter);
        mBanList.setLayoutManager(layoutManager);
        mBanList.setHasFixedSize(true);

        loadBanList(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(() -> loadBanList(adapter));

        return view;
    }

    private void loadBanList(BanListAdapter adapter){
        mSwipeRefreshLayout.setRefreshing(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getBanList(ServerAPIClient.API_KEY, Constants.BAN_TYPES, access_token).enqueue(new Callback<BanList>() {
            @Override
            public void onResponse(Call<BanList> call, Response<BanList> response) {
                if(response.isSuccessful() && response.body() != null) {
                    adapter.setBanList(response.body());
                }
                else if(response.code() == 401){
                    listener.onInvalidAccessTokenResponseReceived();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<BanList> call, Throwable t) {
                t.printStackTrace();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void setInvalidAccessTokenResponseListener(InvalidAccessTokenResponseListener listener){
        this.listener = listener;
    }
}
