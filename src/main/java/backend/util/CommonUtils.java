package backend.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CommonUtils {

    public static final String DEFAULT_DATE_TIME = "dd.MM.yyyy HH:mm:ss";

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return "null";
        }
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_TIME);
    }

    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_DATE_TIME);
        if (date == null || date.equals("")) {
            return null;
        }
        return formatter.parse(date);
    }

}
