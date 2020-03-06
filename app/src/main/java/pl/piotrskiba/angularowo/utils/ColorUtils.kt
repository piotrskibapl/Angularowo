package pl.piotrskiba.angularowo.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import pl.piotrskiba.angularowo.R

object ColorUtils {
    @JvmStatic
    fun changeBrightness(color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)

        hsv[2] *= factor

        return Color.HSVToColor(hsv)
    }

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