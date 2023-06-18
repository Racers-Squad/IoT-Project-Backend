package backend.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CommonUtils {

    public static final String DEFAULT_DATE_TIME = "dd.MM.yyyy HH:mm:ss";

    public static String formatDate(Date date, String format) {
        DateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_TIME);
    }

}