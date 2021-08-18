package org.craftedsw.service.user.profile;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.query.QueryProcessorBase;
import org.craftedsw.dto.UserDto;
import org.craftedsw.model.tables.records.UserRecord;
import org.jooq.DSLContext;

import static org.craftedsw.model.tables.User.USER;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQueryProcessor extends QueryProcessorBase<UserProfileQuery, UserDto> {

    public UserProfileQueryProcessor(DSLContext readModelContext, UserProfileQueryValidator validator) {
        super(readModelContext, validator);
    }

    @Override
    protected UserDto buildPayload(UserProfileQuery query) {
        UserRecord userRec = readModelContext.selectFrom(USER)
                .where(USER.USER_ID.eq(UserId.valueOf(query.userId)))
                .fetchOne();

        return new UserDto(userRec.get(USER.USER_ID), userRec.get(USER.EMAIL));
    }
}
