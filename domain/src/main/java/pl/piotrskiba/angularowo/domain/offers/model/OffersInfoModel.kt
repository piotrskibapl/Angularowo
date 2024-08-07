package pl.piotrskiba.angularowo.domain.offers.model

data class OffersInfoModel(
    val points: Int,
    val pointsLimitReached: Boolean,
    val adOffers: List<AdOfferModel>,
    val prizeOffers: List<OfferModel>,
)
