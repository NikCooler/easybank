package org.craftedsw.service.user.profile;

import org.craftedsw.cqrs.query.QueryValidationException;
import org.craftedsw.cqrs.query.QueryValidator;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static org.craftedsw.model.tables.User.USER;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQueryValidator implements QueryValidator<UserProfileQuery> {

    private final DSLContext readModelContext;

    public UserProfileQueryValidator(DSLContext readModelContext) {
        this.readModelContext = readModelContext;
    }

    @Override
    public boolean isValid(UserProfileQuery query) {
        boolean userExists = readModelContext.fetchExists(
                DSL.select(DSL.one())
                        .from(USER)
                        .where(USER.USER_ID.eq(query.userId)));
        if (!userExists) {
            throw new QueryValidationException("User doesn't exist by id [ " + query.userId + " ]");
        }
        return true;
    }
}
