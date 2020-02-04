package pl.piotrskiba.angularowo.utils;

import android.content.Context;
import android.graphics.Color;

import pl.piotrskiba.angularowo.R;

public class ColorUtils {

    public static int changeBrightness(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }

    public static int getColorFromCode(Context context, String colorcode){
        switch(colorcode){
            case "0":
                return context.getResources().getColor(R.color.color_minecraft_0);
            case "1":
                return context.getResources().getColor(R.color.color_minecraft_1);
            case "2":
                return context.getResources().getColor(R.color.color_minecraft_2);
            case "3":
                return context.getResources().getColor(R.color.color_minecraft_3);
            case "4":
                return context.getResources().getColor(R.color.color_minecraft_4);
            case "5":
                return context.getResources().getColor(R.color.color_minecraft_5);
            case "6":
                return context.getResources().getColor(R.color.color_minecraft_6);
            case "7":
                return context.getResources().getColor(R.color.color_minecraft_7);
            case "8":
                return context.getResources().getColor(R.color.color_minecraft_8);
            case "9":
                return context.getResources().getColor(R.color.color_minecraft_9);
            case "a":
                return context.getResources().getColor(R.color.color_minecraft_a);
            case "b":
                return context.getResources().getColor(R.color.color_minecraft_b);
            case "c":
                return context.getResources().getColor(R.color.color_minecraft_c);
            case "d":
                return context.getResources().getColor(R.color.color_minecraft_d);
            case "e":
                return context.getResources().getColor(R.color.color_minecraft_e);
            case "f":
                return context.getResources().getColor(R.color.color_minecraft_f);
        }
        return context.getResources().getColor(R.color.color_minecraft_7);
    }
}
