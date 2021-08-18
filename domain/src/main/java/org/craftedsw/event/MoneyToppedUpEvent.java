package org.craftedsw.event;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.Currency;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class MoneyToppedUpEvent extends EventBase<UserId> {
    private static final long serialVersionUID = 1L;

    private Currency currency;
    private BigDecimal value;
    private TransactionId transactionId;

    public MoneyToppedUpEvent(UserId aggregateId) {
        super(aggregateId);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
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
    public MoneyToppedUpEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, UserId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
