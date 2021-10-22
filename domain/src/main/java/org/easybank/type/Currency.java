package org.easybank.type;

/**
 * @author Nikolay Smirnov
 */
public enum Currency {

    EUR("Euro");

    final String title;

    Currency(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
