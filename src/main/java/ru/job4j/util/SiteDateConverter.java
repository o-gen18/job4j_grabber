package ru.job4j.util;

import java.util.Calendar;
import java.util.Map;

/**
 * Contains utility method for converting a string-represented date to the Calendar Instance.
 * This class is dedicated specifically for working with sql.ru site parsing.
 */
public class SiteDateConverter {
    private static final Map<String, Integer> MONTHS = new java.util.HashMap<>();

    static {
        Calendar today = Calendar.getInstance();
        MONTHS.put("янв", 0);
        MONTHS.put("фев", 1);
        MONTHS.put("мар", 2);
        MONTHS.put("апр", 3);
        MONTHS.put("май", 4);
        MONTHS.put("июн", 5);
        MONTHS.put("июл", 6);
        MONTHS.put("авг", 7);
        MONTHS.put("сен", 8);
        MONTHS.put("окт", 9);
        MONTHS.put("ноя", 10);
        MONTHS.put("дек", 11);
        MONTHS.put("сегодня", today.get(Calendar.DAY_OF_MONTH));
        MONTHS.put("вчера", today.get(Calendar.DAY_OF_MONTH) - 1);
    }

    private static int getTrueYear(int shortYear, Calendar today) {
        int previousCentury = 1900;
        int thisCentury = 2000;
        int thisYear = today.get(Calendar.YEAR);
        if (thisYear - shortYear >= thisCentury) {
            return thisCentury + shortYear;
        } else {
            return previousCentury + shortYear;
        }
    }

    public static Calendar dateConvert(String input) {
        String[] dateAndTime = input.split(", ");
        String date = dateAndTime[0];
        String time = dateAndTime[1];
        int colon = time.indexOf(":");
        int hour = Integer.parseInt(time.substring(0, colon));
        int minute = Integer.parseInt(time.substring(colon + 1));
        int day;
        int month;
        int year;
        Calendar javaDate = Calendar.getInstance();
        if (date.equals("сегодня") || date.equals("вчера")) {
            month = javaDate.get(Calendar.MONTH);
            day = MONTHS.get(date);
            year = javaDate.get(Calendar.YEAR);
            javaDate.set(year, month, day, hour, minute);
            return javaDate;
        }
        String[] dayMonthYear = date.split(" ");
        day = Integer.parseInt(dayMonthYear[0]);
        month = MONTHS.get(dayMonthYear[1]);
        year = getTrueYear(Integer.parseInt(dayMonthYear[2]), javaDate);
        javaDate.set(year, month, day, hour, minute);
        return javaDate;
    }
}
