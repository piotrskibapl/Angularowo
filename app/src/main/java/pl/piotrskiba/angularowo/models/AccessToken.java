package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

public class AccessToken {

    private final String username;
    @SerializedName("access_token")
    private final String accessToken;
    private final String message;

    public AccessToken(String username, String accessToken, String message){
        this.username = username;
        this.accessToken = accessToken;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getMessage() {
        return message;
    }
}
