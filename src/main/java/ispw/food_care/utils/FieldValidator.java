package ispw.food_care.utils;

import java.util.regex.Pattern;

public class FieldValidator {
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    private static final Pattern PHONE_REGEX = Pattern.compile("^[0-9]{7,15}$");

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isEmailValid(String email) {
        return email != null && EMAIL_REGEX.matcher(email).matches();
    }

    public static boolean isPhoneValid(String phone) {
        return phone != null && PHONE_REGEX.matcher(phone).matches();
    }

    public static boolean doPasswordsMatch(String p1, String p2) {
        return p1 != null && p1.equals(p2);
    }

    public static boolean isLengthValid(String value, int min, int max) {
        return value != null && value.length() >= min && value.length() <= max;
    }
}
