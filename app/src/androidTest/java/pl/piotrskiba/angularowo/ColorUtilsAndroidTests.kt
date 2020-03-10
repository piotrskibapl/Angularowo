package pl.piotrskiba.angularowo

import android.content.Context
import android.graphics.Color
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import pl.piotrskiba.angularowo.utils.ColorUtils

class ColorUtilsAndroidTests {
    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun getColorFromCode_regularColorCodes_success() {
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "0"), instrumentationContext.getColor(R.color.color_minecraft_0))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "1"), instrumentationContext.getColor(R.color.color_minecraft_1))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "2"), instrumentationContext.getColor(R.color.color_minecraft_2))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "3"), instrumentationContext.getColor(R.color.color_minecraft_3))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "4"), instrumentationContext.getColor(R.color.color_minecraft_4))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "5"), instrumentationContext.getColor(R.color.color_minecraft_5))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "6"), instrumentationContext.getColor(R.color.color_minecraft_6))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "7"), instrumentationContext.getColor(R.color.color_minecraft_7))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "8"), instrumentationContext.getColor(R.color.color_minecraft_8))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "9"), instrumentationContext.getColor(R.color.color_minecraft_9))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "a"), instrumentationContext.getColor(R.color.color_minecraft_a))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "b"), instrumentationContext.getColor(R.color.color_minecraft_b))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "c"), instrumentationContext.getColor(R.color.color_minecraft_c))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "d"), instrumentationContext.getColor(R.color.color_minecraft_d))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "e"), instrumentationContext.getColor(R.color.color_minecraft_e))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "f"), instrumentationContext.getColor(R.color.color_minecraft_f))
    }

    @Test
    fun getColorFromCode_invalidColorCodes_returns7() {
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "g"), instrumentationContext.getColor(R.color.color_minecraft_7))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "ó"), instrumentationContext.getColor(R.color.color_minecraft_7))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, "n"), instrumentationContext.getColor(R.color.color_minecraft_7))
        Assert.assertEquals(ColorUtils.getColorFromCode(instrumentationContext, ""), instrumentationContext.getColor(R.color.color_minecraft_7))
    }

    @Test
    fun changeBrightness_darkenBlack_nothingChanged() {
        Assert.assertEquals(Color.BLACK, ColorUtils.changeBrightness(Color.BLACK, 1.5f))
    }
}