package pl.piotrskiba.angularowo.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import pl.piotrskiba.angularowo.R;

public class TextUtils {

    public static String formatPlaytime(Context context, int playtime){
        int days = 0, hours = 0, minutes = 0;
        while(playtime >= 60*60*24){
            days++;
            playtime -= 60*60*24;
        }
        while(playtime >= 60*60){
            hours++;
            playtime -= 60*60;
        }
        while(playtime >= 60){
            minutes++;
            playtime -= 60;
        }

        String result = "";
        if(days > 0)
            result = context.getResources().getQuantityString(R.plurals.days, days, days);
        if(hours > 0){
            if(result.isEmpty()){
                result = context.getResources().getQuantityString(R.plurals.hours, hours, hours);
            }
            else if(minutes > 0){
                result += ", " + context.getResources().getQuantityString(R.plurals.hours, hours, hours);
            }
            else{
                result += " i " + context.getResources().getQuantityString(R.plurals.hours, hours, hours);
            }
        }
        if(minutes > 0) {
            if(result.isEmpty()){
                result = context.getResources().getQuantityString(R.plurals.minutes, minutes, minutes);
            }
            else{
                result += " i " + context.getResources().getQuantityString(R.plurals.minutes, minutes, minutes);
            }
        }

        return result;
    }

    public static String replaceColorCodes(Context context, String s){
        s = s.replace("§0", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_0)).substring(2, 8) + ">");
        s = s.replace("§1", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_1)).substring(2, 8) + ">");
        s = s.replace("§2", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_2)).substring(2, 8) + ">");
        s = s.replace("§3", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_3)).substring(2, 8) + ">");
        s = s.replace("§4", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_4)).substring(2, 8) + ">");
        s = s.replace("§5", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_5)).substring(2, 8) + ">");
        s = s.replace("§6", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_6)).substring(2, 8) + ">");
        s = s.replace("§7", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_7)).substring(2, 8) + ">");
        s = s.replace("§8", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_8)).substring(2, 8) + ">");
        s = s.replace("§9", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_9)).substring(2, 8) + ">");
        s = s.replace("§a", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_a)).substring(2, 8) + ">");
        s = s.replace("§b", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_b)).substring(2, 8) + ">");
        s = s.replace("§c", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_c)).substring(2, 8) + ">");
        s = s.replace("§d", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_d)).substring(2, 8) + ">");
        s = s.replace("§e", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_e)).substring(2, 8) + ">");
        s = s.replace("§f", "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_f)).substring(2, 8) + ">");

        return s;
    }
}
