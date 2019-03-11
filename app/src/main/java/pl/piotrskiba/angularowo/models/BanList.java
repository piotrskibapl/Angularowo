package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BanList {

    @SerializedName("banlist")
    private final List<Ban> banList;

    public BanList(List<Ban> banList){
        this.banList = banList;
    }

    public List<Ban> getBanList(){
        return banList;
    }
}
