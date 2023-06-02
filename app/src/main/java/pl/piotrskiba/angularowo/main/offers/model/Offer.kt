package pl.piotrskiba.angularowo.main.offers.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.offers.model.OfferModel
import pl.piotrskiba.angularowo.utils.TextUtils
import java.util.Calendar
import java.util.Date

data class Offer(
    val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    private val timeBreak: Int,
    private val availabilityDate: Date?,
    private val canAfford: Boolean,
) {

    private val currentDate = Calendar.getInstance().time
    private val availabilityDateReached = (availabilityDate ?: currentDate).time <= currentDate.time
    val canRedeem = canAfford && availabilityDateReached

    @ColorRes
    val imageTintColor =
        if (canRedeem) {
            R.color.blackA50
        } else {
            R.color.blackA85
        }

    @ColorRes
    val textColor =
        if (canRedeem) {
            R.color.offer_text_color_default
        } else {
            R.color.offer_text_color_disabled
        }

    @DrawableRes
    val priceIconDrawable =
        if (canRedeem) {
            R.drawable.ic_coin
        } else {
            R.drawable.ic_coin_disabled
        }

    @ColorRes
    val priceTextColor =
        if (canRedeem) {
            R.color.color_coin
        } else {
            R.color.color_coin_disabled
        }
    val priceText = price.toString()

    val timeLeftVisible = !availabilityDateReached
    val timeLeftText = TextUtils.formatTimeDifference(
        availabilityDate ?: currentDate,
        currentDate
    )
}

fun OfferModel.toUi(userPoints: Int) =
    Offer(
        id,
        title,
        description,
        price,
        imageUrl,
        timeBreak,
        availabilityDate,
        userPoints >= price
    )

fun List<OfferModel>.toUi(userPoints: Int) =
    map { it.toUi(userPoints) }
