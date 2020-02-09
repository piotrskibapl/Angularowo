package pl.piotrskiba.angularowo.models;

public class Offer {

    private final String id;
    private final String title;
    private final String description;
    private final int price;
    private final String imageUrl;
    private final int timeBreak;
    private final int timeleft;

    public Offer(String id, String title, String description, int price, String imageUrl, int timeBreak, int timeleft){
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.timeBreak = timeBreak;
        this.timeleft = timeleft;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getTimeBreak() {
        return timeBreak;
    }

    public int getTimeleft() {
        return timeleft;
    }
}
