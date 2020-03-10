package pl.piotrskiba.angularowo

import org.junit.Assert
import org.junit.Test
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
}