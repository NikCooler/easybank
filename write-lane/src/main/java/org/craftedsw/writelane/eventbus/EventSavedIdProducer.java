package org.craftedsw.writelane.eventbus;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.craftedsw.util.SneakyThrow.doWithRuntimeException;

/**
 * Push a message ( in this case <code>Event ID</code> )
 * when an Event has been stored into the Event Store
 *
 * @author Nikolay Smirnov
 */
public class EventSavedIdProducer {

    private final LinkedBlockingQueue<Long> savedEventIds;

    public EventSavedIdProducer(LinkedBlockingQueue<Long> savedEventIds) {
        this.savedEventIds = savedEventIds;
    }

    public void pushEventIdsToQueue(Queue<Long> eventIds) {
        eventIds.forEach(id ->
                doWithRuntimeException(() -> savedEventIds.put(id))
        );
    }
}
