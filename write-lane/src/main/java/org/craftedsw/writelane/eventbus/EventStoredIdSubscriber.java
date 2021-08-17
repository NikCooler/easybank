package org.craftedsw.writelane.eventbus;

/**
 * @author Nikolay Smirnov
 */
public interface EventStoredIdSubscriber {

    void onSubscribe(Long eventStoredId);

}
