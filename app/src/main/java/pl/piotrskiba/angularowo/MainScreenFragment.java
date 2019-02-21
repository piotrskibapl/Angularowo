package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreenFragment extends Fragment {

    private final static String BASE_BODY_URL = "https://crafatar.com/renders/body/";

    @BindView(R.id.tv_greeting)
    TextView mGreetingTextView;

    @BindView(R.id.iv_player_body)
    ImageView mPlayerBodyImageView;

    private String username;

    public MainScreenFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);

        if(username != null){
            populateUi();
        }

        return view;
    }

    @Override
    public void onResume() {
        if(username == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            String username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);
            if(username != null){
                this.username = username;
                populateUi();
            }
        }

        super.onResume();
    }

    private void populateUi(){
        mGreetingTextView.setText(getString(R.string.greeting, username));

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, username).enqueue(new Callback<DetailedPlayer>() {

            @Override
            public void onResponse(Call<DetailedPlayer> call, Response<DetailedPlayer> response) {
                if(response.isSuccessful() && response.body() != null){
                    DetailedPlayer player = response.body();

                    if(player.getUuid() != null && getContext() != null) {
                        Glide.with(getContext())
                                .load(BASE_BODY_URL + player.getUuid())
                                .into(mPlayerBodyImageView);
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailedPlayer> call, Throwable t) {

            }
        });
    }
}
