package org.craftedsw.writelane;

import org.craftedsw.aggregate.AggregateIdBase;
import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.event.EventBase;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public interface EventStoreService {

    /**
     * Add event into the Event Store table
     */
    void appendEvents(final List<EventBase> events);

    <ID extends AggregateIdBase<ID>> AggregateStateBase<ID> retrieveAggregate(ID aggregateId);

}
