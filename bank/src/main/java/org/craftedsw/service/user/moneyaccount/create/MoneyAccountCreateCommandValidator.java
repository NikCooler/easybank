package org.craftedsw.service.user.moneyaccount.create;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.cqrs.command.CommandValidator;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public class MoneyAccountCreateCommandValidator implements CommandValidator<MoneyAccountCreateCommand> {

    @Override
    public boolean validate(MoneyAccountCreateCommand command, AggregateStateBase aggregateState) {
        if (Objects.isNull(aggregateState)) {
            throw new CommandValidationException("User is not found by id [ " + command.getAggregateId() + " ]");
        }
        var state = (UserAggregateState) aggregateState;
        if (Objects.isNull(command.getCurrency())) {
            throw new CommandValidationException("Currency is required!");
        }
        if (state.getMoneyAccounts().containsKey(command.getCurrency())) {
            throw new CommandValidationException("Money account for currency [ " + command.getCurrency() + " ] already exists for user [ " + command.getAggregateId() + " ]");
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
