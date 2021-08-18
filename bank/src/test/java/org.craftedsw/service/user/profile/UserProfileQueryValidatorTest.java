package org.craftedsw.service.user.profile;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.config.DBConfig;
import org.craftedsw.cqrs.query.QueryValidationException;
import org.craftedsw.model.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

import static org.craftedsw.model.tables.User.USER;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQueryValidatorTest {

    private static final UserId PROFILE_EXISTS_USER_ID    = UserId.random();
    private static final UserId PROFILE_NOT_EXIST_USER_ID = UserId.random();

    private UserProfileQueryValidator validator;
    private DSLContext                readModelContext;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setup() {
        readModelContext = DBConfig.getInstance().initDBContext();
        readModelContext.batchInsert(mockUserRecords()).execute();
        validator = new UserProfileQueryValidator(readModelContext);
    }

    @After
    public void destroy() {
        readModelContext.deleteFrom(USER).execute();
    }

    @Test
    public void testIsValid_UserExists() {
        // given
        UserProfileQuery query = new UserProfileQuery(PROFILE_EXISTS_USER_ID);
        // when
        boolean valid = validator.isValid(query);
        // then
        Assert.assertTrue(valid);
    }

    @Test
    public void testIsValid_UserDoesNotExist() {
        // given
        expected.expect(QueryValidationException.class);
        expected.expectMessage("User doesn't exist by id [ " + PROFILE_NOT_EXIST_USER_ID + " ]");

        UserProfileQuery query = new UserProfileQuery(PROFILE_NOT_EXIST_USER_ID);

        // when
        boolean valid = validator.isValid(query);
        // then
    }

    public static List<UserRecord> mockUserRecords() {
        return List.of(
                new UserRecord(PROFILE_EXISTS_USER_ID, "user.exists@gmail.com")
        );
    }
}