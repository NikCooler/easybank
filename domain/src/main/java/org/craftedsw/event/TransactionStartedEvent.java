package org.craftedsw.event;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.type.Amount;
import org.craftedsw.type.Transaction;

/**
 * @author Nikolay Smirnov
 */
public class TransactionStartedEvent extends EventBase<TransactionId> {
    private static final long serialVersionUID = 1L;

    private Transaction transaction;
    private Amount amount;

    public TransactionStartedEvent(TransactionId aggregateId) {
        super(aggregateId);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    /**
     * This constructor is used only for deserialization
     */
    public TransactionStartedEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, TransactionId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }
}
