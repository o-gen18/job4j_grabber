package ru.job4j;

import org.quartz.Scheduler;
import ru.job4j.database.Store;
import ru.job4j.grabber.Grabber;
import ru.job4j.parser.SqlRuParse;

import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        Predicate<String> language = vacancyName -> vacancyName.toLowerCase().contains("java");
        grab.init(new SqlRuParse(language), store, scheduler);
        grab.web(store);
    }
}
