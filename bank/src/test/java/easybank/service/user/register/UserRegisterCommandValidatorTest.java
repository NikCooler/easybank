package org.easybank.service.user.register;

import org.easybank.aggregate.UserAggregateState;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandValidationException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * @author Nikolay Smirnov
 */
public class UserRegisterCommandValidatorTest {

    private static final UserId USER_ID = UserId.random();

    private UserRegisterCommandValidator validator = new UserRegisterCommandValidator();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testValidate_UserExists() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("User already exists!");

        var command   = new UserRegisterCommand(USER_ID);
        var  aggregate = (UserAggregateState) USER_ID.newInstanceState();

        // when
        boolean result = validator.validate(command, aggregate);

        // then
    }

    @Test
    public void testValidate_EmailIncorrect() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Email is not valid!");

        UserRegisterCommand command = new UserRegisterCommand(USER_ID);
        command.setEmail("invalid-email");

        // when
        boolean result = validator.validate(command, null);

        // then
    }

    @Test
    public void testValidate_ValidCommand() {
        // given
        UserRegisterCommand command = new UserRegisterCommand(USER_ID);
        command.setEmail("nikolay.smirnov@gmail.com");

        // when
        boolean result = validator.validate(command, null);

        // then
        Assert.assertTrue(result);
    }
}