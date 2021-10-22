package org.easybank.service.moneytransfer.confirm;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.aggregate.TransactionAggregateState;
import org.easybank.aggregate.UserAggregateState;
import org.easybank.cqrs.command.CommandValidationException;
import org.easybank.cqrs.command.CommandValidator;
import org.easybank.service.moneytransfer.MoneyTransferValidator;
import org.easybank.type.TransactionStatus;
import org.easybank.writelane.EventStoreService;

import java.util.Objects;

import static java.lang.String.format;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferConfirmCommandValidator implements CommandValidator<MoneyTransferConfirmCommand> {

    private final EventStoreService eventStoreService;
    private final MoneyTransferValidator transferValidator;

    public MoneyTransferConfirmCommandValidator(EventStoreService eventStoreService, MoneyTransferValidator transferValidator) {
        this.eventStoreService = eventStoreService;
        this.transferValidator = transferValidator;
    }

    @Override
    public boolean validate(MoneyTransferConfirmCommand command, AggregateStateBase aggregateState) {
        if (Objects.isNull(aggregateState)) {
            throw new CommandValidationException(format("Money transfer request is not found by id [ %s ]", command.getAggregateId()));
        }
        var transactionState = (TransactionAggregateState) aggregateState;
        var transaction = transactionState.getTransaction();
        var transfer = transaction.getTransfer();
        var transferAmount = transactionState.getAmount();
        if (transaction.getStatus() != TransactionStatus.PROCESSING) {
            throw new CommandValidationException(format("Money transfer request by id [ %s ] has incorrect status [ %s ]",
                    command.getAggregateId(), transaction.getStatus()));
        }

        var userSenderState = (UserAggregateState) eventStoreService.retrieveAggregate(transfer.getWithdrawFrom());
        var userReceiverState = (UserAggregateState) eventStoreService.retrieveAggregate(transfer.getDepositTo());
        transferValidator.validateTransfer(command.getAggregateId(), userSenderState, userReceiverState, transferAmount);

        return true;
    }
}
