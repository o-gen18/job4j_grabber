package ru.job4j.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Post;
import ru.job4j.util.SiteDateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Predicate;

public class SqlRuParse implements Parse {

    private static final Logger LOG = LoggerFactory.getLogger(SqlRuParse.class.getName());

    private Predicate<String> language;

    public SqlRuParse(Predicate<String> language) {
        this.language = language;
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
                    Calendar javaDate = SiteDateConverter.dateConvert(dateOfLatestComment.text());
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
            post.setVacancyDesc(descUneven.html());

            Element descEven = descs.get(0);
            Element author = descEven.child(0);
            String url = author.attr("href");
            String name = author.text();
            post.setAuthorName(name);
            post.setAuthorURL(url);

            String dateAndStuff = msgFooter.get(0).text();
            int endingOfDate = dateAndStuff.indexOf(" [");
            String date = dateAndStuff.substring(0, endingOfDate);
            Calendar javaDate = SiteDateConverter.dateConvert(date);
            post.setDateOfCreation(javaDate);
            LOG.debug("Post was created on: {}", javaDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }
}