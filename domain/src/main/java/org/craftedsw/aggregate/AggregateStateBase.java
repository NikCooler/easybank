package org.craftedsw.aggregate;

import org.craftedsw.event.EventBase;

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
        var state = this;

        events.stream()
                .filter(event -> isApplyMethodExist(event, state))
                .forEach(event -> invokeApplyMethod(event, state));
    }

    private boolean isApplyMethodExist(EventBase<ID> event, AggregateStateBase<ID> state) {
        try {
            var stateClass = state.getClass();
            stateClass.getMethod("apply", event.getClass());
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    private void invokeApplyMethod(EventBase<ID> event, AggregateStateBase<ID> state) {
        doWithRuntimeException(() -> {
            var stateClass = state.getClass();
            var methodApply = stateClass.getMethod("apply", event.getClass());
            methodApply.invoke(state, event);
            version++;
        });
    }
}
