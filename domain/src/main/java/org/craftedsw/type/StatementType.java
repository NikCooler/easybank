package org.craftedsw.type;

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
}
