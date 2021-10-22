package org.easybank.type;

/**
 * @author Nikolay Smirnov
 */
public class StatementAct {

    private final Amount change;
    private final Amount total;

    public StatementAct(Amount change, Amount total) {
        this.change = change;
        this.total = total;
    }

    public Amount getChange() {
        return change;
    }

    public Amount getTotal() {
        return total;
    }
}
