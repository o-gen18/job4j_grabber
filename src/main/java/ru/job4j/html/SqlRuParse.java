package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        Elements dates = doc.select(".altCol");
        for (int i = 0; i < row.size(); i++) {
            Element tdPosts = row.get(i);
            Element href = tdPosts.child(0);
            Element date = dates.get(i);
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            if (i % 2 != 0) {
                System.out.println(date.text());
            }
        }
    }
}
