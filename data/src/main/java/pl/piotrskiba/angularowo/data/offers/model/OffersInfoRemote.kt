package pl.piotrskiba.angularowo.data.offers.model

import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel

data class OffersInfoRemote(
    val points: Int,
    val pointsLimitReached: Boolean,
    val adOffers: List<AdOfferRemote>,
    val offers: List<OfferRemote>
)

fun OffersInfoRemote.toDomain() =
    OffersInfoModel(
        points,
        pointsLimitReached,
        adOffers.toDomain(),
        offers.toDomain()
    )
