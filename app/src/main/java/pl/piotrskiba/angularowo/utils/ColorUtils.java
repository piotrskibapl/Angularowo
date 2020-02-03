package pl.piotrskiba.angularowo.utils;

import android.graphics.Color;
import android.util.Log;

import pl.piotrskiba.angularowo.R;

public class ColorUtils {

    public static int changeBrightness(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }

    public static int getColorFromCode(String colorcode){
        switch(colorcode){
            case "0":
                return R.color.color_minecraft_0;
            case "1":
                return R.color.color_minecraft_1;
            case "2":
                return R.color.color_minecraft_2;
            case "3":
                return R.color.color_minecraft_3;
            case "4":
                return R.color.color_minecraft_4;
            case "5":
                return R.color.color_minecraft_5;
            case "6":
                return R.color.color_minecraft_6;
            case "7":
                return R.color.color_minecraft_7;
            case "8":
                return R.color.color_minecraft_8;
            case "9":
                return R.color.color_minecraft_9;
            case "a":
                return R.color.color_minecraft_a;
            case "b":
                return R.color.color_minecraft_b;
            case "c":
                return R.color.color_minecraft_c;
            case "d":
                return R.color.color_minecraft_d;
            case "e":
                return R.color.color_minecraft_e;
            case "f":
                return R.color.color_minecraft_f;
        }
        return R.color.color_minecraft_7;
    }
}
