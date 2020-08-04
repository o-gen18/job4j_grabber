package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class PostLoad {
    public static void main(String[] args) throws Exception {
        SqlRuParse sqlRuParse = new SqlRuParse();
        List<Post> posts = new ArrayList<>();
        Document doc = Jsoup.connect(
                "https://www.sql.ru/forum/1325330/"
                        + "lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t").get();
        Elements descs = doc.select(".msgBody");
        Elements msgFooter = doc.select(".msgFooter");

        for (int i = 0, k = 0; i < descs.size(); i += 2, k++) {

            Post post = new Post();
            Element descUneven = descs.get(i + 1);
            post.setVacancyDesc(descUneven.text());

            Element descEven = descs.get(i);
            Element author = descEven.child(0);
            String url = author.attr("href");
            String name = author.text();
            post.setAuthorName(name);
            post.setAuthorURL(url);

            String dateAndStuff = msgFooter.get(k).text();
            int endingOfDate = dateAndStuff.indexOf(" [");
            String date = dateAndStuff.substring(0, endingOfDate);
            System.out.println(date);
            post.setDate(sqlRuParse.dateConvert(date));
            posts.add(post);

            Post saved = posts.get(k);
            System.out.println("Автор: " + saved.getAuthorName());
            System.out.println("Информация об авторе: " + saved.getAuthorURL());
            System.out.println("Описание вакансии: " + saved.getVacancyDesc());
            System.out.println("Дата публикации: " + saved.getDate().getTime());
            System.out.println();
        }
    }
}
