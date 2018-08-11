package com.boyanstoynov.littlebigspender.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Description
 *
 * @author Boyan Stoynov
 */
public class DateUtils {

    private static Locale defaultLocale = Locale.getDefault();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", defaultLocale);

    /**
     * Returns an integer representing today's year.
     * @return int today's year
     */
    public static int yearToday() {
        return new GregorianCalendar(defaultLocale).get(Calendar.YEAR);
    }

    /**
     * Returns an integer representing today's month.
     * @return int today's month
     */
    public static int monthToday() {
        return new GregorianCalendar(defaultLocale).get(Calendar.MONTH);
    }

    /**
     * Returns an integer representing today's day.
     * @return int today's day
     */
    public static int dayToday() {
        return new GregorianCalendar(defaultLocale).get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Formats a Date according to the dd-MM-yyyy format.
     * @param date Date object
     * @return String formatted date
     */
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    /**
     * Returns a Date representing the start of the day (i.e. 00:00:00)
     * for the given Date object.
     * @param date arbitrary Date
     * @return Date start of day
     */
    public static Date startOfDay(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * Returns a Date representing the end of the day (i.e. 24:00:00)
     * for the given Date object.
     * @param date arbitrary Date
     * @return Date end of day
     */
    public static Date endOfDay(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    /**
     * Returns True if and only if Date is equal to today's date.
     * @param date Date to be checked
     * @return boolean whether date is today
     */
    public static boolean isToday(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        todayCalendar.setTime(new Date());
        compareCalendar.setTime(date);

        return todayCalendar.get(Calendar.YEAR) == compareCalendar.get(Calendar.YEAR) &&
        todayCalendar.get(Calendar.DAY_OF_YEAR) == compareCalendar.get(Calendar.DAY_OF_YEAR);
    }
}
