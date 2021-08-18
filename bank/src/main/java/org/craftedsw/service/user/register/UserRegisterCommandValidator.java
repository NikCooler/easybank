package org.craftedsw.service.user.register;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.cqrs.command.CommandValidator;
import org.craftedsw.util.EmailValidator;

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
