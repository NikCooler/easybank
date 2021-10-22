package org.easybank.type;

import java.time.LocalDateTime;

/**
 * @author Nikolay Smirnov
 */
public class TransactionStatement {

    private final StatementType type;
    private final StatementAct act;
    private final LocalDateTime date;

    public TransactionStatement(StatementType type, StatementAct act, LocalDateTime date) {
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

    public StatementAct getAct() {
        return act;
    }
}
