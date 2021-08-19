package org.craftedsw.type;

import java.time.LocalDateTime;

/**
 * @author Nikolay Smirnov
 */
public class Statement {

    private final StatementType type;
    private final StatementAct act;
    private final LocalDateTime date;

    public Statement(StatementType type, StatementAct act, LocalDateTime date) {
        this.type = type;
        this.act = act;
        this.date = date;
    }

    public StatementType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String printAct() {
        return "";
    }
}
