package org.easybank.service.user.moneyaccount.deposit;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandProcessorBase;
import org.easybank.event.DepositEvent;
import org.easybank.event.EventBase;
import org.easybank.type.Amount;
import org.easybank.writelane.EventStoreService;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public class DepositAccountCommandProcessor extends CommandProcessorBase<DepositAccountCommand, UserId> {

    public DepositAccountCommandProcessor(EventStoreService eventStoreService, DepositAccountCommandValidator validator) {
        super(eventStoreService, validator);
    }

    @Override
    protected List<EventBase<?>> buildEvents(DepositAccountCommand command, AggregateStateBase<UserId> aggregateState) {
        var depositEvent = new DepositEvent(aggregateState.getAggregateId());
        var amount = Amount.of(command.getCurrency(), command.getValue());
        depositEvent.setAmount(amount);

        return List.of(aggregateState.setupAggregateVersion(depositEvent));
    }

}
