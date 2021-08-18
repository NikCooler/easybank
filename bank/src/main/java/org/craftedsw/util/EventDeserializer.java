package org.craftedsw.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.craftedsw.aggregate.AggregateIdBase;
import org.craftedsw.event.EventBase;
import org.craftedsw.model.tables.records.EventStoreRecord;

import java.lang.reflect.Constructor;

import static org.craftedsw.util.SneakyThrow.doWithRuntimeException;
import static java.lang.Class.forName;
import static java.util.Arrays.stream;

/**
 * @author Nikolay Smirnov
 */
public final class EventDeserializer {

    private static final int EVENT_BASE_CONSTRUCTOR_ARGS_COUNT = 4;
    private static final ObjectMapper OM = new ObjectMapper();

    private EventDeserializer() {}

    /**
     * Deserialize DB record form {@link EventStoreRecord} to derived class of type {@link EventBase}
     */
    public static <ID extends AggregateIdBase<ID>> EventBase<ID> deserialize(EventStoreRecord rec) {
        return doWithRuntimeException(() -> {
            String className = rec.getEventType();
            Class<?> clazz = forName(className);
            Constructor<?> constructor = stream(clazz.getConstructors())
                    .filter(EventDeserializer::isConstructorMatched)
                    .findFirst()
                    .orElseThrow();
            Object abstractEvent = constructor
                    .newInstance(rec.getEventId(), rec.getAggregateId(), rec.getAggregateVersion(), rec.getEventTimestamp());
            return OM.readerForUpdating(abstractEvent).<EventBase<ID>>readValue(rec.getEventData());
        });
    }

    private static boolean isConstructorMatched(Constructor<?> constructor) {
        if (constructor.getParameterCount() != EVENT_BASE_CONSTRUCTOR_ARGS_COUNT) {
            return false;
        }
        Class<?>[] params = constructor.getParameterTypes();

        boolean match = params[0].equals(long.class);
        match &= params[1].equals(String.class);
        match &= params[2].equals(int.class);
        match &= params[3].equals(long.class);
        return match;
    }
}
