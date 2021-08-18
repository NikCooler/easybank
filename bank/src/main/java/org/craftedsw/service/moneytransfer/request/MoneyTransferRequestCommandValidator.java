package org.craftedsw.service.moneytransfer.request;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.cqrs.command.CommandValidator;
import org.craftedsw.type.Currency;
import org.craftedsw.writelane.EventStoreService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferRequestCommandValidator implements CommandValidator<MoneyTransferRequestCommand> {

    private final EventStoreService eventStoreService;

    public MoneyTransferRequestCommandValidator(EventStoreService eventStoreService) {
        this.eventStoreService = eventStoreService;
    }

    @Override
    public boolean validate(MoneyTransferRequestCommand command, AggregateStateBase aggregateState) {

        if (Objects.nonNull(aggregateState)) {
            throw new CommandValidationException("Money transfer request already exists by id [ " + command.getAggregateId() + " ]");
        }

        validate(cmd -> Objects.isNull(cmd.getTransferFrom()), command, "Field 'transferFrom' must NOT be empty");
        validate(cmd -> Objects.isNull(cmd.getTransferTo()),   command, "Field 'transferTo' must NOT be empty");
        validate(cmd -> Objects.isNull(cmd.getCurrency()),     command, "Field 'currency' must NOT be empty");
        validate(cmd -> Objects.isNull(cmd.getValue()),        command, "Field 'value' must NOT be empty");

        var userFromState = (UserAggregateState) eventStoreService.retrieveAggregate(command.getTransferFrom());
        var userToState   = (UserAggregateState) eventStoreService.retrieveAggregate(command.getTransferTo());

        if (Objects.isNull(userFromState)) {
            throw new CommandValidationException("User 'from' is not found by id [ " + command.getTransferFrom() + " ]");
        }

        if (Objects.isNull(userToState)) {
            throw new CommandValidationException("User 'to' is not found by id [ " + command.getTransferTo() + " ]");
        }

        if (userFromState.getAggregateId().equals(userToState.getAggregateId())) {
            throw new CommandValidationException("Users are the same!");
        }

        if (command.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommandValidationException("Money amount must be greater than zero!");
        }

        if (command.getValue().scale() > 2) {
            throw new CommandValidationException("Money amount is in incorrect format. Must be '#.##'");
        }

        String prefixErrorMessage = "Money transfer request [ " + command.getAggregateId() + " ] failed due to ";
        Map<Currency, BigDecimal> userFromAccounts = userFromState.getMoneyAccounts();
        if (!userFromAccounts.containsKey(command.getCurrency())) {
            throw new CommandValidationException(prefixErrorMessage + "'user from' [ " + userFromState.getAggregateId() + " ] " +
                    "doesn't have money account [ '" + command.getCurrency() + "' ]");
        }
        BigDecimal userFromMoney = userFromAccounts.get(command.getCurrency());
        if (userFromMoney.compareTo(command.getValue()) < 0) {
            throw new CommandValidationException(prefixErrorMessage + "'user from' [ " + userFromState.getAggregateId() + " ] " +
                    "doesn't have enough money [ '" + command.getCurrency() + "' ]");
        }
        if (!userToState.getMoneyAccounts().containsKey(command.getCurrency())) {
            throw new CommandValidationException(prefixErrorMessage + "'user to' [ " + userToState.getAggregateId() + " ] " +
                    "doesn't have money account [ '" + command.getCurrency() + "' ]");
        }
        return true;
    }

    private static void validate(Predicate<MoneyTransferRequestCommand> validateFunc, MoneyTransferRequestCommand command, String errorMessage) {
        if (validateFunc.test(command)) {
            throw new CommandValidationException(errorMessage);
        }
    }
}
