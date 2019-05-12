package pl.piotrskiba.angularowo.models;

public class ChatMessage {

    private final String username;
    private final String rank;
    private final String message;

    public ChatMessage(String username, String rank, String message){
        this.username = username;
        this.rank = rank;
        this.message = message;
    }

    public String getUsername(){
        return username;
    }

    public String getRank(){
        return rank;
    }

    public String getMessage(){
        return message;
    }
}
