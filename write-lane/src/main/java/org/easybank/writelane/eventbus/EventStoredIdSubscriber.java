package org.easybank.writelane.eventbus;

/**
 * @author Nikolay Smirnov
 */
public interface EventStoredIdSubscriber {

    void onSubscribe(Long eventStoredId);

}
