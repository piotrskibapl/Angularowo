package pl.piotrskiba.angularowo.interfaces;

import android.view.View;

import pl.piotrskiba.angularowo.models.Offer;

public interface OfferClickListener {

    void onOfferClick(View view, Offer clickedOffer);
}
