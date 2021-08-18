package org.craftedsw.service.moneytransfer.request;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.cqrs.command.CommandProcessorBase;
import org.craftedsw.event.EventBase;
import org.craftedsw.event.TransactionStartedEvent;
import org.craftedsw.type.*;
import org.craftedsw.writelane.EventStoreService;

import java.util.List;

/**
 * Create a new request to transfer money from one account to another.
 * Money is not yet transferred between accounts.
 * There are 2 steps:
 * - Send a request to transfer money -> Transfer is in progress (confirmation is required)
 * - Confirm the transfer request. For example: It could be a response from a bank ( {@link org.craftedsw.service.moneytransfer.confirm.MoneyTransferConfirmCommandProcessor} )
 *
 * @see org.craftedsw.service.moneytransfer.confirm.MoneyTransferConfirmCommandProcessor
 *
 * @author Nikolay Smirnov
 */
public class MoneyTransferRequestCommandProcessor extends CommandProcessorBase<MoneyTransferRequestCommand, TransactionId> {

    public MoneyTransferRequestCommandProcessor(EventStoreService eventStoreService, MoneyTransferRequestCommandValidator validator) {
        super(eventStoreService, validator);
    }

    @Override
    protected List<EventBase<?>> buildEvents(MoneyTransferRequestCommand command, AggregateStateBase<TransactionId> aggregateState) {
        var event = new TransactionStartedEvent(command.getAggregateId());

        var transactionState = TransactionState.of(TransactionType.TRANSFER, TransactionStatus.PROCESSING);
        var transfer = Transfer.of(command.getTransferFrom(), command.getTransferTo());
        var transaction = Transaction.of(transactionState, transfer);

        event.setTransaction(transaction);
        event.setAmount(Amount.of(command.getCurrency(), command.getValue()));
        event.setAggregateVersion(1);

        return List.of(event);
    }
}
