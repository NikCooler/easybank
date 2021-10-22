package org.easybank.service.user.moneyaccount.create;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandProcessorBase;
import org.easybank.event.EventBase;
import org.easybank.event.MoneyAccountCreatedEvent;
import org.easybank.type.Amount;
import org.easybank.type.MoneyAccount;
import org.easybank.writelane.EventStoreService;

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
        var amount = Amount.of(command.getCurrency(), command.getValue());
        var account = MoneyAccount.of(amount);
        event.setMoneyAccount(account);

        return List.of(aggregateState.setupAggregateVersion(event));
    }
}
