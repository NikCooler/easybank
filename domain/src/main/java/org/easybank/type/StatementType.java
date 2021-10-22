package org.easybank.type;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public enum StatementType {

    DEBIT("debit"),
    CREDIT("credit");

    private final String title;

    StatementType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static boolean exists(String type) {
        return Arrays.stream(StatementType.values())
                .anyMatch(st -> Objects.equals(st.name(), type));
    }
}
