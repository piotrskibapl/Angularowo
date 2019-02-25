package pl.piotrskiba.angularowo.models;

import java.io.Serializable;

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

    public String getRank(){
        return rank;
    }

    public boolean isVanished(){
        return vanished;
    }
}
