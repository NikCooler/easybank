/*
 * This file is generated by jOOQ.
 */
package org.easybank.model.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EventStore implements Serializable {

    private static final long serialVersionUID = 673782475;

    private Long    eventId;
    private String  aggregateId;
    private Integer aggregateVersion;
    private String  eventType;
    private String  eventData;
    private Long    eventTimestamp;

    public EventStore() {}

    public EventStore(EventStore value) {
        this.eventId = value.eventId;
        this.aggregateId = value.aggregateId;
        this.aggregateVersion = value.aggregateVersion;
        this.eventType = value.eventType;
        this.eventData = value.eventData;
        this.eventTimestamp = value.eventTimestamp;
    }

    public EventStore(
        Long    eventId,
        String  aggregateId,
        Integer aggregateVersion,
        String  eventType,
        String  eventData,
        Long    eventTimestamp
    ) {
        this.eventId = eventId;
        this.aggregateId = aggregateId;
        this.aggregateVersion = aggregateVersion;
        this.eventType = eventType;
        this.eventData = eventData;
        this.eventTimestamp = eventTimestamp;
    }

    public Long getEventId() {
        return this.eventId;
    }

    public EventStore setEventId(Long eventId) {
        this.eventId = eventId;
        return this;
    }

    public String getAggregateId() {
        return this.aggregateId;
    }

    public EventStore setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
        return this;
    }

    public Integer getAggregateVersion() {
        return this.aggregateVersion;
    }

    public EventStore setAggregateVersion(Integer aggregateVersion) {
        this.aggregateVersion = aggregateVersion;
        return this;
    }

    public String getEventType() {
        return this.eventType;
    }

    public EventStore setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public String getEventData() {
        return this.eventData;
    }

    public EventStore setEventData(String eventData) {
        this.eventData = eventData;
        return this;
    }

    public Long getEventTimestamp() {
        return this.eventTimestamp;
    }

    public EventStore setEventTimestamp(Long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final EventStore other = (EventStore) obj;
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        }
        else if (!eventId.equals(other.eventId))
            return false;
        if (aggregateId == null) {
            if (other.aggregateId != null)
                return false;
        }
        else if (!aggregateId.equals(other.aggregateId))
            return false;
        if (aggregateVersion == null) {
            if (other.aggregateVersion != null)
                return false;
        }
        else if (!aggregateVersion.equals(other.aggregateVersion))
            return false;
        if (eventType == null) {
            if (other.eventType != null)
                return false;
        }
        else if (!eventType.equals(other.eventType))
            return false;
        if (eventData == null) {
            if (other.eventData != null)
                return false;
        }
        else if (!eventData.equals(other.eventData))
            return false;
        if (eventTimestamp == null) {
            if (other.eventTimestamp != null)
                return false;
        }
        else if (!eventTimestamp.equals(other.eventTimestamp))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.eventId == null) ? 0 : this.eventId.hashCode());
        result = prime * result + ((this.aggregateId == null) ? 0 : this.aggregateId.hashCode());
        result = prime * result + ((this.aggregateVersion == null) ? 0 : this.aggregateVersion.hashCode());
        result = prime * result + ((this.eventType == null) ? 0 : this.eventType.hashCode());
        result = prime * result + ((this.eventData == null) ? 0 : this.eventData.hashCode());
        result = prime * result + ((this.eventTimestamp == null) ? 0 : this.eventTimestamp.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("EventStore (");

        sb.append(eventId);
        sb.append(", ").append(aggregateId);
        sb.append(", ").append(aggregateVersion);
        sb.append(", ").append(eventType);
        sb.append(", ").append(eventData);
        sb.append(", ").append(eventTimestamp);

        sb.append(")");
        return sb.toString();
    }
}