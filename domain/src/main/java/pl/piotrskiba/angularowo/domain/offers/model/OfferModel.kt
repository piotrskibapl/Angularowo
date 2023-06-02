package pl.piotrskiba.angularowo.domain.offers.model

import java.util.Date

data class OfferModel(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    val timeBreak: Int,
    val availabilityDate: Date?,
)
