package pl.piotrskiba.angularowo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.Player;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import pl.piotrskiba.angularowo.utils.RankUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PlayerDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.iv_player_body)
    ImageView mPlayerBodyImageView;

    @BindView(R.id.tv_balance)
    TextView mPlayerBalanceTextView;

    @BindView(R.id.tv_islandlevel)
    TextView mPlayerIslandLevelTextView;

    // player_list_item
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.iv_player_avatar)
    ImageView mPlayerAvatar;

    @BindView(R.id.tv_player_name)
    TextView mPlayerName;

    @BindView(R.id.tv_player_rank)
    TextView mPlayerRank;

    @BindView(R.id.iv_vanish_status)
    ImageView mPlayerVanishIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.player_info);
        }

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(Constants.EXTRA_PLAYER)){
            Player player = (Player) parentIntent.getSerializableExtra(Constants.EXTRA_PLAYER);
            populatePlayer(player);
            loadDetailedPlayerData(player);
        }

        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    private void loadDetailedPlayerData(Player player){
        mSwipeRefreshLayout.setRefreshing(true);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, player.getUsername()).enqueue(new Callback<DetailedPlayer>() {

            @Override
            public void onResponse(Call<DetailedPlayer> call, Response<DetailedPlayer> response) {
                if(response.isSuccessful() && response.body() != null){
                    DetailedPlayer player = response.body();

                    populatePlayer(player);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DetailedPlayer> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void populatePlayer(Player player){
        if(player.getUuid() != null) {
            Glide.with(this)
                    .load("https://crafatar.com/avatars/" + player.getUuid() + "?size=100")
                    .into(mPlayerAvatar);

            Glide.with(this)
                    .load(Constants.BASE_BODY_URL + player.getUuid())
                    .into(mPlayerBodyImageView);
        }
        else{
            mPlayerAvatar.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
        }

        mPlayerName.setText(player.getUsername());
        mPlayerRank.setText(player.getRank());

        int color_id = RankUtils.getRankColorId(player.getRank());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ConstraintLayout) mPlayerAvatar.getParent()).setBackground(getResources().getDrawable(color_id));
        }
        else{
            ((ConstraintLayout) mPlayerAvatar.getParent()).setBackgroundDrawable(getResources().getDrawable(color_id));
        }

        if(player.isVanished())
            mPlayerVanishIcon.setVisibility(View.VISIBLE);
        else
            mPlayerVanishIcon.setVisibility(View.INVISIBLE);
    }

    private void populatePlayer(DetailedPlayer detailedPlayer){
        Player player = new Player(detailedPlayer.getUsername(), detailedPlayer.getUuid(),
                detailedPlayer.getRank(), detailedPlayer.isVanished());
        populatePlayer(player);

        mPlayerBalanceTextView.setText(getString(R.string.balance_format, (int)detailedPlayer.getBalance()));
        mPlayerIslandLevelTextView.setText(String.valueOf(detailedPlayer.getIslandLevel()));
    }

    @Override
    public void onRefresh() {
        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(Constants.EXTRA_PLAYER)){
            Player player = (Player) parentIntent.getSerializableExtra(Constants.EXTRA_PLAYER);
            loadDetailedPlayerData(player);
        }
    }
}
