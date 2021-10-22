package org.easybank.writelane;

import org.easybank.aggregate.AggregateIdBase;
import org.easybank.aggregate.AggregateStateBase;
import org.easybank.event.EventBase;
import org.easybank.model.tables.records.EventStoreRecord;
import org.easybank.util.EventDeserializer;
import org.easybank.writelane.eventbus.EventSavedIdProducer;
import org.easybank.writelane.repository.EventStoreRepository;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * @author Nikolay Smirnov
 */
public class EventStoreServiceImpl implements EventStoreService {

    private final EventStoreRepository eventStoreRepository;
    private final DSLContext           writeModelContext;
    private final EventSavedIdProducer eventSavedIdProducer;

    public EventStoreServiceImpl(EventStoreRepository eventStoreRepository, DSLContext writeModelContext, EventSavedIdProducer eventSavedIdProducer) {
        this.eventStoreRepository = eventStoreRepository;
        this.writeModelContext    = writeModelContext;
        this.eventSavedIdProducer = eventSavedIdProducer;
    }

    public void appendEvents(final List<EventBase<?>> events) {
        Queue<Long> eventIdsQueue = doInTransaction(txContext -> {
            return events.stream()
                    .map(e -> {
                        validateAggregateVersion(txContext, e);
                        return eventStoreRepository.appendEvent(txContext, e);
                    })
                    .collect(Collectors.toCollection(ArrayDeque::new));
        });

        eventSavedIdProducer.pushEventIdsToQueue(eventIdsQueue);
    }

    @Override
    public <ID extends AggregateIdBase<ID>> AggregateStateBase<ID> retrieveAggregate(final ID aggregateId) {
        List<EventBase<ID>> events = doInTransaction(txContext ->
            eventStoreRepository.listEventRecords(txContext, aggregateId)
                    .stream()
                    .map(rec -> (EventStoreRecord) rec)
                    .map(EventDeserializer::<ID>deserialize)
                    .collect(Collectors.toList())
        );

        if (events.isEmpty()) {
            return null;
        }
        return buildAggregateState(events);
    }

    private <ID extends AggregateIdBase<ID>> AggregateStateBase<ID> buildAggregateState(List<EventBase<ID>> events) {
        EventBase<ID> firstEvent = events.get(0);
        ID aggregateId = firstEvent.getAggregateId();
        AggregateStateBase<ID> aggregateState = aggregateId.newInstanceState();
        aggregateState.applyEvents(events);

        return aggregateState;
    }

    private Integer getLastAggregateVersion(DSLContext txContext, AggregateIdBase<?> aggregateId) {
        return ofNullable(eventStoreRepository.getLastAggregateVersion(txContext, aggregateId))
                .map(v -> v + 1)
                .orElse(1);
    }

    private void validateAggregateVersion(DSLContext txContext, EventBase e) {
        Integer lastAggregateVersion = getLastAggregateVersion(txContext, e.getAggregateId());
        if (!Objects.equals(lastAggregateVersion, e.getAggregateVersion())) {
            throw new ConcurrentModificationException("Aggregate version is incorrect for aggregate by id [ " + e.getAggregateId()
                    + " ]. Required [ " + lastAggregateVersion + " ], Actual [ " + e.getAggregateVersion() + "]");
        }
    }

    private <T> T doInTransaction(Function<DSLContext, T> transactionFunc) {
        return writeModelContext.transactionResult(txConfig -> {
            var txContext = DSL.using(txConfig);

            return transactionFunc.apply(txContext);
        });
    }
}
