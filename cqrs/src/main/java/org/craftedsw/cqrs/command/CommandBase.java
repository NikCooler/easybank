package org.craftedsw.cqrs.command;

import org.craftedsw.aggregate.AggregateIdBase;

/**
 * @author Nikolay Smirnov
 */
public abstract class CommandBase<ID extends AggregateIdBase<ID>> {

    private final ID aggregateId;

    public CommandBase(ID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public ID getAggregateId() {
        return aggregateId;
    }

    public ID tryLock() {
        return aggregateId.tryLock();
    }
}
