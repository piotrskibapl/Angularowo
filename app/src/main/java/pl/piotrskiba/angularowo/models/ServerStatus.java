package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

public class ServerStatus {

    @SerializedName("player_count")
    private final int playerCount;

    @SerializedName("tps_5")
    private final double tps;

    public ServerStatus(int playerCount, double tps){
        this.playerCount = playerCount;
        this.tps = tps;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public double getTps() {
        return tps;
    }
}
