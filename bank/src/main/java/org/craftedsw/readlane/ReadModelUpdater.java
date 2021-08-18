package org.craftedsw.readlane;

import org.craftedsw.event.*;
import org.craftedsw.model.tables.records.EventStoreRecord;
import org.craftedsw.writelane.eventbus.EventStoredIdSubscriber;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.lang.reflect.Method;

import static org.craftedsw.model.tables.EventStore.EVENT_STORE;
import static org.craftedsw.model.tables.User.USER;
import static org.craftedsw.util.EventDeserializer.deserialize;

/**
 * @author Nikolay Smirnov
 */
public class ReadModelUpdater implements EventStoredIdSubscriber {

    private final DSLContext readModelContext;

    public ReadModelUpdater(DSLContext readModelContext) {
        this.readModelContext = readModelContext;
    }

    @Override
    public void onSubscribe(Long eventStoredId) {
        readModelContext.transaction(conf -> {
            DSLContext txContext = DSL.using(conf);
            EventStoreRecord eventRec = txContext.selectFrom(EVENT_STORE)
                    .where(EVENT_STORE.EVENT_ID.eq(eventStoredId))
                    .fetchOne();

            EventBase<?> event = deserialize(eventRec);
            Method apply = this.getClass().getMethod("apply", event.getClass());
            apply.invoke(this, event);
        });
    }

    public void apply(UserRegisteredEvent event) {
        readModelContext.insertInto(USER)
                .set(USER.USER_ID, event.getAggregateId())
                .set(USER.EMAIL,   event.getEmail())
                .execute();
    }
}
