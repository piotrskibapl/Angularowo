package pl.piotrskiba.angularowo.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import pl.piotrskiba.angularowo.R

object ColorUtils {

    /**
     * A function used to change the brightness of the color
     *
     * @param color the color to have brightness changed
     * @param factor brightness factor; factor > 1 will darken the color;
     * factor < 1 will lighten the color
     * @return the color with changed brightness
     */
    @JvmStatic
    fun changeBrightness(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        hsv[2] *= factor

        return Color.HSVToColor(hsv)
    }

    /**
     * A function to change the Minecraft color code to an actual color
     *
     * @param context
     * @param colorCode the color code to get color from
     * @return color corresponding to the color code or color corresponding to the "7" color code
     * if no corresponding color was found
     */
    @JvmStatic
    fun getColorFromCode(context: Context, colorCode: String): Int {
        when (colorCode) {
            "0" -> return ContextCompat.getColor(context, R.color.color_minecraft_0)
            "1" -> return ContextCompat.getColor(context, R.color.color_minecraft_1)
            "2" -> return ContextCompat.getColor(context, R.color.color_minecraft_2)
            "3" -> return ContextCompat.getColor(context, R.color.color_minecraft_3)
            "4" -> return ContextCompat.getColor(context, R.color.color_minecraft_4)
            "5" -> return ContextCompat.getColor(context, R.color.color_minecraft_5)
            "6" -> return ContextCompat.getColor(context, R.color.color_minecraft_6)
            "7" -> return ContextCompat.getColor(context, R.color.color_minecraft_7)
            "8" -> return ContextCompat.getColor(context, R.color.color_minecraft_8)
            "9" -> return ContextCompat.getColor(context, R.color.color_minecraft_9)
            "a" -> return ContextCompat.getColor(context, R.color.color_minecraft_a)
            "b" -> return ContextCompat.getColor(context, R.color.color_minecraft_b)
            "c" -> return ContextCompat.getColor(context, R.color.color_minecraft_c)
            "d" -> return ContextCompat.getColor(context, R.color.color_minecraft_d)
            "e" -> return ContextCompat.getColor(context, R.color.color_minecraft_e)
            "f" -> return ContextCompat.getColor(context, R.color.color_minecraft_f)
        }
        return ContextCompat.getColor(context, R.color.color_minecraft_7)
    }
}
