package org.craftedsw.event;

import org.craftedsw.aggregate.TransactionId;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferFailedEvent extends EventBase<TransactionId> {
    private static final long serialVersionUID = 1L;

    private String errorMessage;

    public MoneyTransferFailedEvent(TransactionId aggregateId) {
        super(aggregateId);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * This constructor is used only for deserialization
     */
    public MoneyTransferFailedEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, TransactionId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
