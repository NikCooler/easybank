package org.craftedsw.service.user.moneyaccount.withdraw;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandProcessorBase;
import org.craftedsw.event.EventBase;
import org.craftedsw.event.WithdrawnEvent;
import org.craftedsw.type.Amount;
import org.craftedsw.writelane.EventStoreService;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public class WithdrawAccountCommandProcessor extends CommandProcessorBase<WithdrawAccountCommand, UserId> {

    public WithdrawAccountCommandProcessor(EventStoreService eventStoreService, WithdrawAccountCommandValidator validator) {
        super(eventStoreService, validator);
    }

    @Override
    protected List<EventBase<?>> buildEvents(WithdrawAccountCommand command, AggregateStateBase<UserId> aggregateState) {
        var amount = Amount.of(command.getCurrency(), command.getValue());
        var withdrawEvent = new WithdrawnEvent(aggregateState.getAggregateId());
        withdrawEvent.setAmount(amount);

        return List.of(aggregateState.setupAggregateVersion(withdrawEvent));
    }
}
