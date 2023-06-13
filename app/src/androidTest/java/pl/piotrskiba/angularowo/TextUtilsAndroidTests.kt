package pl.piotrskiba.angularowo

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import pl.piotrskiba.angularowo.utils.TextUtils

class TextUtilsAndroidTests {
    private lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun replaceColorCodes_oneColorCode_success() {
        val expected = "</font><font color=#008000>colored message"
        val actual = TextUtils.replaceColorCodes(instrumentationContext, "§acolored message")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun replaceColorCodes_threeColorCodes_success() {
        val expected = "Simple colored </font><font color=#00005e>text</font><font color=#808080>, with </font><font color=#5e005e>three color codes"
        val actual = TextUtils.replaceColorCodes(instrumentationContext, "Simple colored §1text§f, with §5three color codes")
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun replaceColorCodes_noColorCode_nothingChanged() {
        val text = "this message is not colored"
        Assert.assertEquals(text, text)
    }

    @Test
    fun replaceColorCodes_invalidColorCodes_nothingChanged() {
        val text = "§pthis message has a §nfew color codes that §-1does not §zexist"
        Assert.assertEquals(text, text)
    }
}
