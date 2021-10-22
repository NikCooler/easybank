package org.easybank.writelane;

import org.easybank.aggregate.AggregateIdBase;
import org.easybank.aggregate.AggregateStateBase;
import org.easybank.event.EventBase;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public interface EventStoreService {

    /**
     * Add event into the Event Store table
     */
    void appendEvents(final List<EventBase<?>> events);

    <ID extends AggregateIdBase<ID>> AggregateStateBase<ID> retrieveAggregate(ID aggregateId);

}
