package pl.piotrskiba.angularowo.network;

import pl.piotrskiba.angularowo.models.AccessToken;
import pl.piotrskiba.angularowo.models.BanList;
import pl.piotrskiba.angularowo.models.ChatMessageList;
import pl.piotrskiba.angularowo.models.DetailedPlayer;
import pl.piotrskiba.angularowo.models.PlayerList;
import pl.piotrskiba.angularowo.models.ReportList;
import pl.piotrskiba.angularowo.models.RewardList;
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
    Call<BanList> getAllBans(@Query("api_key") String api_key, @Query("type") String type, @Query("username") String username, @Query("access_token") String access_token);

    @GET("get_ban_list.php?filter=active")
    Call<BanList> getActiveBans(@Query("api_key") String api_key, @Query("type") String type, @Query("username") String username, @Query("access_token") String access_token);

    @GET("register_device.php")
    Call<AccessToken> registerDevice(@Query("api_key") String api_key, @Query("user_code") String user_code);

    @GET("get_player_info.php")
    Call<DetailedPlayer> getPlayerInfo(@Query("api_key") String api_key, @Query("username") String username, @Query("access_token") String access_token);

    @GET("get_ad_campaigns.php")
    Call<RewardList> getAdCampaigns(@Query("api_key") String api_key, @Query("access_token") String access_token);

    @GET("redeem_ad_prize.php")
    Call<Void> redeemAdPrize(@Query("api_key") String api_key, @Query("prize") String prize, @Query("access_token") String access_token);

    @GET("get_last_chat_messages.php")
    Call<ChatMessageList> getLastChatMessages(@Query("api_key") String api_key, @Query("access_token") String access_token);

    @GET("get_reports.php?own")
    Call<ReportList> getUserReports(@Query("api_key") String api_key, @Query("access_token") String access_token);

    @GET("get_reports.php")
    Call<ReportList> getAllReports(@Query("api_key") String api_key, @Query("archived") boolean archived, @Query("access_token") String access_token);
}
