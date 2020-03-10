package pl.piotrskiba.angularowo;

import org.junit.Assert;
import org.junit.Test;

import pl.piotrskiba.angularowo.utils.TextUtils;

public class TextUtilsTests {

    @Test
    public void normalize_specialChars_normalized() {
        Assert.assertEquals("eoaszzcnaY", TextUtils.normalize("ęóąśłżźćń¥ƒáÝ‰"));
    }

    @Test
    public void normalize_normalizedText_notChanged() {
        String sentence = "Already normalized sentence.";
        Assert.assertEquals(sentence, TextUtils.normalize(sentence));
    }
}
