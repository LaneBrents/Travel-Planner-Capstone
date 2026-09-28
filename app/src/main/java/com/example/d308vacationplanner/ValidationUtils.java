package com.example.d308vacationplanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class ValidationUtils {

    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_LOCATION_LENGTH = 100;

    private ValidationUtils() {
        // Prevent instantiation of this utility class.
    }

    public static boolean isValidDate(String dateText) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat(DATE_FORMAT, Locale.US);

        dateFormat.setLenient(false);

        try {
            Date parsedDate = dateFormat.parse(dateText);
            return parsedDate != null
                    && dateFormat.format(parsedDate).equals(dateText);
        } catch (ParseException e) {
            return false;
        }
    }

    public static Date parseDate(String dateText) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat(DATE_FORMAT, Locale.US);

        dateFormat.setLenient(false);

        try {
            return dateFormat.parse(dateText);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isValidTitle(String title) {
        return title != null
                && !title.trim().isEmpty()
                && title.trim().length() <= MAX_TITLE_LENGTH;
    }

    public static boolean isValidLocation(String location) {
        return location != null
                && !location.trim().isEmpty()
                && location.trim().length() <= MAX_LOCATION_LENGTH;
    }
}
