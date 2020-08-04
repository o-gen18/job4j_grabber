package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Calendar;
import java.util.Map;

public class SqlRuParse {

    private final Map<String, Integer> months = new java.util.HashMap<>();

    public SqlRuParse() {
        fillingMap();
    }

    private void fillingMap() {
        Calendar today = Calendar.getInstance();
        months.put("янв", 0);
        months.put("фев", 1);
        months.put("мар", 2);
        months.put("апр", 3);
        months.put("май", 4);
        months.put("июн", 5);
        months.put("июл", 6);
        months.put("авг", 7);
        months.put("сен", 8);
        months.put("окт", 9);
        months.put("ноя", 10);
        months.put("дек", 11);
        months.put("сегодня", today.get(Calendar.DAY_OF_MONTH));
        months.put("вчера", today.get(Calendar.DAY_OF_MONTH) - 1);
    }

    private int getTrueYear(int shortYear, Calendar today) {
        int previousCentury = 1900;
        int thisCentury = 2000;
        int thisYear = today.get(Calendar.YEAR);
        if (thisYear - shortYear >= thisCentury) {
            return thisCentury + shortYear;
        } else {
            return previousCentury + shortYear;
        }
    }

    public Calendar dateConvert(String input) {
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
            day = months.get(date);
            year = javaDate.get(Calendar.YEAR);
            javaDate.set(year, month, day, hour, minute);
            return javaDate;
        }
        String[] dayMonthYear = date.split(" ");
        day = Integer.parseInt(dayMonthYear[0]);
        month = months.get(dayMonthYear[1]);
        year = getTrueYear(Integer.parseInt(dayMonthYear[2]), javaDate);
        javaDate.set(year, month, day, hour, minute);
        return javaDate;
    }

    public static void main(String[] args) throws Exception {
        SqlRuParse sqlRuParse = new SqlRuParse();
        for (int p = 1; p <= 5; p++) {
            String page = String.valueOf(p);
            String url = "https://www.sql.ru/forum/job-offers/".concat(page);
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".postslisttopic");
            Elements dates = doc.select(".altCol");
            for (int i = 0, k = 1; i < row.size(); i++, k += 2) {
                Element tdPosts = row.get(i);
                Element href = tdPosts.child(0);
                Element date = dates.get(k);
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                Calendar javaDate = sqlRuParse.dateConvert(date.text());
                System.out.println(javaDate.getTime());
            }
        }
    }
}