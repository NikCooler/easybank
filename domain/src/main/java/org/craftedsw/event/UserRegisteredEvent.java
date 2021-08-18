package org.craftedsw.event;

import org.craftedsw.aggregate.UserId;

/**
 * @author Nikolay Smirnov
 */
public class UserRegisteredEvent extends EventBase<UserId> {
    private static final long serialVersionUID = 1L;

    private String email;

    public UserRegisteredEvent(UserId aggregateId) {
        super(aggregateId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * This constructor is used only for deserialization
     */
    public UserRegisteredEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, UserId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
