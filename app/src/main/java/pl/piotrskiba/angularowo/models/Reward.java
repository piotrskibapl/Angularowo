package pl.piotrskiba.angularowo.models;

import android.graphics.Bitmap;

public class Reward {

    private final int imageResource;
    private final String title;
    private final String description;
    private final String adId;

    public Reward(int imageResource, String title, String description, String adId){
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
        this.adId = adId;
    }

    public int getImageResource() {
        return imageResource;
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
