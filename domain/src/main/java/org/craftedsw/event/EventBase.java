package org.craftedsw.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.craftedsw.aggregate.AggregateIdBase;

import java.io.Serializable;

import static java.lang.System.currentTimeMillis;

/**
 * @author Nikolay Smirnov
 */
public abstract class EventBase<ID extends AggregateIdBase<ID>> implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Long    eventId;
    private final ID      aggregateId;
    private final Long    eventTimestamp;
    private       Integer aggregateVersion;

    public EventBase(ID aggregateId) {
        // event id will be setup after deserialization from event store
        this.eventId          = null;
        this.aggregateId      = aggregateId;
        this.aggregateVersion = null;
        this.eventTimestamp   = currentTimeMillis();
    }

    /**
     * This constructor is used only for deserialization
     */
    EventBase(long eventId, ID aggregateId, int aggregateVersion, long eventTimestamp) {
        this.eventId          = eventId;
        this.aggregateId      = aggregateId;
        this.aggregateVersion = aggregateVersion;
        this.eventTimestamp   = eventTimestamp;
    }

    @JsonIgnore
    public Long getEventId() {
        return eventId;
    }

    @JsonIgnore
    public ID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateVersion(Integer aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
    }

    @JsonIgnore
    public Integer getAggregateVersion() {
        return aggregateVersion;
    }

    @JsonIgnore
    public Long getEventTimestamp() {
        return eventTimestamp;
    }
}
