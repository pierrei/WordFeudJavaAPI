package nu.mrpi.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Pierre Ingmansson
 */
public class DateUtil {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {

        @Override
        public DateFormat get() {
            return super.get();
        }

        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DATE_FORMAT);
        }

        @Override
        public void remove() {
            super.remove();
        }

        @Override
        public void set(final DateFormat value) {
            super.set(value);
        }

    };

    public static Date convert(final double timestamp) {
        return new Date((long) (timestamp * 1000));
    }

    public static String format(final double timestamp) {
        return df.get().format(convert(timestamp));
    }
}
