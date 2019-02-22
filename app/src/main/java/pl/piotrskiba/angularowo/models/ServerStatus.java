package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

public class ServerStatus {

    @SerializedName("server_name")
    private final String serverName;
    @SerializedName("player_count")
    private final int playerCount;
    @SerializedName("vanished_player_count")
    private final int vanishedPlayerCount;
    private final int uptime;

    public ServerStatus(String serverName, int playerCount, int vanishedPlayerCount, int uptime){
        this.serverName = serverName;
        this.playerCount = playerCount;
        this.vanishedPlayerCount = vanishedPlayerCount;
        this.uptime = uptime;
    }

    public String getServerName() {
        return serverName;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getVanishedPlayerCount() {
        return vanishedPlayerCount;
    }

    public int getUptime() {
        return uptime;
    }
}
