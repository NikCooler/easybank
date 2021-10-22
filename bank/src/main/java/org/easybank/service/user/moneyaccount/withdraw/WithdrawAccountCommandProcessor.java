package org.easybank.service.user.moneyaccount.withdraw;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandProcessorBase;
import org.easybank.event.EventBase;
import org.easybank.event.WithdrawnEvent;
import org.easybank.type.Amount;
import org.easybank.writelane.EventStoreService;

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
