package ru.job4j.html;

import org.junit.Test;
import ru.job4j.grabber.SqlRuParse;

import java.util.Calendar;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SqlRuParseTest {
    private final SqlRuParse sqlRuParse = new SqlRuParse(vacancyName -> vacancyName.toLowerCase().contains("java"));

    @Test
    public void whenDate15Jan2020At1735ThenReturnCalendar() {
        String date = "15 янв 20, 17:35";
        Calendar result = sqlRuParse.dateConvert(date);
        Calendar expected = Calendar.getInstance();
        expected.set(2020, Calendar.JANUARY, 15, 17, 35);
        assertThat(result, is(expected));
    }

    @Test
    public void whenDate22Aug1975At355ThenReturnCalendar() {
        String date = "22 авг 75, 3:55";
        Calendar result = sqlRuParse.dateConvert(date);
        Calendar expected = Calendar.getInstance();
        expected.set(1975, Calendar.AUGUST, 22, 3, 55);
        assertThat(result, is(expected));
    }

    @Test
    public void whenTodayAt1201ThenReturnCalendar() {
        String date = "сегодня, 12:01";
        Calendar result = sqlRuParse.dateConvert(date);
        Calendar expected = Calendar.getInstance();
        expected.set(Calendar.HOUR_OF_DAY, 12);
        expected.set(Calendar.MINUTE, 1);
        assertThat(result, is(expected));
    }

    @Test
    public void whenYesterdayAt1421ThenReturnCalendar() {
        String date = "вчера, 14:21";
        Calendar result = sqlRuParse.dateConvert(date);
        Calendar expected = Calendar.getInstance();
        expected.set(Calendar.DAY_OF_MONTH, expected.get(Calendar.DAY_OF_MONTH) - 1);
        expected.set(Calendar.HOUR_OF_DAY, 14);
        expected.set(Calendar.MINUTE, 21);
        assertThat(result, is(expected));
    }
}
