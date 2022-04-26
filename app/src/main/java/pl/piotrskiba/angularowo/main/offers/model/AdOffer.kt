package pl.piotrskiba.angularowo.main.offers.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.offers.model.AdOfferModel
import pl.piotrskiba.angularowo.utils.TextUtils
import java.util.Calendar
import java.util.Date

data class AdOffer(
    private val id: String,
    private val points: Int,
    private val adId: String,
    private val timeBreak: Int,
    private val availabilityDate: Date?,
    private val pointsLimitReached: Boolean,
) {

    private val currentDate = Calendar.getInstance().time
    private val availabilityDateReached = (availabilityDate ?: currentDate).time <= currentDate.time
    private val canRedeem = !pointsLimitReached && availabilityDateReached

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

    val timeLeftVisible = !availabilityDateReached
    val timeLeftText = TextUtils.formatTimeDifference(
        availabilityDate ?: currentDate,
        currentDate
    )
}

fun AdOfferModel.toUi(pointsLimitReached: Boolean) =
    AdOffer(
        id,
        points,
        adId,
        timeBreak,
        availabilityDate,
        pointsLimitReached
    )

fun List<AdOfferModel>.toUi(pointsLimitReached: Boolean) =
    map { it.toUi(pointsLimitReached) }
