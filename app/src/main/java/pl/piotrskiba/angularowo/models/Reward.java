package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

public class Reward {

    @SerializedName("image")
    private final String imageUrl;
    private final String title;
    private final String description;
    private final String adId;

    public Reward(String imageUrl, String title, String description, String adId){
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.adId = adId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAdId() {
        return adId;
    }
}
