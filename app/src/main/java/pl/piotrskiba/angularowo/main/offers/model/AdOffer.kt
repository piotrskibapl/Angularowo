package pl.piotrskiba.angularowo.main.offers.model

import pl.piotrskiba.angularowo.domain.offers.model.AdOfferModel
import java.util.Date

data class AdOffer(
    val id: String,
    val points: Int,
    val adId: String,
    val timeBreak: Int,
    val availabilityDate: Date?
)

fun AdOfferModel.toUi() =
    AdOffer(
        id,
        points,
        adId,
        timeBreak,
        availabilityDate
    )

fun List<AdOfferModel>.toUi() =
    map { it.toUi() }
