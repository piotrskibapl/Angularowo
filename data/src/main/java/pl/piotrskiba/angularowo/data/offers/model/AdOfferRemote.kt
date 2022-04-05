package pl.piotrskiba.angularowo.data.offers.model

import pl.piotrskiba.angularowo.domain.offers.model.AdOfferModel
import java.util.Date
import java.util.concurrent.TimeUnit

data class AdOfferRemote(
    val id: String,
    val points: Int,
    val adId: String,
    val timeBreak: Int,
    val availabilityDate: Long?
)

fun List<AdOfferRemote>.toDomain() =
    map { it.toDomain() }

private fun AdOfferRemote.toDomain() =
    AdOfferModel(
        id,
        points,
        adId,
        timeBreak,
        if (availabilityDate == null) null else Date(TimeUnit.SECONDS.toMillis(availabilityDate))
    )
