package org.craftedsw.service.user.moneyaccount.create;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandProcessorBase;
import org.craftedsw.event.EventBase;
import org.craftedsw.event.MoneyAccountCreatedEvent;
import org.craftedsw.type.MoneyAccount;
import org.craftedsw.writelane.EventStoreService;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public class MoneyAccountCreateCommandProcessor extends CommandProcessorBase<MoneyAccountCreateCommand, UserId> {

    public MoneyAccountCreateCommandProcessor(EventStoreService eventStoreService, MoneyAccountCreateCommandValidator validator) {
        super(eventStoreService, validator);
    }

    @Override
    protected List<EventBase<?>> buildEvents(MoneyAccountCreateCommand command, AggregateStateBase<UserId> aggregateState) {
        var event = new MoneyAccountCreatedEvent(command.getAggregateId());
        event.setMoneyAccount(MoneyAccount.of(command.getCurrency(), command.getValue()));
        return List.of(aggregateState.setupAggregateVersion(event));
    }
}
