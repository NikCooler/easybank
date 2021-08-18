package org.craftedsw.event;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.Currency;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferStartedEvent extends EventBase<TransactionId> {
    private static final long serialVersionUID = 1L;

    private UserId transferFrom;
    private UserId transferTo;
    private Currency currency;
    private BigDecimal value;

    public MoneyTransferStartedEvent(TransactionId aggregateId) {
        super(aggregateId);
    }

    public UserId getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(UserId transferFrom) {
        this.transferFrom = transferFrom;
    }

    public UserId getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(UserId transferTo) {
        this.transferTo = transferTo;
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

    /**
     * This constructor is used only for deserialization
     */
    public MoneyTransferStartedEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, TransactionId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }
}
