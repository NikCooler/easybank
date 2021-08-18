package org.craftedsw.service.user.profile;

import org.craftedsw.cqrs.query.Query;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQuery implements Query {

    public final String userId;

    public UserProfileQuery(String userId) {
        this.userId = userId;
    }
}
