package pl.piotrskiba.angularowo.models;

import java.util.List;

public class PlayerList {
    private final List<Player> players;

    public PlayerList(List<Player> players){
        this.players = players;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
