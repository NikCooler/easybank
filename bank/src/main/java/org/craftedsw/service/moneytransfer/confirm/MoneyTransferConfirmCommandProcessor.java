package org.craftedsw.service.moneytransfer.confirm;

import org.craftedsw.aggregate.*;
import org.craftedsw.cqrs.command.CommandProcessorBase;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.event.*;
import org.craftedsw.type.Amount;
import org.craftedsw.type.TransactionStatus;
import org.craftedsw.writelane.EventStoreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

/**
 * Confirm money transfer request from one account to another.
 * Money is being transferred from one account to another.
 * <p>
 * Foe example:
 * - It could be a confirmation request from a bank
 *
 * @author Nikolay Smirnov
 */
public class MoneyTransferConfirmCommandProcessor extends CommandProcessorBase<MoneyTransferConfirmCommand, TransactionId> {

    public MoneyTransferConfirmCommandProcessor(EventStoreService eventStoreService, MoneyTransferConfirmCommandValidator validator) {
        super(eventStoreService, validator);
    }

    /**
     * Lock a user money is transferred from AND a user money is transferred to
     * to prevent inconsistency and support atomicity
     */
    @Override
    protected void beforeCommandExecution(MoneyTransferConfirmCommand command) {
        var transactionState = (TransactionAggregateState) eventStoreService.retrieveAggregate(command.getAggregateId());
        var transaction = transactionState.getTransaction();
        var transfer = transaction.getTransfer();
        command.transferFromId = transfer.getWithdrawFrom();
        command.transferToId = transfer.getDepositTo();

        ofNullable(command.transferFromId).ifPresent(UserId::tryLock);
        ofNullable(command.transferToId).ifPresent(UserId::tryLock);
    }

    @Override
    protected List<EventBase<?>> buildEvents(MoneyTransferConfirmCommand command, AggregateStateBase<TransactionId> aggregateState) {
        var transactionState = (TransactionAggregateState) aggregateState;
        var transaction = transactionState.getTransaction();
        var transfer = transaction.getTransfer();
        List<EventBase<?>> events = new ArrayList<>(3);
        if (!verifyCommand(command, transactionState, events)) {
            return unmodifiableList(events);
        }

        var withdrawFrom = buildWithdrawEvent(transfer.getWithdrawFrom(), command.getAggregateId(), transactionState.getAmount());
        events.add(withdrawFrom);

        var depositTo = buildDepositEvent(transfer.getDepositTo(), command.getAggregateId(), transactionState.getAmount());
        events.add(depositTo);

        events.add(
                transactionState.setupAggregateVersion(
                        new TransactionCompletedEvent(command.getAggregateId())
                )
        );

        return unmodifiableList(events);
    }

    private EventBase<UserId> buildWithdrawEvent(UserId userSenderId, TransactionId transactionId, Amount amount) {
        var userSenderState = (UserAggregateState) eventStoreService.retrieveAggregate(userSenderId);
        var withdrawFrom = new WithdrawnEvent(userSenderId);
        withdrawFrom.setTransactionId(transactionId);
        withdrawFrom.setAmount(amount);

        return userSenderState.setupAggregateVersion(withdrawFrom);
    }

    private EventBase<UserId> buildDepositEvent(UserId userReceiverId, TransactionId transactionId, Amount amount) {
        var userReceiverState = (UserAggregateState) eventStoreService.retrieveAggregate(userReceiverId);
        var depositTo = new DepositEvent(userReceiverId);
        depositTo.setTransactionId(transactionId);
        depositTo.setAmount(amount);

        return userReceiverState.setupAggregateVersion(depositTo);
    }

    private boolean verifyCommand(MoneyTransferConfirmCommand command, TransactionAggregateState transactionState, List<EventBase<?>> events) {
        try {
            commandValidator.validate(command, transactionState);
        } catch (Exception ex) {
            var failedEvent = new TransactionFailedEvent(command.getAggregateId());
            failedEvent.setErrorMessage(ex.getMessage());
            events.add(failedEvent);

            return false;
        }
        return true;
    }

    /**
     * Mock the validation method
     * and execute full validation inside this method {@link MoneyTransferConfirmCommandProcessor#buildEvents(MoneyTransferConfirmCommand, AggregateStateBase)}
     */
    @Override
    protected void validateCommand(MoneyTransferConfirmCommand command, AggregateStateBase<TransactionId> aggregateState) {
        if (Objects.isNull(aggregateState)) {
            throw new CommandValidationException("Money transfer request is not found by id [ " + command.getAggregateId() + " ]");
        }
        var transactionState = (TransactionAggregateState) aggregateState;
        var transaction = transactionState.getTransaction();
        if (transaction.getStatus() != TransactionStatus.PROCESSING) {
            throw new CommandValidationException("Money transfer request by id [ " + command.getAggregateId() + " ] has incorrect status [ " + transaction.getStatus() + " ]");
        }
    }

    @Override
    protected void afterCommandExecution(MoneyTransferConfirmCommand command) {
        AggregateLockingCache lockingCache = AggregateLockingCache.getInstance();
        ofNullable(command.transferFromId).ifPresent(lockingCache::unlockAll);
        ofNullable(command.transferToId).ifPresent(lockingCache::unlockAll);
    }
}
