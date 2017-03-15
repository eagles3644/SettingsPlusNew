package com.dugan.settingsplus;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by leona on 12/13/2015.
 */
public class MyDateTimeFormatter {

    public static String formatDateTime(Long millis){

        Locale locale = Locale.getDefault();
        DateFormat shortDatetime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        DateFormat shortTime = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        String datetime = shortDatetime.format(millis);

        if(isSameDay(millis)){
            datetime = shortTime.format(millis);
        }

        return datetime;
    }

    public static boolean isSameDay(Long millis){
        Calendar today = Calendar.getInstance();
        today.getTimeInMillis();
        Calendar requestTime = Calendar.getInstance();
        requestTime.setTimeInMillis(millis);
        return today.get(Calendar.YEAR) == requestTime.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == requestTime.get(Calendar.DAY_OF_YEAR);
    }

}
