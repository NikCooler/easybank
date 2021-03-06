package org.easybank.event;

import org.easybank.aggregate.TransactionId;
import org.easybank.aggregate.UserId;
import org.easybank.type.Amount;

/**
 * @author Nikolay Smirnov
 */
public class DepositEvent extends EventBase<UserId> {
    private static final long serialVersionUID = 1L;

    private Amount amount;
    private TransactionId transactionId;

    public DepositEvent(UserId aggregateId) {
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
    public DepositEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, UserId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
