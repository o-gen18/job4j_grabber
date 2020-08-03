package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Map;

public class SqlRuParse {

    private static final Map<String, Integer> MONTHS = new java.util.HashMap<>();

    private static void fillingMap() {
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
        MONTHS.put("сегодня", Calendar.DATE);
        MONTHS.put("вчера", Calendar.DATE - 1);
    }

    public static void main(String[] args) throws Exception {
        fillingMap();
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        Elements dates = doc.select(".altCol");
        for (int i = 0, k = 1; i < row.size(); i++, k += 2) {
            Element tdPosts = row.get(i);
            Element href = tdPosts.child(0);
            Element date = dates.get(k);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
           Calendar javaDate = dateConvert(date.text());
            System.out.println(javaDate.getTime());
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
            month = MONTHS.get(date);
            day = Calendar.DATE;
            year = Calendar.YEAR;
            javaDate.set(year, month, day, hour, minute);
            return javaDate;
        }
        String[] dayMonthYear = date.split(" ");
        day = Integer.parseInt(dayMonthYear[0]);
        month = MONTHS.get(dayMonthYear[1]);
        year = Integer.parseInt(dayMonthYear[2]);
        javaDate.set(year, month, day, hour, minute);
        return javaDate;
    }
}