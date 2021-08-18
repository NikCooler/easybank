package org.craftedsw.cqrs.command;

import org.craftedsw.aggregate.AggregateIdBase;
import org.craftedsw.aggregate.AggregateLockingCache;
import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.cqrs.HttpCode;
import org.craftedsw.cqrs.Response;
import org.craftedsw.event.EventBase;
import org.craftedsw.writelane.EventStoreService;

import java.util.List;

/**
 * Write lane.
 * Command processor for validating, transforming and saving the events
 *
 * @author Nikolay Smirnov
 */
public abstract class CommandProcessorBase<CMD extends CommandBase<ID>, ID extends AggregateIdBase<ID>> {

    protected final EventStoreService eventStoreService;
    protected final CommandValidator<CMD> commandValidator;

    public CommandProcessorBase(EventStoreService eventStoreService, CommandValidator<CMD> commandValidator) {
        this.eventStoreService = eventStoreService;
        this.commandValidator = commandValidator;
    }

    /**
     * Can be override to implement specific pre-processing logic
     */
    protected void beforeCommandExecution(CMD command) {
    }

    /**
     * Execute command and save events to the event store.
     * Push saved event id to a queue to start postprocessing
     */
    public final Response executeCommand(CMD command) {
        try {
            command.tryLock();

            beforeCommandExecution(command);

            AggregateStateBase<ID> aggregateState = eventStoreService.retrieveAggregate(command.getAggregateId());
            validateCommand(command, aggregateState);

            List<EventBase<?>> newEvents = buildEvents(command, aggregateState);
            eventStoreService.appendEvents(newEvents);

            return Response.of(HttpCode.OK);

        } catch (Exception ex) {
            return Response.of(HttpCode.BAD_REQUEST.getCode(), ex.getMessage());
        } finally {
            afterCommandExecution(command);
            AggregateLockingCache.getInstance().unlockAll(command.getAggregateId());
        }
    }

    /**
     * Validate the command
     */
    protected void validateCommand(CMD command, AggregateStateBase<ID> aggregateState) {
        commandValidator.validate(command, aggregateState);
    }

    /**
     * Can be override to implement specific post-processing logic
     */
    protected void afterCommandExecution(CMD command) {
    }

    /**
     * Build events based on the command which came from clients
     */
    protected abstract List<EventBase<?>> buildEvents(CMD command, AggregateStateBase<ID> aggregateState);

}
