package org.craftedsw.event;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.Amount;

/**
 * @author Nikolay Smirnov
 */
public class WithdrawnEvent extends EventBase<UserId> {
    private static final long serialVersionUID = 1L;

    private Amount amount;
    private TransactionId transactionId;

    public WithdrawnEvent(UserId aggregateId) {
        super(aggregateId);
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * This constructor is used only for deserialization
     */
    public WithdrawnEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, UserId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
