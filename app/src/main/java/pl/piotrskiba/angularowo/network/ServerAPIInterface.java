package pl.piotrskiba.angularowo.network;

import pl.piotrskiba.angularowo.models.PlayerList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerAPIInterface {

    @GET("get_online_players.php")
    Call<PlayerList> getPlayers(@Query("api_key") String api_key);
}
