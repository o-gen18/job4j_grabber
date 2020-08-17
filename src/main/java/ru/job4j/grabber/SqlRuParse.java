package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SqlRuParse implements Parse {

    private static final Logger LOG = LoggerFactory.getLogger(SqlRuParse.class.getName());

    private Predicate<String> language;

    private final Map<String, Integer> months = new java.util.HashMap<>();

    public SqlRuParse(Predicate<String> language) {
        this.language = language;
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

    @Override
    public List<Post> list(String link)  {
        List<Post> posts = new ArrayList<>();
        LOG.debug("Parsing: {}", link);
        try {
            for (int p = 1; p <= 5; p++) {
                String page = String.valueOf(p);
                String url = link.concat(page);
                Document doc = Jsoup.connect(url).get();
                Elements row = doc.select(".postslisttopic");
                LOG.debug("Retrieved {} rows", row.size());
                Elements dates = doc.select(".altCol");
                for (int i = 0, k = 1; i < row.size(); i++, k += 2) {

                    Element tdPosts = row.get(i);
                    Element href = tdPosts.child(0);
                    Element dateOfLatestComment = dates.get(k);
                    String vacancyURL = href.attr("href");
                    String vacancyName = href.text();
                    LOG.debug("Parsing vacancy: {}", vacancyName);
                    if (!language.test(vacancyName)) {
                        continue;
                    }
                    Post post = detail(vacancyURL);
                    Calendar javaDate = dateConvert(dateOfLatestComment.text());
                    post.setDateOfLatestComment(javaDate);
                    post.setVacancyName(vacancyName);
                    post.setVacancyURL(vacancyURL);
                    posts.add(post);
                }
            }
            LOG.debug("Parsing completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        try {
            Document doc = Jsoup.connect(link).get();
            Elements descs = doc.select(".msgBody");
            Elements msgFooter = doc.select(".msgFooter");

            Element descUneven = descs.get(1);
            post.setVacancyDesc(descUneven.text());

            Element descEven = descs.get(0);
            Element author = descEven.child(0);
            String url = author.attr("href");
            String name = author.text();
            post.setAuthorName(name);
            post.setAuthorURL(url);

            String dateAndStuff = msgFooter.get(0).text();
            int endingOfDate = dateAndStuff.indexOf(" [");
            String date = dateAndStuff.substring(0, endingOfDate);
            Calendar javaDate = dateConvert(date);
            post.setDateOfCreation(javaDate);
            LOG.debug("Post was created on: {}", javaDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }
}