package org.craftedsw.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.craftedsw.aggregate.TransactionId;

/**
 * @author Nikolay Smirnov
 */
@JsonSerialize
public class MoneyTransferCompletedEvent extends EventBase<TransactionId> {
    private static final long serialVersionUID = 1L;

    public MoneyTransferCompletedEvent(TransactionId aggregateId) {
        super(aggregateId);
    }

    /**
     * This constructor is used only for deserialization
     */
    public MoneyTransferCompletedEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, TransactionId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
