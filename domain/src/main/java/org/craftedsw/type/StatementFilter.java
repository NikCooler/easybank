package org.craftedsw.type;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Nikolay Smirnov
 */
public class StatementFilter {

    private final Set<StatementType> types = new HashSet<>();
    private final DateRangeFilter dateRangeFilter;

    public StatementFilter(Long dateFrom, Long dateTo) {
        this.dateRangeFilter = new DateRangeFilter(dateFrom, dateTo);
    }

    public void addAllTypes(Set<StatementType> types) {
        if (Objects.nonNull(types)) {
            this.types.addAll(types);
        }
    }

    public boolean isTypePresent(StatementType type) {
        return types.contains(type);
    }

    public boolean isDateAllowed(LocalDateTime date) {
        return dateRangeFilter.isInRange(date);
    }
}
