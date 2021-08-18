package org.craftedsw.service.user.moneyaccount.create;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.cqrs.command.CommandValidator;

import java.math.BigDecimal;
import java.util.Objects;

import static java.lang.String.format;

/**
 * @author Nikolay Smirnov
 */
public class MoneyAccountCreateCommandValidator implements CommandValidator<MoneyAccountCreateCommand> {

    @Override
    public boolean validate(MoneyAccountCreateCommand command, AggregateStateBase aggregateState) {
        if (Objects.isNull(aggregateState)) {
            throw new CommandValidationException(format("User is not found by id [ %s ]", command.getAggregateId()));
        }
        var state = (UserAggregateState) aggregateState;
        if (Objects.isNull(command.getCurrency())) {
            throw new CommandValidationException("Currency is required!");
        }
        if (state.getAccounts().containsKey(command.getCurrency())) {
            throw new CommandValidationException(format("Money account for currency [ %s ] already exists for user [ %s ]", command.getCurrency(), command.getAggregateId()));
        }
        if (Objects.isNull(command.getValue())) {
            throw new CommandValidationException("Money amount is required!");
        }
        if (command.getValue().compareTo(BigDecimal.valueOf(0.0)) <=0) {
            throw new CommandValidationException("Money amount must NOT be less or equals '0' !");
        }
        return true;
    }
}
