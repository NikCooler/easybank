package org.craftedsw.service.user.profile;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.query.Query;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQuery implements Query {

    public final UserId userId;

    public UserProfileQuery(UserId userId) {
        this.userId = userId;
    }
}
