package pl.piotrskiba.angularowo.utils;

import android.graphics.Color;

public class ColorUtils {

    public static int changeBrightness(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }
}
