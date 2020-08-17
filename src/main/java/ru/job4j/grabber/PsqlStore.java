package ru.job4j.grabber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private Connection conn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            conn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        createTable();
    }

    private void createTable() {
        try (Statement st = conn.createStatement()) {
            String sql = String.join(
                    "", Files.readAllLines(Path.of("./db", "Create_table_post.sql")));
            st.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Calendar convertToCalendar(Timestamp ts) {
        Calendar javaDate = Calendar.getInstance();
        javaDate.setTimeInMillis(ts.getTime());
        return javaDate;
    }

    @Override
    public void save(Post post) {
        LOG.debug("Saving post with name: {}", post.getVacancyName());
        try (PreparedStatement st = conn.prepareStatement(
                "insert into post("
                        + "name, text, link, created, author, author_URL, last_commented)"
                        + "values(?, ?, ?, ?, ?, ?, ?)")) {
            st.setString(1, post.getVacancyName());
            st.setString(2, post.getVacancyDesc());
            st.setString(3, post.getVacancyURL());
            st.setTimestamp(4, new Timestamp(post.getDateOfCreation().getTime().getTime()));
            st.setString(5, post.getAuthorName());
            st.setString(6, post.getAuthorURL());
            st.setTimestamp(7, new Timestamp(post.getDateOfLatestComment().getTime().getTime()));
            st.executeUpdate();
            LOG.debug("Query executed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOG.debug("Saving complete");
    }

    @Override
    public List<Post> getAll() {
        LOG.debug("Retrieving posts from database");
        List<Post> list = new ArrayList<>();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery("select * from post");
            while (rs.next()) {
                Post post = new Post();
                post.setId(String.valueOf((rs.getInt("id"))));
                post.setVacancyName(rs.getString("name"));
                post.setVacancyDesc(rs.getString("text"));
                post.setVacancyURL(rs.getString("link"));
                post.setDateOfCreation(convertToCalendar(rs.getTimestamp("created")));
                post.setAuthorName(rs.getString("author"));
                post.setAuthorURL(rs.getString("author_url"));
                post.setDateOfLatestComment(convertToCalendar(rs.getTimestamp("last_commented")));
                list.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(String id) {
        LOG.debug("Finding by id");
        Post post = null;
        try (PreparedStatement st = conn.prepareStatement("select * from post where id = ?")) {
            st.setInt(1, Integer.parseInt(id));
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                post = new Post();
                post.setId(String.valueOf((rs.getInt("id"))));
                post.setVacancyName(rs.getString("name"));
                post.setVacancyDesc(rs.getString("text"));
                post.setVacancyURL(rs.getString("link"));
                post.setDateOfCreation(convertToCalendar(rs.getTimestamp("created")));
                post.setAuthorName(rs.getString("author"));
                post.setAuthorURL(rs.getString("author_url"));
                post.setDateOfLatestComment(convertToCalendar(rs.getTimestamp("last_commented")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        LOG.debug("Found: {}", post);
        return post;
    }

    @Override
    public void close() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    public static void main(String[] args) {
        Properties cfg = new Properties();
        try (InputStream in = PsqlStore.class.getClassLoader()
             .getResourceAsStream("grabber.properties")) {
            cfg.load(in);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }

        PsqlStore psqlStore = new PsqlStore(cfg);
        SqlRuParse sqlRuParse = new SqlRuParse(lang -> lang.toLowerCase().contains("java"));
        List<Post> posts = sqlRuParse.list("https://www.sql.ru/forum/job-offers/");
        Post fifthPost = posts.get(4);
        Post tenthPost = posts.get(10);
        psqlStore.save(fifthPost);
        psqlStore.save(tenthPost);
        Post fifth = psqlStore.findById("1");
        Post tenth = psqlStore.findById("2");
        System.out.println(fifth.toString());
        System.out.println(tenth.toString());

        List<Post> list = psqlStore.getAll();
        list.forEach(System.out::println);
    }
}
