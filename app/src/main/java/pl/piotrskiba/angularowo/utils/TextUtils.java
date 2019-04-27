package pl.piotrskiba.angularowo.utils;

import android.content.Context;

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
}
