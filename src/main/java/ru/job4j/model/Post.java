package ru.job4j.model;

import java.util.Calendar;
import java.util.Objects;

public class Post {
    
    private String id;

    private String vacancyName;

    private String vacancyURL;

    private String vacancyDesc;

    private String authorName;

    private String authorURL;

    private Calendar dateOfCreation;

    private Calendar dateOfLatestComment;

    public Post() { }

    public Post(String vacancyName, String vacancyURL, String vacancyDesc,
                 String authorName, String authorURL, Calendar created, Calendar latestComment) {
        this.vacancyName = vacancyName;
        this.vacancyURL = vacancyURL;
        this.vacancyDesc = vacancyDesc;
        this.authorName = authorName;
        this.authorURL = authorURL;
        this.dateOfCreation = created;
        this.dateOfLatestComment = latestComment;
    }

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

    @Override
    public String toString() {
        return "id in database: " + id + System.lineSeparator()
                + "Name of vacancy: " + vacancyName + System.lineSeparator()
                + "URL of vacancy: " + vacancyURL + System.lineSeparator()
                + "Описание вакансии: " + vacancyDesc + System.lineSeparator()
                + "Created: " + dateOfCreation.getTime() + System.lineSeparator()
                + "Author's name: " + authorName + System.lineSeparator()
                + "Author's info: " + authorURL + System.lineSeparator()
                + "Date of latest comment: "
                + dateOfLatestComment.getTime() + System.lineSeparator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id)
                && Objects.equals(vacancyName, post.vacancyName)
                && Objects.equals(vacancyURL, post.vacancyURL)
                && Objects.equals(vacancyDesc, post.vacancyDesc)
                && Objects.equals(authorName, post.authorName)
                && Objects.equals(authorURL, post.authorURL)
                && Objects.equals(dateOfCreation, post.dateOfCreation)
                && Objects.equals(dateOfLatestComment, post.dateOfLatestComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, vacancyName, vacancyURL, vacancyDesc,
                authorName, authorURL, dateOfCreation, dateOfLatestComment);
    }
}
