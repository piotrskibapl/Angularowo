package pl.piotrskiba.angularowo.interfaces;

import android.view.View;

import pl.piotrskiba.angularowo.models.AdOffer;

public interface AdOfferClickListener {

    void onAdOfferClick(View view, AdOffer clickedAdOffer);
}
