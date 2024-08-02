package pl.piotrskiba.angularowo.main.offers.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.offers.model.OfferModel
import java.util.Date

data class PrizeOffer(
    override val id: String,
    val title: String,
    val description: String,
    val price: Int,
    val imageUrl: String,
    private val timeBreak: Int,
    private val availabilityDate: Date?,
    private val canAfford: Boolean,
) : Offer(id = id, availabilityDate = availabilityDate, redeemable = canAfford) {

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
}

fun OfferModel.toUi(userPoints: Int) =
    PrizeOffer(
        id,
        title,
        description,
        price,
        imageUrl,
        timeBreak,
        availabilityDate,
        userPoints >= price,
    )

fun List<OfferModel>.toUi(userPoints: Int) =
    map { it.toUi(userPoints) }
