package org.easybank.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.easybank.aggregate.TransactionId;
import org.easybank.type.TransactionStatus;

/**
 * @author Nikolay Smirnov
 */
@JsonSerialize
public class TransactionCompletedEvent extends EventBase<TransactionId> {
    private static final long serialVersionUID = 1L;

    public TransactionStatus status;

    public TransactionCompletedEvent(TransactionId aggregateId) {
        super(aggregateId);
        this.status = TransactionStatus.COMPLETED;
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
    public TransactionCompletedEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, TransactionId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
