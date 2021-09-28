package ru.job4j.html;

import org.junit.Test;
import ru.job4j.database.ConnectionRollback;
import ru.job4j.database.PsqlStore;
import ru.job4j.model.Post;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PsqlStoreTest {

    public Connection init() {
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("grabber.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            return DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void whenSaveThenObjectIsPersisted() throws Exception {
        try (PsqlStore psqlStore = new PsqlStore(ConnectionRollback.create(this.init()))) {
            Post expected = new Post("Java Junior", "vacancy url",
                    "vacancy desc", "author name", "author url",
                    Calendar.getInstance(), Calendar.getInstance());
            psqlStore.save(expected);
            Post result = psqlStore.findById(expected.getId());
            assertThat(expected.getVacancyName(), is(result.getVacancyName()));
            assertThat(expected.getVacancyURL(), is(result.getVacancyURL()));
            assertThat(expected.getVacancyDesc(), is(result.getVacancyDesc()));
            assertThat(expected.getAuthorName(), is(result.getAuthorName()));
            assertThat(expected.getAuthorURL(), is(result.getAuthorURL()));
            assertThat(expected.getDateOfCreation(), is(result.getDateOfCreation()));
        }
    }

    @Test
    public void whenFindByIdThenReturnExpected() throws Exception {
        try (PsqlStore psqlStore = new PsqlStore(ConnectionRollback.create(this.init()))) {
            Post expected = new Post("Java Junior", "vacancy url",
                    "vacancy desc", "author name", "author url",
                    Calendar.getInstance(), Calendar.getInstance());
            psqlStore.save(expected);
            Post result = psqlStore.findById(expected.getId());
            assertThat(expected.getVacancyName(), is(result.getVacancyName()));
            assertThat(expected.getVacancyURL(), is(result.getVacancyURL()));
            assertThat(expected.getVacancyDesc(), is(result.getVacancyDesc()));
            assertThat(expected.getAuthorName(), is(result.getAuthorName()));
            assertThat(expected.getAuthorURL(), is(result.getAuthorURL()));
            assertThat(expected.getDateOfCreation(), is(result.getDateOfCreation()));
        }
    }
}
