package com.codepath.easydo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by John on 7/17/2017.
 */

public class Util {

    public static StringBuilder showDate(int year, int month, int day) {
        return (new StringBuilder().append(month).append("/")
                .append(day).append("/").append(year));
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
