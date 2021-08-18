package org.craftedsw.util;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.event.EventBase;
import org.craftedsw.event.UserRegisteredEvent;
import org.craftedsw.model.tables.records.EventStoreRecord;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Nikolay Smirnov
 */
public class EventDeserializerTest {

    private static final UserId USER_ID = UserId.random();

    @Test
    public void testDeserializeUserRegisteredEvent() {
        // given
        String email = "mornov.nikolay@gmail.com";
        EventStoreRecord record = new EventStoreRecord();
        record.setAggregateId(USER_ID.toString());
        record.setAggregateVersion(1);
        record.setEventId(10L);
        record.setEventType("org.craftedsw.event.UserRegisteredEvent");
        record.setEventData("{\"email\":\"" + email + "\"}");
        record.setEventTimestamp(System.currentTimeMillis());

        // when
        EventBase result = EventDeserializer.deserialize(record);

        // then
        Assert.assertTrue(result instanceof UserRegisteredEvent);
        var event = (UserRegisteredEvent) result;
        Assert.assertEquals(email,   event.getEmail());
        Assert.assertEquals(USER_ID, event.getAggregateId());
        Assert.assertEquals(10L,     event.getEventId().longValue());
    }

    @Test(expected = Exception.class)
    public void testDeserializeBrokenEventType() {
        // given
        var record = new EventStoreRecord();
        record.setEventType("org.craftedsw.event.NotExistsEvent");

        // when
        EventDeserializer.deserialize(record);

        // then
    }
}