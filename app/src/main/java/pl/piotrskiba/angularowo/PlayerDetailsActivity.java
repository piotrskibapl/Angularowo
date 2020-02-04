package pl.piotrskiba.angularowo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.Player;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import pl.piotrskiba.angularowo.utils.ColorUtils;
import pl.piotrskiba.angularowo.utils.GlideUtils;
import pl.piotrskiba.angularowo.utils.TextUtils;
import pl.piotrskiba.angularowo.utils.UrlUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.iv_player_body)
    ImageView mPlayerBodyImageView;

    @BindView(R.id.tv_balance)
    TextView mPlayerBalanceTextView;

    @BindView(R.id.tv_tokens)
    TextView mPlayerTokensTextView;

    @BindView(R.id.tv_playtime)
    TextView mPlayerPlayTimeTextView;

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

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, player.getUsername(), access_token).enqueue(new Callback<DetailedPlayer>() {

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
                t.printStackTrace();
            }
        });
    }

    private void populatePlayer(Player player){
        if(!isFinishing()) {
            if (player.getUuid() != null) {
                Glide.with(this)
                        .load(UrlUtils.buildAvatarUrl(player.getUuid(), true))
                        .signature(new IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(5)))
                        .placeholder(R.drawable.default_body)
                        .into(mPlayerAvatar);

                Glide.with(this)
                        .load(UrlUtils.buildBodyUrl(player.getUuid(), true))
                        .signature(new IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(5)))
                        .placeholder(R.drawable.default_body)
                        .into(mPlayerBodyImageView);
            } else {
                mPlayerAvatar.setImageDrawable(getResources().getDrawable(R.drawable.default_avatar));
            }

            mPlayerName.setText(player.getUsername());
            mPlayerRank.setText(player.getRank().getName());

            int color = ColorUtils.getColorFromCode(this, player.getRank().getColorCode());
            ((ConstraintLayout) mPlayerAvatar.getParent()).setBackgroundColor(color);

            if (player.isVanished())
                mPlayerVanishIcon.setVisibility(View.VISIBLE);
            else
                mPlayerVanishIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void populatePlayer(DetailedPlayer detailedPlayer){
        Player player = new Player(detailedPlayer.getUsername(), detailedPlayer.getUuid(),
                detailedPlayer.getRank().getName(), detailedPlayer.isVanished());
        populatePlayer(player);

        mPlayerBalanceTextView.setText(getString(R.string.balance_format, (int)detailedPlayer.getBalance()));
        mPlayerTokensTextView.setText(String.valueOf(detailedPlayer.getTokens()));
        mPlayerPlayTimeTextView.setText(TextUtils.formatPlaytime(this, detailedPlayer.getPlaytime()));
    }

    @Override
    public void onRefresh() {
        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(Constants.EXTRA_PLAYER)){
            Player player = (Player) parentIntent.getSerializableExtra(Constants.EXTRA_PLAYER);
            loadDetailedPlayerData(player);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
