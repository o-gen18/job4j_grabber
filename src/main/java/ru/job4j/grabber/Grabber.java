package ru.job4j.grabber;

import com.sun.net.httpserver.HttpServer;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.database.PsqlStore;
import ru.job4j.database.Store;
import ru.job4j.model.Post;
import ru.job4j.parser.Parse;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.concurrent.Executors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final Properties cfg = new Properties();

    public Store store() {
        return new PsqlStore(cfg);
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg() throws IOException {
        try (InputStream in = PsqlStore.class.getClassLoader()
                .getResourceAsStream("grabber.properties")) {
            cfg.load(in);
        }
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            for (Post post : parse.list("https://www.sql.ru/forum/job-offers/")) {
                store.save(post);
            }
        }
    }

    public void web(Store store) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 9000), 0);
            server.createContext("/vacancies", exchange -> {
                List<Post> posts = store.getAll();
                StringJoiner html = new StringJoiner(System.lineSeparator());
                html.add("<!DOCTYPE html>");
                html.add("<html>");
                html.add("<head>");
                html.add("<meta charset=\"UTF-8\">");
                html.add("<title>Vacancies</title>");
                html.add("</head>");
                html.add("<body>");

                html.add("<table style=\"border: 1px solid black;\">");
                html.add("<tr style=\"border: 1px solid black;\">");
                html.add("<th style=\"border: 1px solid black;\">Name</th>");
                html.add("<th style=\"border: 1px solid black;\">Date</th>");
                html.add("<th style=\"border: 1px solid black;\">Description</th>");
                html.add("</tr>");

                for (Post post : posts) {
                    html.add("<tr style=\"border: 1px solid black;\">");
                    html.add(String.format(
                            "<td style=\"border: 1px solid black;\"><a href=\"%s\">%s</a></td>",
                            post.getVacancyURL(), post.getVacancyName()));
                    html.add(String.format("<td style=\"border: 1px solid black;\">%s</td>",
                            new SimpleDateFormat("dd.MM.yyyy HH:mm")
                                    .format(post.getDateOfCreation().getTime())));
                    html.add(String.format("<td style=\"border: 1px solid black;\">%s</td>",
                            post.getVacancyDesc()));
                    html.add("</tr>");
                }

                html.add("</table>");

                html.add("</body>");
                html.add("</html>");

                byte[] bytes = html.toString().getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().put("Content-Type",
                        List.of("text/html", "charset=UTF-8"));
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                    os.flush();
                }
            });
            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void web2(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(
                    Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {

                            out.write(post.toString().getBytes(Charset.forName("Windows-1251")));
                            out.write(System.lineSeparator().getBytes());
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
