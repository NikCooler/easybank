package org.craftedsw.service.user.moneyaccount.deposit;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandProcessorBase;
import org.craftedsw.event.DepositEvent;
import org.craftedsw.event.EventBase;
import org.craftedsw.type.Amount;
import org.craftedsw.writelane.EventStoreService;

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
