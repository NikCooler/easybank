package org.craftedsw.service.user.moneyaccount.deposit;

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
public class DepositAccountCommandValidator implements CommandValidator<DepositAccountCommand> {

    @Override
    public boolean validate(DepositAccountCommand command, AggregateStateBase aggregateState) {
        if (Objects.isNull(aggregateState)) {
            throw new CommandValidationException(format("User is not found by id [ %s ]", command.getAggregateId()));
        }
        if (Objects.isNull(command.getCurrency())) {
            throw new CommandValidationException("Currency is required!");
        }
        var value = command.getValue();
        if (Objects.isNull(value)) {
            throw new CommandValidationException("Money amount is required!");
        }
        if (value.compareTo(BigDecimal.valueOf(0.0)) <=0) {
            throw new CommandValidationException("Money amount must NOT be less or equals '0' !");
        }
        var state = (UserAggregateState) aggregateState;
        var accounts = state.getAccounts();
        if (!accounts.containsKey(command.getCurrency())) {
            throw new CommandValidationException(format("Money account is not found for currency [ %s ]for user [ %s ]", command.getCurrency(), command.getAggregateId()));
        }
        return true;
    }
}
