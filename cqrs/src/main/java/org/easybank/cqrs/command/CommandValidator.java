package org.easybank.cqrs.command;

import org.easybank.aggregate.AggregateStateBase;

/**
 * Command validator
 *
 * @author Nikolay Smirnov
 */
public interface CommandValidator<CMD extends CommandBase> {

    /**
     * @param command to be validated
     * @param aggregateState last aggregate state. Might be <code>NULL</code> if an aggregate is <code>NEW</code>
     *                       and doesn't exist in the Event Store
     *
     * @return <code>TRUE</code> if the command is <code>VALID</code>
     * @throws {@link CommandValidationException} if the command is <code>NOT VALID</code>
     */
    boolean validate(CMD command, AggregateStateBase aggregateState);

}
