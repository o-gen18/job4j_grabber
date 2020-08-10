package ru.job4j.grabber;

import java.util.Calendar;

public class Post {
    
    private String id;

    private String vacancyName;

    private String vacancyURL;

    private String vacancyDesc;

    private String authorName;

    private String authorURL;

    private Calendar dateOfCreation;

    private Calendar dateOfLatestComment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Calendar getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Calendar dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Calendar getDateOfLatestComment() {
        return dateOfLatestComment;
    }

    public void setDateOfLatestComment(Calendar dateOfLatestComment) {
        this.dateOfLatestComment = dateOfLatestComment;
    }
}
