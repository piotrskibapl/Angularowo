package pl.piotrskiba.angularowo.main.offers.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.offers.model.AdOfferModel
import java.util.Date

data class AdOffer(
    override val id: String,
    val points: Int,
    val adId: String,
    private val timeBreak: Int,
    private val availabilityDate: Date?,
    private val pointsLimitReached: Boolean,
) : Offer(id = id, availabilityDate = availabilityDate, redeemable = !pointsLimitReached) {

    @ColorRes
    val pointsTextColor =
        if (canRedeem) {
            R.color.color_coin
        } else {
            R.color.color_coin_disabled
        }
    val pointsText = points.toString()

    @DrawableRes
    val iconDrawable =
        if (canRedeem) {
            R.drawable.ic_coin
        } else {
            R.drawable.ic_coin_disabled
        }
}

fun AdOfferModel.toUi(pointsLimitReached: Boolean) =
    AdOffer(
        id,
        points,
        adId,
        timeBreak,
        availabilityDate,
        pointsLimitReached,
    )

fun List<AdOfferModel>.toUi(pointsLimitReached: Boolean) =
    map { it.toUi(pointsLimitReached) }
