package org.easybank.service.user.profile;

import org.easybank.aggregate.UserId;
import org.easybank.cqrs.query.Query;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQuery implements Query {

    public final UserId userId;

    public UserProfileQuery(UserId userId) {
        this.userId = userId;
    }
}
