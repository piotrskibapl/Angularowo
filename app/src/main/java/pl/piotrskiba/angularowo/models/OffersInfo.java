package pl.piotrskiba.angularowo.models;

import java.util.List;

public class OffersInfo {

    private final int points;
    private final List<AdOffer> adOffers;
    private final List<Offer> offers;

    public OffersInfo(int points, List<AdOffer> adOffers, List<Offer> offers){
        this.points = points;
        this.adOffers = adOffers;
        this.offers = offers;
    }

    public int getPoints() {
        return points;
    }

    public List<AdOffer> getAdOffers() {
        return adOffers;
    }

    public List<Offer> getOffers() {
        return offers;
    }
}
