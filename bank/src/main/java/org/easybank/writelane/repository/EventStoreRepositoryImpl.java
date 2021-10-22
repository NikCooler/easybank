package org.easybank.writelane.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.easybank.aggregate.AggregateIdBase;
import org.easybank.event.EventBase;
import org.easybank.model.tables.records.EventStoreRecord;
import org.jooq.DSLContext;
import org.jooq.InsertSetMoreStep;

import java.util.List;

import static org.easybank.model.tables.EventStore.EVENT_STORE;
import static org.easybank.util.SneakyThrow.doWithRuntimeException;

/**
 * @author Nikolay Smirnov
 */
public class EventStoreRepositoryImpl implements EventStoreRepository {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Long appendEvent(final DSLContext txContext, final EventBase<?> event) {
        String eventData = doWithRuntimeException(() -> OBJECT_MAPPER.writeValueAsString(event));

        InsertSetMoreStep<EventStoreRecord> step = txContext.insertInto(EVENT_STORE)
                .set(EVENT_STORE.AGGREGATE_ID,      event.getAggregateId().toString())
                .set(EVENT_STORE.AGGREGATE_VERSION, event.getAggregateVersion())
                .set(EVENT_STORE.EVENT_TIMESTAMP,   event.getEventTimestamp())
                .set(EVENT_STORE.EVENT_DATA,        eventData)
                .set(EVENT_STORE.EVENT_TYPE,        event.getClass().getName());

        return step.returning(EVENT_STORE.EVENT_ID).fetchOne().getEventId();
    }

    @Override
    public Integer getLastAggregateVersion(DSLContext txContext, AggregateIdBase<?> aggregateId) {
        return txContext.select(EVENT_STORE.AGGREGATE_VERSION)
                .from(EVENT_STORE)
                .where(EVENT_STORE.AGGREGATE_ID.eq(aggregateId.toString()))
                .orderBy(EVENT_STORE.EVENT_ID.desc())
                .limit(1)
                .fetchOne(EVENT_STORE.AGGREGATE_VERSION);
    }

    @SuppressWarnings("unchecked")
    public List<EventStoreRecord> listEventRecords(final DSLContext txContext, final AggregateIdBase aggregateId) {
        return txContext.selectFrom(EVENT_STORE)
                .where(EVENT_STORE.AGGREGATE_ID.eq(aggregateId.toString()))
                .fetch();
    }
}
