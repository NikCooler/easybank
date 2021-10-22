package org.easybank.service.moneytransfer.request;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.aggregate.TransactionId;
import org.easybank.cqrs.command.CommandProcessorBase;
import org.easybank.event.EventBase;
import org.easybank.event.TransactionStartedEvent;
import org.easybank.type.*;
import org.easybank.writelane.EventStoreService;

import java.util.List;

/**
 * Create a new request to transfer money from one account to another.
 * Money is not yet transferred between accounts.
 * There are 2 steps:
 * - Send a request to transfer money -> Transfer is in progress (confirmation is required)
 * - Confirm the transfer request. For example: It could be a response from a bank ( {@link org.easybank.service.moneytransfer.confirm.MoneyTransferConfirmCommandProcessor} )
 *
 * @see org.easybank.service.moneytransfer.confirm.MoneyTransferConfirmCommandProcessor
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
