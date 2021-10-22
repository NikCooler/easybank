package org.easybank.writelane.repository;

import org.easybank.aggregate.AggregateIdBase;
import org.easybank.event.EventBase;
import org.jooq.DSLContext;
import org.jooq.Record;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public interface EventStoreRepository {

    /**
     * Save a single event in a transaction
     *
     * @param txContext transaction context
     * @param event     event
     *
     * @return Last event id stored in the event store table
     */
    Long appendEvent(DSLContext txContext, EventBase<?> event);

    /**
     * Get all event records for an aggregate by {@link AggregateIdBase}
     */
    <REC extends Record> List<REC> listEventRecords(DSLContext txContext, AggregateIdBase aggregateId);

    /**
     * @param txContext   transaction context
     * @param aggregateId aggregate id
     *
     * @return last aggregate version or <code>NULL</code> if events were not found by the aggregate id
     */
    Integer getLastAggregateVersion(DSLContext txContext, AggregateIdBase<?> aggregateId);

}
