package pl.piotrskiba.angularowo.models;

import java.io.Serializable;

import pl.piotrskiba.angularowo.utils.RankUtils;

public class Player implements Serializable {

    private final String username;
    private final String uuid;
    private final String rank;
    private final boolean vanished;

    public Player(String username, String uuid, String rank, boolean vanished){
        this.username = username;
        this.uuid = uuid;
        this.rank = rank;
        this.vanished = vanished;
    }

    public String getUsername(){
        return username;
    }

    public String getUuid(){
        return uuid;
    }

    public Rank getRank(){
        return RankUtils.getRankFromName(rank);
    }

    public boolean isVanished(){
        return vanished;
    }
}
