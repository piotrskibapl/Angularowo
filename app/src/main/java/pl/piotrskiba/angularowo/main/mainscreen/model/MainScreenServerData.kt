package pl.piotrskiba.angularowo.main.mainscreen.model

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel
import java.util.Locale
import kotlin.math.round

data class MainScreenServerData(
    val motdVisible: Boolean,
    val motdClickable: Boolean,
    val motdText: String?,
    val motdUrl: String?,
    private val motdTextColorHex: String?,
    private val motdBackgroundColorHex: String?,
    private val playerCount: Int,
    private val tps: Double,
    private val displayTps: Boolean,
) {

    @ColorInt
    fun motdTextColor() =
        Color.parseColor(motdTextColorHex ?: "#000000")

    @ColorInt
    fun motdBackgroundColor() =
        Color.parseColor(motdBackgroundColorHex ?: "#000000")

    fun playerCountText(context: Context) =
        if (displayTps) {
            context.resources.getQuantityString(
                R.plurals.main_screen_playercount_tps,
                playerCount,
                playerCount,
                tpsText(tps),
            )
        } else {
            context.resources.getQuantityString(
                R.plurals.main_screen_playercount,
                playerCount,
                playerCount,
            )
        }

    private fun tpsText(tps: Double) =
        if (tps == round(tps)) {
            String.format(Locale.getDefault(), "%d", tps.toInt())
        } else {
            String.format(Locale.getDefault(), "%.1f", tps)
        }
}

fun ServerStatusModel.toUi(displayTps: Boolean) = MainScreenServerData(
    motdVisible = motd != null,
    motdClickable = motd?.url != null,
    motdText = motd?.text,
    motdUrl = motd?.url,
    motdTextColorHex = motd?.textColor,
    motdBackgroundColorHex = motd?.backgroundColor,
    playerCount = playerCount,
    tps = tps,
    displayTps = displayTps,
)
