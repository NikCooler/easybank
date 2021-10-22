package org.easybank.writelane.eventbus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.easybank.util.SneakyThrow.doWithRuntimeException;

/**
 * Async get a message from the queue
 * and notify all subscribers of type {@link EventStoredIdSubscriber}
 *
 * @author Nikolay Smirnov
 */
public class EventSavedIdDispatcher implements Runnable {

    private final LinkedBlockingQueue<Long> savedEventIds;
    private final List<EventStoredIdSubscriber> subscribers = new ArrayList<>();

    public EventSavedIdDispatcher(LinkedBlockingQueue<Long> savedEventIds) {
        this.savedEventIds = savedEventIds;
    }

    public void registerSubscriber(EventStoredIdSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void run() {
        // Instead of WHILE (TRUE) better to use something like Pre Destroy flag
        // But to keep it simple leave it as it is. Maybe mark this Thread as Daemon ?
        while (true) {
            Long eventId = doWithRuntimeException(() -> savedEventIds.poll(Integer.MAX_VALUE, TimeUnit.DAYS));
            subscribers.forEach(s -> s.onSubscribe(eventId));
        }
    }
}
