package org.craftedsw.writelane;

import org.craftedsw.aggregate.AggregateIdBase;
import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.event.EventBase;
import org.craftedsw.model.tables.records.EventStoreRecord;
import org.craftedsw.util.EventDeserializer;
import org.craftedsw.writelane.eventbus.EventSavedIdProducer;
import org.craftedsw.writelane.repository.EventStoreRepository;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import static java.util.Optional.ofNullable;

/**
 * @author Nikolay Smirnov
 */
public class EventStoreServiceImpl implements EventStoreService {

    private final EventStoreRepository eventStoreRepository;
    private final DSLContext           writeModelContext;
    private final EventSavedIdProducer eventSavedIdProducer;

    public EventStoreServiceImpl(
            EventStoreRepository eventStoreRepository,
            DSLContext           writeModelContext,
            EventSavedIdProducer eventSavedIdProducer
    ) {
        this.eventStoreRepository = eventStoreRepository;
        this.writeModelContext    = writeModelContext;
        this.eventSavedIdProducer = eventSavedIdProducer;
    }

    public void appendEvents(final List<EventBase> events) {
        Queue<Long> eventIdQueue = new ArrayDeque<>(events.size());
        writeModelContext.transaction(txConfig -> {
            final DSLContext txContext = DSL.using(txConfig);
            events.forEach(e -> {
                Integer lastAggregateVersion = ofNullable(eventStoreRepository.getLastAggregateVersion(txContext, e.getAggregateId()))
                        .map(v -> v + 1)
                        .orElse(1);
                if (!Objects.equals(lastAggregateVersion, e.getAggregateVersion())) {
                    throw new ConcurrentModificationException("Aggregate version is incorrect for aggregate by id [ " + e.getAggregateId()
                            + " ]. Required [ " + lastAggregateVersion + " ], Actual [ " + e.getAggregateVersion() + "]");
                }
                eventIdQueue.add(eventStoreRepository.appendEvent(txContext, e));
            });
        });

        eventSavedIdProducer.pushEventIdsToQueue(eventIdQueue);
    }

    @Override
    public <ID extends AggregateIdBase<ID>> AggregateStateBase<ID> retrieveAggregate(final ID aggregateId) {
        List<EventBase<ID>> events = new ArrayList<>();
        writeModelContext.transaction(txConfig -> {
            final DSLContext txContext = DSL.using(txConfig);
            eventStoreRepository.listEventRecords(txContext, aggregateId)
                    .stream()
                    .map(rec -> (EventStoreRecord) rec)
                    .map(EventDeserializer::deserialize)
                    .forEach(events::add);
        });

        if (events.isEmpty()) {
            return null;
        }
        AggregateStateBase<ID> aggregateState = events.get(0).getAggregateId().newInstanceState();
        aggregateState.applyEvents(events);
        return aggregateState;
    }
}
