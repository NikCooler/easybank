package org.craftedsw.service.moneytransfer.request;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.cqrs.command.CommandValidator;
import org.craftedsw.service.moneytransfer.MoneyTransferValidator;
import org.craftedsw.type.Amount;
import org.craftedsw.writelane.EventStoreService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferRequestCommandValidator implements CommandValidator<MoneyTransferRequestCommand> {

    private final EventStoreService eventStoreService;
    private final MoneyTransferValidator transferValidator;

    public MoneyTransferRequestCommandValidator(EventStoreService eventStoreService, MoneyTransferValidator transferValidator) {
        this.eventStoreService = eventStoreService;
        this.transferValidator = transferValidator;
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

        var userSenderState = (UserAggregateState) eventStoreService.retrieveAggregate(command.getTransferFrom());
        var userReceiverState = (UserAggregateState) eventStoreService.retrieveAggregate(command.getTransferTo());

        if (Objects.isNull(userSenderState)) {
            throw new CommandValidationException("User 'from' is not found by id [ " + command.getTransferFrom() + " ]");
        }

        if (Objects.isNull(userReceiverState)) {
            throw new CommandValidationException("User 'to' is not found by id [ " + command.getTransferTo() + " ]");
        }

        if (userSenderState.getAggregateId().equals(userReceiverState.getAggregateId())) {
            throw new CommandValidationException("Users are the same!");
        }

        if (command.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommandValidationException("Money amount must be greater than zero!");
        }

        if (command.getValue().scale() > 2) {
            throw new CommandValidationException("Money amount is in incorrect format. Must be '#.##'");
        }

        var amount = Amount.of(command.getCurrency(), command.getValue());
        transferValidator.validateTransfer(command.getAggregateId(), userSenderState, userReceiverState, amount);
        return true;
    }

    private static void validate(Predicate<MoneyTransferRequestCommand> validateFunc, MoneyTransferRequestCommand command, String errorMessage) {
        if (validateFunc.test(command)) {
            throw new CommandValidationException(errorMessage);
        }
    }
}
