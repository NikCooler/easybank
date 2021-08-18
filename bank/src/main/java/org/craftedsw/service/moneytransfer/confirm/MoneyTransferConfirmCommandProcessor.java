package org.craftedsw.service.moneytransfer.confirm;

import org.craftedsw.aggregate.*;
import org.craftedsw.cqrs.command.CommandProcessorBase;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.event.*;
import org.craftedsw.type.TransferStatus;
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
        var transferState = (MoneyTransferAggregateState) eventStoreService.retrieveAggregate(command.getAggregateId());
        command.transferFromId = transferState.getTransferFrom();
        command.transferToId = transferState.getTransferTo();

        ofNullable(command.transferFromId).ifPresent(UserId::tryLock);
        ofNullable(command.transferToId).ifPresent(UserId::tryLock);
    }

    @Override
    protected List<EventBase<?>> buildEvents(MoneyTransferConfirmCommand command, AggregateStateBase<TransactionId> aggregateState) {
        var moneyTransferState = (MoneyTransferAggregateState) aggregateState;
        List<EventBase<?>> events = new ArrayList<>(3);

        try {
            commandValidator.validate(command, moneyTransferState);
        } catch (Exception ex) {
            var failedEvent = new MoneyTransferFailedEvent(command.getAggregateId());
            failedEvent.setErrorMessage(ex.getMessage());
            events.add(failedEvent);

            return unmodifiableList(events);
        }

        var userFromState = (UserAggregateState) eventStoreService.retrieveAggregate(moneyTransferState.getTransferFrom());
        var userToState = (UserAggregateState) eventStoreService.retrieveAggregate(moneyTransferState.getTransferTo());

        var userFrom = new MoneyWithdrawnEvent(moneyTransferState.getTransferFrom());
        userFrom.setTransactionId(moneyTransferState.getAggregateId());
        userFrom.setCurrency(moneyTransferState.getCurrency());
        userFrom.setValue(moneyTransferState.getValue());

        events.add(userFromState.setupAggregateVersion(userFrom));

        var userTo = new MoneyToppedUpEvent(moneyTransferState.getTransferTo());
        userTo.setTransactionId(moneyTransferState.getAggregateId());
        userTo.setCurrency(moneyTransferState.getCurrency());
        userTo.setValue(moneyTransferState.getValue());

        events.add(userToState.setupAggregateVersion(userTo));

        events.add(
                moneyTransferState.setupAggregateVersion(
                        new MoneyTransferCompletedEvent(command.getAggregateId())
                )
        );

        return unmodifiableList(events);
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
        MoneyTransferAggregateState transferState = (MoneyTransferAggregateState) aggregateState;

        if (transferState.getTransferStatus() != TransferStatus.PROCESSING) {
            throw new CommandValidationException("Money transfer request by id [ " + command.getAggregateId() + " ] has incorrect status [ " + transferState.getTransferStatus() + " ]");
        }
    }

    @Override
    protected void afterCommandExecution(MoneyTransferConfirmCommand command) {
        AggregateLockingCache lockingCache = AggregateLockingCache.getInstance();
        ofNullable(command.transferFromId).ifPresent(lockingCache::unlockAll);
        ofNullable(command.transferToId).ifPresent(lockingCache::unlockAll);
    }
}
