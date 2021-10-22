package org.easybank.util;

import java.util.Objects;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * @author Nikolay Smirnov
 */
public final class EmailValidator {

    private EmailValidator() {}

    private static final String  PART = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*";

    private static final Pattern FIRST_PART = Pattern.compile(PART, CASE_INSENSITIVE);
    private static final Pattern SECOND_PART = Pattern.compile(PART + "\\.[a-z]{2,}", CASE_INSENSITIVE);

    public static boolean isValid(String email) {
        if (Objects.isNull(email) || email.length() <= 6 || email.length() > 200) {
            return false;
        }
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }
        var firstPartMatcher = FIRST_PART.matcher(parts[0]);
        var secondPartMatcher = SECOND_PART.matcher(parts[1]);

        return firstPartMatcher.matches() && secondPartMatcher.matches();
    }
}
