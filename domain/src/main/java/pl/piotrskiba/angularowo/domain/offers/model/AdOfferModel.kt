package pl.piotrskiba.angularowo.domain.offers.model

import java.util.Date

data class AdOfferModel(
    val id: String,
    val points: Int,
    val adId: String,
    val timeBreak: Int,
    val availabilityDate: Date?
)
