package pl.piotrskiba.angularowo.models;

public class DetailedPlayer {
    private final String username;
    private final String uuid;
    private final String status;
    private final String rank;
    private final boolean vanished;
    private final float balance;
    private final int playtime;
    private final int tokens;

    public DetailedPlayer(String username, String uuid, String status, String rank, boolean vanished, float balance, int playtime, int tokens){
        this.username = username;
        this.uuid = uuid;
        this.status = status;
        this.rank = rank;
        this.vanished = vanished;
        this.balance = balance;
        this.playtime = playtime;
        this.tokens = tokens;
    }

    public String getUsername(){
        return username;
    }

    public String getUuid(){
        return uuid;
    }

    public String getStatus() {
        return status;
    }

    public String getRank(){
        return rank;
    }

    public boolean isVanished(){
        return vanished;
    }

    public float getBalance() {
        return balance;
    }

    public int getPlaytime() {
        return playtime;
    }

    public int getTokens() {
        return tokens;
    }
}
