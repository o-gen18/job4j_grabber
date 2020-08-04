package ru.job4j.html;

import java.util.Calendar;

public class Post {

    private String vacancyName;

    private String vacancyURL;

    private String vacancyDesc;

    private String authorName;

    private String authorURL;

    private Calendar date;

    public String getVacancyName() {
        return vacancyName;
    }

    public void setVacancyName(String vacancyName) {
        this.vacancyName = vacancyName;
    }

    public String getVacancyURL() {
        return vacancyURL;
    }

    public void setVacancyURL(String vacancyURL) {
        this.vacancyURL = vacancyURL;
    }

    public String getVacancyDesc() {
        return vacancyDesc;
    }

    public void setVacancyDesc(String vacancyDesc) {
        this.vacancyDesc = vacancyDesc;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorURL() {
        return authorURL;
    }

    public void setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
