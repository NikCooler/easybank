package org.easybank.util;

import org.easybank.aggregate.UserId;
import org.easybank.event.EventBase;
import org.easybank.event.UserRegisteredEvent;
import org.easybank.model.tables.records.EventStoreRecord;
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
        record.setEventType("org.easybank.event.UserRegisteredEvent");
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
        record.setEventType("org.easybank.event.NotExistsEvent");

        // when
        EventDeserializer.deserialize(record);

        // then
    }
}