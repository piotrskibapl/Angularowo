package pl.piotrskiba.angularowo.main.offers.model

import pl.piotrskiba.angularowo.domain.offers.model.OfferModel
import java.util.Date

data class Offer(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val timeBreak: Int,
    val availabilityDate: Date?
)

fun OfferModel.toUi() =
    Offer(
        id,
        title,
        description,
        price,
        imageUrl,
        timeBreak,
        availabilityDate
    )

fun List<OfferModel>.toUi() =
    map { it.toUi() }
