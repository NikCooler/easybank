package org.easybank.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * @author Nikolay Smirnov
 */
public final class DateUtil {

    private DateUtil() {}

    public static LocalDateTime toLocalDateTime(Long timestampInMillis) {
        if (timestampInMillis == null) {
            return null;
        }

        return LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestampInMillis),
                TimeZone.getDefault().toZoneId()
        );
    }
}
