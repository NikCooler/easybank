package org.easybank.service.user.register;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.cqrs.command.CommandValidationException;
import org.easybank.cqrs.command.CommandValidator;
import org.easybank.util.EmailValidator;

import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public class UserRegisterCommandValidator implements CommandValidator<UserRegisterCommand> {

    @Override
    public boolean validate(UserRegisterCommand command, AggregateStateBase aggregateState) {
        if (Objects.nonNull(aggregateState)) {
            throw new CommandValidationException("User already exists!");
        }
        if (!EmailValidator.isValid(command.getEmail())) {
            throw new CommandValidationException("Email is not valid!");
        }
        return true;
    }
}
