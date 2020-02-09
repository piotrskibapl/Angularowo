package pl.piotrskiba.angularowo.models;

public class AdOffer {

    private final String id;
    private final int points;
    private final String adId;
    private final int timeBreak;
    private final int timeleft;

    public AdOffer(String id, int points, String adId, int timeBreak, int timeleft){
        this.id = id;
        this.points = points;
        this.adId = adId;
        this.timeBreak = timeBreak;
        this.timeleft = timeleft;
    }

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public String getAdId() {
        return adId;
    }

    public int getTimeBreak() {
        return timeBreak;
    }

    public int getTimeleft() {
        return timeleft;
    }
}
