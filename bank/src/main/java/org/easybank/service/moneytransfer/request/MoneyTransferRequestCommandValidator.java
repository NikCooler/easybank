package org.easybank.service.moneytransfer.request;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.aggregate.UserAggregateState;
import org.easybank.cqrs.command.CommandValidationException;
import org.easybank.cqrs.command.CommandValidator;
import org.easybank.service.moneytransfer.MoneyTransferValidator;
import org.easybank.type.Amount;
import org.easybank.writelane.EventStoreService;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

import static java.lang.String.format;

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
            throw new CommandValidationException(format("Money transfer request already exists by id [ %s ]", command.getAggregateId()));
        }

        validate(cmd -> Objects.isNull(cmd.getTransferFrom()), command, "Field 'transferFrom' must NOT be empty");
        validate(cmd -> Objects.isNull(cmd.getTransferTo()),   command, "Field 'transferTo' must NOT be empty");
        validate(cmd -> Objects.isNull(cmd.getCurrency()),     command, "Field 'currency' must NOT be empty");
        validate(cmd -> Objects.isNull(cmd.getValue()),        command, "Field 'value' must NOT be empty");

        var userSenderState = (UserAggregateState) eventStoreService.retrieveAggregate(command.getTransferFrom());
        var userReceiverState = (UserAggregateState) eventStoreService.retrieveAggregate(command.getTransferTo());
        validateUsers(userSenderState, userReceiverState, command);

        var amount = Amount.of(command.getCurrency(), command.getValue());
        transferValidator.validateTransfer(command.getAggregateId(), userSenderState, userReceiverState, amount);
        return true;
    }

    private void validateUsers(UserAggregateState userSenderState, UserAggregateState userReceiverState, MoneyTransferRequestCommand command) {
        if (Objects.isNull(userSenderState)) {
            throw new CommandValidationException(format("User 'from' is not found by id [ %s ]", command.getTransferFrom()));
        }

        if (Objects.isNull(userReceiverState)) {
            throw new CommandValidationException(format("User 'to' is not found by id [ %s ]", command.getTransferTo()));
        }

        if (userSenderState.getAggregateId().equals(userReceiverState.getAggregateId())) {
            throw new CommandValidationException("Users are the same!");
        }

        var value = command.getValue();
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CommandValidationException("Money amount must be greater than zero!");
        }

        if (value.scale() > 2) {
            throw new CommandValidationException("Money amount is in incorrect format. Must be '#.##'");
        }
    }

    private static void validate(Predicate<MoneyTransferRequestCommand> validateFunc, MoneyTransferRequestCommand command, String errorMessage) {
        if (validateFunc.test(command)) {
            throw new CommandValidationException(errorMessage);
        }
    }
}
