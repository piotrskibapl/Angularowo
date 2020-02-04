package pl.piotrskiba.angularowo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import pl.piotrskiba.angularowo.models.BanList;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.PlayerList;
import pl.piotrskiba.angularowo.models.ServerStatus;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import pl.piotrskiba.angularowo.utils.PreferenceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppViewModel extends AndroidViewModel {

    private MutableLiveData<ServerStatus> serverStatus;
    private MutableLiveData<DetailedPlayer> player;
    private MutableLiveData<BanList> activePlayerBans;
    private MutableLiveData<BanList> banList;
    private MutableLiveData<PlayerList> playerList;

    public AppViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ServerStatus> getServerStatus() {
        if (serverStatus == null) {
            serverStatus = new MutableLiveData<>();
            loadServerStatus();
        }
        return serverStatus;
    }
    public void refreshServerStatus(){
        loadServerStatus();
    }
    private void loadServerStatus(){
        String access_token = PreferenceUtils.getAccessToken(getApplication());

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getServerStatus(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<ServerStatus>() {
            @Override
            public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response) {
                if(response.isSuccessful() && response.body() != null){
                    serverStatus.setValue(response.body());
                }
                else{
                    serverStatus.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ServerStatus> call, Throwable t) {
                serverStatus.setValue(null);
                t.printStackTrace();
            }
        });
    }

    public LiveData<DetailedPlayer> getPlayer() {
        if (player == null) {
            player = new MutableLiveData<>();
            loadPlayer();
        }
        return player;
    }
    public void refreshPlayer(){
        loadPlayer();
    }
    private void loadPlayer(){
        String access_token = PreferenceUtils.getAccessToken(getApplication());
        String username = PreferenceUtils.getUsername(getApplication());

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, username, access_token).enqueue(new Callback<DetailedPlayer>() {

            @Override
            public void onResponse(Call<DetailedPlayer> call, Response<DetailedPlayer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    player.setValue(response.body());
                }
                else {
                    player.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<DetailedPlayer> call, Throwable t) {
                player.setValue(null);
                Log.d("asdasd", call.request().url().toString());
                t.printStackTrace();
            }
        });
    }

    public LiveData<BanList> getActivePlayerBans() {
        if (activePlayerBans == null) {
            activePlayerBans = new MutableLiveData<>();
            loadActivePlayerBans();
        }
        return activePlayerBans;
    }
    public void refreshActivePlayerBans(){
        loadActivePlayerBans();
    }
    private void loadActivePlayerBans(){
        String access_token = PreferenceUtils.getAccessToken(getApplication());
        String username = PreferenceUtils.getUsername(getApplication());

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getActiveBans(ServerAPIClient.API_KEY, Constants.ACTIVE_BAN_TYPES, username, access_token).enqueue(new Callback<BanList>() {
            @Override
            public void onResponse(Call<BanList> call, Response<BanList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activePlayerBans.setValue(response.body());
                }
                else{
                    activePlayerBans.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BanList> call, Throwable t) {
                activePlayerBans.setValue(null);
                t.printStackTrace();
            }
        });
    }

    public LiveData<BanList> getBanList(){
        if(banList == null){
            banList = new MutableLiveData<>();
            loadBanList();
        }
        return banList;
    }
    public void refreshBanList(){
        loadBanList();
    }
    private void loadBanList(){
        String access_token = PreferenceUtils.getAccessToken(getApplication());

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getActiveBans(ServerAPIClient.API_KEY, Constants.BAN_TYPES, null, access_token).enqueue(new Callback<BanList>() {
            @Override
            public void onResponse(Call<BanList> call, Response<BanList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    banList.setValue(response.body());
                }
                else {
                    banList.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BanList> call, Throwable t) {
                banList.setValue(null);
                t.printStackTrace();
            }
        });
    }

    public LiveData<PlayerList> getPlayerList(){
        if(playerList == null){
            playerList = new MutableLiveData<>();
            loadPlayerList();
        }
        return playerList;
    }
    public void refreshPlayerList(){
        loadPlayerList();
    }
    private void loadPlayerList(){
        String access_token = PreferenceUtils.getAccessToken(getApplication());

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.getPlayers(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<PlayerList>() {
            @Override
            public void onResponse(Call<PlayerList> call, Response<PlayerList> response) {
                if (response.isSuccessful() && response.body() != null) {
                    playerList.setValue(response.body());
                }
                else {
                    playerList.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PlayerList> call, Throwable t) {
                playerList.setValue(null);
                t.printStackTrace();
            }
        });
    }
}
