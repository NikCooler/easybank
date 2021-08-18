package org.craftedsw.service.user.register;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandProcessorBase;
import org.craftedsw.event.EventBase;
import org.craftedsw.event.UserRegisteredEvent;
import org.craftedsw.writelane.EventStoreService;

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
    protected List<EventBase> buildEvents(UserRegisterCommand command, AggregateStateBase<UserId> aggregateState) {
        var registeredEvent = new UserRegisteredEvent(command.getAggregateId());
        registeredEvent.setEmail(command.getEmail());
        registeredEvent.setAggregateVersion(1);
        return List.of(registeredEvent);
    }
}
