package pl.piotrskiba.angularowo.main.offers.model

import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel

data class OffersInfo(
    private val points: Int,
    val pointsLimitReached: Boolean,
    val adOffers: List<AdOffer>,
    val offers: List<Offer>
) {


    val pointsText = points.toString()
}

fun OffersInfoModel.toUi() =
    OffersInfo(
        points,
        pointsLimitReached,
        adOffers.toUi(pointsLimitReached),
        offers.toUi(points)
    )
