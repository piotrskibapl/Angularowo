package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Ban implements Serializable {

    public final static String TYPE_BAN = "Ban";
    public final static String TYPE_MUTE = "Wyciszenie";
    public final static String TYPE_WARNING = "Ostrzeżenie";

    @SerializedName("name")
    private final String username;
    private final String reason;
    private final String banner;
    @SerializedName("bantime")
    private final float banTime;
    @SerializedName("expires")
    private final float expireDate;
    private final String type;

    public Ban(String username, String reason, String banner, float banTime, float expireDate, String type){
        this.username = username;
        this.reason = reason;
        this.banner = banner;
        this.banTime = banTime;
        this.expireDate = expireDate;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }

    public String getBanner() {
        return banner;
    }

    public float getBanTime() {
        return banTime;
    }

    public float getExpireDate() {
        return expireDate;
    }

    public String getType() {
        return type;
    }
}
