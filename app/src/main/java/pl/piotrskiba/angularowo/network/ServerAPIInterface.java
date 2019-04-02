package pl.piotrskiba.angularowo.network;

import pl.piotrskiba.angularowo.models.AccessToken;
import pl.piotrskiba.angularowo.models.BanList;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.PlayerList;
import pl.piotrskiba.angularowo.models.ServerStatus;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerAPIInterface {

    @GET("get_server_status.php")
    Call<ServerStatus> getServerStatus(@Query("api_key") String api_key, @Query("access_token") String access_token);

    @GET("get_online_players.php")
    Call<PlayerList> getPlayers(@Query("api_key") String api_key, @Query("access_token") String access_token);

    @GET("get_ban_list.php")
    Call<BanList> getBanList(@Query("api_key") String api_key, @Query("type") String type, @Query("username") String username, @Query("access_token") String access_token);

    @GET("register_device.php")
    Call<AccessToken> registerDevice(@Query("api_key") String api_key, @Query("user_code") String user_code);

    @GET("get_player_info.php")
    Call<DetailedPlayer> getPlayerInfo(@Query("api_key") String api_key, @Query("username") String username, @Query("access_token") String access_token);
}
