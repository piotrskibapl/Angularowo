package pl.piotrskiba.angularowo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.PlayerListAdapter;
import pl.piotrskiba.angularowo.models.PlayerList;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_players)
    RecyclerView mPlayerList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mPlayerList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mPlayerList.setLayoutManager(layoutManager);

        final PlayerListAdapter adapter = new PlayerListAdapter(this);
        mPlayerList.setAdapter(adapter);

        loadPlayerList(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(() -> loadPlayerList(adapter));
    }

    private void loadPlayerList(PlayerListAdapter adapter){
        mSwipeRefreshLayout.setRefreshing(true);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayers(ServerAPIClient.API_KEY).enqueue(new Callback<PlayerList>() {
            @Override
            public void onResponse(Call<PlayerList> call, Response<PlayerList> response) {
                if(response.isSuccessful()) {
                    adapter.setPlayerList(response.body());
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<PlayerList> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
