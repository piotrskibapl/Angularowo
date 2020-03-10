package pl.piotrskiba.angularowo

import org.junit.Assert
import org.junit.Test
import pl.piotrskiba.angularowo.utils.TextUtils
import pl.piotrskiba.angularowo.utils.TextUtils.normalize

class TextUtilsTests {
    @Test
    fun normalize_specialChars_normalized() {
        Assert.assertEquals("eoaszzcnaY", normalize("ęóąśłżźćń¥ƒáÝ‰"))
    }

    @Test
    fun normalize_normalizedText_notChanged() {
        val sentence = "Already normalized sentence."
        Assert.assertEquals(sentence, normalize(sentence))
    }

    @Test
    fun formatTps_20_noComma() {
        val expected = "20"
        val actual = TextUtils.formatTps(20.toDouble())
        Assert.assertEquals(expected, actual)
    }

    @Test
    fun formatTps_19_99_withComma() {
        val expected = "19.99"
        val actual = TextUtils.formatTps(19.99)
        Assert.assertEquals(expected, actual)
    }
}