package org.craftedsw.aggregate;

import org.craftedsw.event.EventBase;

import java.lang.reflect.Method;
import java.util.List;

import static org.craftedsw.util.SneakyThrow.doWithRuntimeException;

/**
 * The latest aggregate state based on aggregate's events
 *
 * @author Nikolay Smirnov
 */
public abstract class AggregateStateBase<ID extends AggregateIdBase<ID>> {

    private final ID aggregateId;
    private int version = 0;

    protected AggregateStateBase(ID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public ID getAggregateId() {
        return aggregateId;
    }

    public EventBase<ID> setupAggregateVersion(EventBase<ID> event) {
        event.setAggregateVersion(++version);
        return event;
    }

    public void applyEvents(List<EventBase<ID>> events) {
        final AggregateStateBase state = this;
        final Class<?> stateClass = this.getClass();

        events.stream()
                .filter(event -> {
                    try {
                        stateClass.getMethod("apply", event.getClass());
                    } catch (NoSuchMethodException e) {
                        return false;
                    }
                    return true;
                })
                .forEach(event ->
                    doWithRuntimeException(() -> {
                        version++;
                        Method methodApply = stateClass.getMethod("apply", event.getClass());
                        methodApply.invoke(state, event);
                    })
                );
    }

}
