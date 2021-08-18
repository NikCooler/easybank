package org.craftedsw.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Nikolay Smirnov
 */
public abstract class AggregateIdBase<ID extends AggregateIdBase<ID>> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID aggregateId;

    private ReentrantLock lock;

    protected AggregateIdBase(UUID pId) {
        aggregateId = pId;
    }

    @JsonIgnore
    public UUID getAggregateId() {
        return aggregateId;
    }

    public abstract AggregateStateBase<ID> newInstanceState();

    @SuppressWarnings("unchecked")
    public ID tryLock() {
        lock = AggregateLockingCache.getInstance().getLock(this);
        try {
            if (lock.tryLock()) {
                return (ID) this;
            }
        } catch (Exception nope) {
            // NOPE
        }
        throw new AggregateLockingException("Can not lock aggregate by id [ " + this + " ]");
    }

    ReentrantLock getLock() {
        return lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AggregateIdBase<?> that = (AggregateIdBase<?>) o;
        return aggregateId.equals(that.aggregateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aggregateId);
    }

    @JsonValue
    @JsonUnwrapped
    @Override
    public String toString() {
        return aggregateId.toString();
    }

}
