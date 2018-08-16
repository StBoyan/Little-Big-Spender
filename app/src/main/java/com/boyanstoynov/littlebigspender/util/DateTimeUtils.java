package com.boyanstoynov.littlebigspender.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Utility class with static methods that provide various
 * functions to the application relating to date and time.
 *
 * @author Boyan Stoynov
 */
public class DateTimeUtils {

    private static Locale defaultLocale = Locale.getDefault();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", defaultLocale);
    private static SimpleDateFormat dayMonthFormat = new SimpleDateFormat("MMM d", defaultLocale);

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
     * Formats a Date according to the MMM d format.
     * @param date Date object
     * @return String formatted date
     */
    public static String formatDayMonth(Date date) {
        return dayMonthFormat.format(date);
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
     * Returns a date representing the start of the current week (Monday).
     * @return Date Monday of this week
     */
    public static Date getStartOfWeek() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        return calendar.getTime();
    }

    /**
     * Returns True if and only if the date is equal to today's date.
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

    /**
     * Returns true if and only if the date has past.
     * @param date Date to be checked
     * @return boolean whether date has passed
     */
    public static boolean dateHasPassed(Date date) {
        Calendar todayCalendar = Calendar.getInstance();
        Calendar compareCalendar = Calendar.getInstance();
        todayCalendar.setTime(new Date());
        compareCalendar.setTime(date);

        if (todayCalendar.get(Calendar.YEAR) > compareCalendar.get(Calendar.YEAR))
            return false;
        else if (todayCalendar.get(Calendar.YEAR) < compareCalendar.get(Calendar.YEAR))
            return true;

        if (todayCalendar.get(Calendar.DAY_OF_YEAR) <= compareCalendar.get(Calendar.DAY_OF_YEAR))
            return false;
        else
            return true;
    }

    /**
     * Returns a date that is a month in the future from the
     * given date.
     * @param date date
     * @return Date a month in the future
     */
    public static Date addMonth(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    /**
     * Returns a date that is 2 weeks in the future from the
     * given date.
     * @param date date
     * @return Date 2 weeks in the future
     */
    public static Date addTwoWeeks(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 14);
        return calendar.getTime();
    }

    /**
     * Returns a date that is a week in the future from the
     * given date.
     * @param date date
     * @return Date a week in the future
     */
    public static Date addWeek(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        return calendar.getTime();
    }

    /**
     * Returns a date that is a day in the future from the
     * given date.
     * @param date date
     * @return Date a day in the future
     */
    public static Date addDay(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,1);
        return calendar.getTime();
    }

    /**
     * Gets the current time denoted in epoch time in milliseconds.
     * @return long current epoch time
     */
    public static long getCurrentTimeInMillis() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * Returns the elapsed time in milliseconds between the specified
     * time and the present moment.
     * @param epochTime epoch time in milliseconds
     * @return long elapsed time in milliseconds
     */
    public static long getElapsedTime(long epochTime) {
        Date pastTime = new Date(epochTime);
        Date now = new Date();

        return now.getTime() - pastTime.getTime();
    }
}
