package pl.piotrskiba.angularowo.utils;

import java.util.Calendar;

public class GlideUtils {

    public static int getSignatureVersionNumber(int lifetimeInDays){
        Calendar calendar = Calendar.getInstance();

        return calendar.get(Calendar.YEAR) * 1000 + calendar.get(Calendar.DAY_OF_YEAR) / lifetimeInDays;
    }
}
