package org.craftedsw.aggregate;

import org.craftedsw.event.UserRegisteredEvent;

/**
 * @author Nikolay Smirnov
 */
public class UserAggregateState extends AggregateStateBase<UserId> {

    private String email;

    UserAggregateState(UserId aggregateId) {
        super(aggregateId);
    }

    public void apply(UserRegisteredEvent event) {
        this.email = event.getEmail();
    }

    public String getEmail() {
        return email;
    }
}
