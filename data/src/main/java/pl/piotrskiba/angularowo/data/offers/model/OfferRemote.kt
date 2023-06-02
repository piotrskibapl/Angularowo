package pl.piotrskiba.angularowo.data.offers.model

import pl.piotrskiba.angularowo.domain.offers.model.OfferModel
import java.util.Date

data class OfferRemote(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val timeBreak: Int,
    val availabilityDate: Long?,
)

fun List<OfferRemote>.toDomain() =
    map { it.toDomain() }

private fun OfferRemote.toDomain() =
    OfferModel(
        id,
        title,
        description,
        price,
        imageUrl,
        timeBreak,
        if (availabilityDate == null) null else Date(availabilityDate)
    )
