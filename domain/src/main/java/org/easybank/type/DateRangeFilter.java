package org.easybank.type;

import java.time.LocalDateTime;
import java.util.Objects;

import static org.easybank.util.DateUtil.toLocalDateTime;

/**
 * @author Nikolay Smirnov
 */
public class DateRangeFilter {

    private final LocalDateTime timestampFrom;
    private final LocalDateTime timestampTo;

    public DateRangeFilter(Long timestampFrom, Long timestampTo) {
        this.timestampFrom = toLocalDateTime(timestampFrom);
        this.timestampTo = toLocalDateTime(timestampTo);
    }

    public boolean isInRange(LocalDateTime date) {
        if (Objects.nonNull(timestampFrom) && timestampFrom.compareTo(date) > 0) {
            return false;
        }
        if (Objects.nonNull(timestampTo) && timestampTo.compareTo(date) < 0) {
            return false;
        }
        return true;
    }
}
