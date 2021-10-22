package org.easybank.service.user.register;

import org.easybank.aggregate.AggregateStateBase;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandProcessorBase;
import org.easybank.event.EventBase;
import org.easybank.event.UserRegisteredEvent;
import org.easybank.writelane.EventStoreService;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public class UserRegisterCommandProcessor extends CommandProcessorBase<UserRegisterCommand, UserId> {

    public UserRegisterCommandProcessor(
            EventStoreService            eventStoreService,
            UserRegisterCommandValidator validator
    ) {
        super(eventStoreService, validator);
    }

    @Override
    protected List<EventBase<?>> buildEvents(UserRegisterCommand command, AggregateStateBase<UserId> aggregateState) {
        var registeredEvent = new UserRegisteredEvent(command.getAggregateId());
        registeredEvent.setEmail(command.getEmail());
        registeredEvent.setAggregateVersion(1);
        return List.of(registeredEvent);
    }
}
