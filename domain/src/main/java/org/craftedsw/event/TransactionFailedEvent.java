package org.craftedsw.event;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.type.TransactionStatus;

/**
 * @author Nikolay Smirnov
 */
public class TransactionFailedEvent extends EventBase<TransactionId> {
    private static final long serialVersionUID = 1L;

    private TransactionStatus status;
    private String errorMessage;

    public TransactionFailedEvent(TransactionId aggregateId) {
        super(aggregateId);
        this.status = TransactionStatus.FAILED;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    /**
     * This constructor is used only for deserialization
     */
    public TransactionFailedEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, TransactionId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
